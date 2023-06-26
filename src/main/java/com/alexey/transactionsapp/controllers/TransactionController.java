package com.alexey.transactionsapp.controllers;

import com.alexey.transactionsapp.controllers.dto.ExternalTransactionRequest;
import com.alexey.transactionsapp.controllers.dto.TransactionRequest;
import com.alexey.transactionsapp.externalapi.WithdrawalService;
import com.alexey.transactionsapp.model.Account;
import com.alexey.transactionsapp.model.Address;
import com.alexey.transactionsapp.model.Transaction;
import com.alexey.transactionsapp.service.AccountService;
import com.alexey.transactionsapp.service.ExternalTransferService;
import com.alexey.transactionsapp.service.TransferService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/transaction")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {
    private final TransferService transferService;
    private final AccountService accountService;

    private final ExternalTransferService externalTransferService;

    @PostMapping("/credit")
    public Optional<Account> credit(Address address, BigDecimal amount){
        transferService.credit(address, amount);
        return accountService.getAccount(address);
    }

    @PostMapping("/send")
    public Transaction sendMoney(@Valid @RequestBody TransactionRequest tReq){
        return transferService.sendMoney(tReq.from(), tReq.to(), tReq.amount());
    }

    @PostMapping("/external/send")
    public String sendExternal(@Valid @RequestBody ExternalTransactionRequest exTReq){

        WithdrawalService.WithdrawalId withdrawalId = externalTransferService.sendMoney(exTReq.from(), exTReq.amount());
        return withdrawalId.value().toString();
    }

    @GetMapping("/external/{id}")
    public ResponseEntity<WithdrawalService.WithdrawalState> getExternalStatus(@NotEmpty @PathVariable String id){
        return  ResponseEntity.of(externalTransferService.getStatus(id));

    }

    @GetMapping("/all")
    public List<Transaction> findAllTransactions(){
        return transferService.getAllTransactions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getById(@NotEmpty @PathVariable String id){
        return ResponseEntity.of(transferService.findTransactionById(id));
    }

}
