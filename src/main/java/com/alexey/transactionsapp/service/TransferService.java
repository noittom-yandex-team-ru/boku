package com.alexey.transactionsapp.service;

import com.alexey.transactionsapp.model.Account;
import com.alexey.transactionsapp.model.Address;
import com.alexey.transactionsapp.model.Transaction;
import com.alexey.transactionsapp.persistance.TransferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService {
    private final TransferRepository transferRepository;
    private final AccountService accountService;
    private final AccountLockService accountLockService;

    public Transaction sendMoney(Address from, Address to, BigDecimal amount) {
        Optional<Account> fromAccountOpt = accountService.getAccount(from);
        Optional<Account> toAccountOpt = accountService.getAccount(to);

        if (fromAccountOpt.isEmpty() || toAccountOpt.isEmpty())
            throw new IllegalArgumentException("account(s) do not exists");
        Account fromAcc = fromAccountOpt.get();
        Account toAcc = toAccountOpt.get();
        AccountLockService.Result<Transaction> res = accountLockService.lockAndExecute(fromAcc.getAddress(), toAcc.getAddress(),
                500, () -> {
                    if (fromAcc.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0)
                        throw new IllegalStateException("not enough amount");
                    fromAcc.debit(amount);
                    toAcc.credit(amount);
                    accountService.saveAccount(fromAcc, toAcc);

                    Transaction transaction = transferRepository.saveTransfer(from, to, amount);
                    log.info("amount sent, transaction: {}", transaction);
                    return transaction;
                });

        if (!res.status())
            throw new IllegalStateException("can not perform transaction");
        return res.result();
    }

    public List<Transaction> getAllTransactions() {
        return transferRepository.findAllTransactions();
    }

    public Optional<Transaction> findTransactionById(String transactionId) {
        return transferRepository.findTransactionById(transactionId);
    }

    public Optional<Account> credit(Address address, BigDecimal amount) {
        return accountService.getAccount(address)
                .map(acc -> {
                    accountLockService.lockAndExecute(acc.getAddress(),
                            500, () -> {
                                acc.credit(amount);
                                accountService.saveAccount(acc);
                                return null;
                            });

                    return acc;
                });
    }
}
