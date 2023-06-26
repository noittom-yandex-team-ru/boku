package com.alexey.transactionsapp.controllers;

import com.alexey.transactionsapp.model.Account;
import com.alexey.transactionsapp.model.Address;
import com.alexey.transactionsapp.service.AccountService;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/account")
@RequiredArgsConstructor
@Slf4j
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/all")
    public List<Account> findAllAccounts(){
        return accountService.getAllAccounts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getById(@NotEmpty @PathVariable String address){
        return ResponseEntity.of(accountService.getAccount(new Address(address)));
    }

}
