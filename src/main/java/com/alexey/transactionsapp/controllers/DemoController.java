package com.alexey.transactionsapp.controllers;

import com.alexey.transactionsapp.model.Account;
import com.alexey.transactionsapp.service.AccountService;
import com.alexey.transactionsapp.service.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping(path = "/api/v1/demo")
@RequiredArgsConstructor
@Slf4j
public class DemoController {
    private final AccountService accountService;
    private final TransferService transferService;

    @PostMapping("init-accounts")
    public List<Account> initAcc() {
        return IntStream.range(0, 3).mapToObj(i -> {
                    Account account = accountService.createAccount("a" + i);
                    Optional<Account> account1 = transferService.credit(account.getAddress(), BigDecimal.TEN);
                    return account1;
                }).filter(Optional::isPresent).map(Optional::get)
                .collect(Collectors.toList());

    }

}
