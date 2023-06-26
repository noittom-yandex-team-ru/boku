package com.alexey.transactionsapp.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Account {
    private final Address address;
    private BigDecimal balance;
    public Account(Address address){
        this(address, BigDecimal.ZERO);
    }
    public Account(Address address, BigDecimal balance){
        this.address = address;
        this.balance = balance;;
    }

    public void debit(BigDecimal amnt) {
        BigDecimal newBalance = balance.subtract(amnt);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("negative balance, address: " + address);
        setBalance(newBalance);
    }
    public void credit(BigDecimal amnt) {
        BigDecimal newBalance = balance.add(amnt);
        setBalance(newBalance);
    }
}
