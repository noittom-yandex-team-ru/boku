package com.alexey.transactionsapp.model;

import java.math.BigDecimal;

public record AccountTransfer(Address from, Address to, BigDecimal money, Transaction transaction) {
}
