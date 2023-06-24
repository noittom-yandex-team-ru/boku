package com.alexey.transactionsapp.controllers;

import com.alexey.transactionsapp.dto.TransactionRequest;
import com.alexey.transactionsapp.model.Transaction;
import com.alexey.transactionsapp.service.TransfersService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/transaction")
@RequiredArgsConstructor
@Slf4j
public class TransactionsApiController {
    private final TransfersService transfersService;

    @GetMapping("/health")
    public String health(){
        return "OK";
    }

    @PostMapping("/send")
    public Transaction sendMoney(@Valid @RequestBody TransactionRequest tReq){
        return transfersService.sendMoney(tReq.from(), tReq.to(), tReq.money());
    }

    //TODO add pagination
    @GetMapping("/all")
    public List<Transaction> findAllTransactions(){
        return transfersService.getAllTransactions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getById(@NotEmpty @PathVariable String id){
        return ResponseEntity.of(transfersService.getTransaction(id));
    }

}
