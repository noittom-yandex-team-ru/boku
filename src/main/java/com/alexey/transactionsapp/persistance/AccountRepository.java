package com.alexey.transactionsapp.persistance;

import com.alexey.transactionsapp.model.Account;
import com.alexey.transactionsapp.model.Address;
import com.alexey.transactionsapp.persistance.dto.AccountEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface AccountRepository extends JpaRepository<AccountEntry, String> {
    default Optional<Account> findByAddress(Address address) {
        if (address == null)
            return Optional.empty();
        return findById(address.address()).map(e-> new Account(new Address(e.getAddress()), e.getBalance()));
    }

    default Account saveAccount(Account account) {
        if (account == null)
            return null;
        AccountEntry accountEntry = new AccountEntry(account.getAddress().address(), account.getBalance());
        AccountEntry saved = save(accountEntry);

        return new Account(new Address(saved.getAddress()), saved.getBalance());
    }
    default List<Account> saveAccount(Account... accounts) {
        if (accounts == null || accounts.length == 0)
            return null;
        List<AccountEntry> collect = Arrays.stream(accounts)
                .map(a -> new AccountEntry(a.getAddress().address(), a.getBalance()))
                .collect(Collectors.toList());
        List<AccountEntry> accountEntries = saveAll(collect);
        List<Account> savedAccounts = accountEntries.stream()
                .map(e -> new Account(new Address(e.getAddress()), e.getBalance()))
                .collect(Collectors.toList());
        return savedAccounts;
    }

    default List<Account> findAllAccounts() {
        return findAll().stream().map(a->
                new Account(new Address(a.getAddress()), a.getBalance())).collect(Collectors.toList());
    }
}
