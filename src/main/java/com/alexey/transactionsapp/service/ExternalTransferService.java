package com.alexey.transactionsapp.service;

import com.alexey.transactionsapp.externalapi.WithdrawalService;
import com.alexey.transactionsapp.model.Account;
import com.alexey.transactionsapp.model.Address;
import com.alexey.transactionsapp.model.Transaction;
import com.alexey.transactionsapp.persistance.TransferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExternalTransferService {
    private final TransferRepository transferRepository;
    private final AccountService accountService;
    private final AccountLockService accountLockService;

    private final WithdrawalService withdrawalService;
    private final Executor executors =  Executors.newCachedThreadPool();

    public WithdrawalService.WithdrawalId sendMoney(Address from, BigDecimal amount) {
        Optional<Account> fromAccountOpt = accountService.getAccount(from);
        if (fromAccountOpt.isEmpty()) {
            throw new IllegalArgumentException("account does not exists");
        }
        String externalId = getExternalId(from.address());
        WithdrawalService.WithdrawalId withdrawalId = new WithdrawalService.WithdrawalId(UUID.fromString(externalId));
        log.info("start external withdrawal: {}", withdrawalId);
        Account fromAcc = fromAccountOpt.get();

        executors.execute( () -> exec(from, externalId, amount, withdrawalId, fromAcc));

        return withdrawalId;
    }

    private String getExternalId(String address) {
        String externalId = UUID.randomUUID().toString();
        return externalId;
    }


    private Transaction exec(Address from, String externalId, BigDecimal amount, WithdrawalService.WithdrawalId withdrawalId, Account fromAcc) {
        AccountLockService.Result<Transaction> res = accountLockService.lockAndExecute(fromAcc.getAddress(),

                10_000, () -> {
                    if (fromAcc.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0)
                        throw new IllegalStateException("not enough amount");

                    long t = System.currentTimeMillis();

                    withdrawalService.requestWithdrawal(withdrawalId, new WithdrawalService.Address(from.address()), amount);
                    WithdrawalService.WithdrawalState requestState = withdrawalService.getRequestState(withdrawalId);
                    while (requestState == WithdrawalService.WithdrawalState.PROCESSING){
                        //busy wait
                        requestState = withdrawalService.getRequestState(withdrawalId);
                    }
                    if (requestState == WithdrawalService.WithdrawalState.COMPLETED) {
                        fromAcc.debit(amount);
                        accountService.saveAccount(fromAcc);
                    }

                    Transaction transaction = transferRepository.saveTransfer(from, new Address(externalId), amount);
                    log.info("amount sent, elapsed: {}, withdrawalId: {}, transaction: {}",
                            (System.currentTimeMillis() - t), withdrawalId, transaction);
                    return transaction;
                });

        if (!res.status())
            throw new IllegalStateException("can not perform transaction: " + withdrawalId.value().toString());

        return res.result();
    }


    public Optional<WithdrawalService.WithdrawalState> getStatus(String id) {
        try {
            return Optional.ofNullable(withdrawalService.getRequestState(new WithdrawalService.WithdrawalId(UUID.fromString(id))));
        } catch (Exception ignored) {
            return Optional.empty();
        }

    }
}
