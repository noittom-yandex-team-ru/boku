package com.alexey.transactionsapp.model;

public record AccountTransfer(Address from, Address to, Money money, Transaction transaction) {
}
