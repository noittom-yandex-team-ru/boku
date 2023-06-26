package com.alexey.transactionsapp.service;

import com.alexey.transactionsapp.model.Account;
import com.alexey.transactionsapp.model.Address;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountLockServiceTest {

    @Test
    public void lockTest() throws InterruptedException {
        Random rand = new Random(31);
        AccountLockService accountLockService = new AccountLockService();
        List<Account> addressList = List.of(
                new Account(new Address("a1"), BigDecimal.TEN),
                new Account(new Address("a2"), BigDecimal.TEN),
                new Account(new Address("a3"), BigDecimal.TEN)
                );
        int originalSum = addressList.stream().map(Account::getBalance)
                .mapToInt(BigDecimal::intValue)
                .sum();


        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                Account a = addressList.get(rand.nextInt(addressList.size()));
                Account b = addressList.get(rand.nextInt(addressList.size()));
                accountLockService.lockAndExecute(a.getAddress(),b.getAddress(), 10, ()-> {
                    BigDecimal balance = a.getBalance();
                    if (balance.compareTo(BigDecimal.ZERO) <1)
                        return null;
                    a.debit(BigDecimal.ONE);
                    sleep();
                    b.credit(BigDecimal.ONE);
                    sleep();

                    return null;
                });
            });

        }
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        int finalSum = addressList.stream().map(Account::getBalance)
                .mapToInt(BigDecimal::intValue)
                .sum();
        assertEquals(originalSum, finalSum);
    }

    @SneakyThrows
    static void sleep(){
        Thread.sleep(10);
    }

}
