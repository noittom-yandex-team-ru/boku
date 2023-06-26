package com.alexey.transactionsapp.persistance;

import com.alexey.transactionsapp.model.Account;
import com.alexey.transactionsapp.model.Address;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class RepoTest {
    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void accountRepoTest(){
        Account acc = new Account(new Address("add1"), BigDecimal.TEN);
        Account save = accountRepository.saveAccount(acc);

        Optional<Account> byAddress = accountRepository.findByAddress(acc.getAddress());
        assertTrue(byAddress.isPresent());
        assertEquals(acc, save);
        assertEquals(acc, byAddress.get());
    }
}
