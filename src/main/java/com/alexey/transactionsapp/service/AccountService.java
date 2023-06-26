package com.alexey.transactionsapp.service;

import com.alexey.transactionsapp.model.Account;
import com.alexey.transactionsapp.model.Address;
import com.alexey.transactionsapp.persistance.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    @Transactional
    public Account createAccount(String address){
        Account account = new Account(new Address(address));
        return accountRepository.saveAccount(account);
    }

    public Optional<Account> getAccount(Address address) {
        return accountRepository.findByAddress(address);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAllAccounts();
    }

    @Transactional
    public void saveAccount(Account... acc) {

        accountRepository.saveAccount(acc);

    }
}
