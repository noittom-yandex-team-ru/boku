package com.alexey.transactionsapp.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record Money(
        @NotEmpty String ccy,
        @NotNull @PositiveOrZero BigDecimal amount
) {
}
