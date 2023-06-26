package com.alexey.transactionsapp.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record Transaction(@NotEmpty String id, @NotNull Address from, @NotNull Address to, @PositiveOrZero BigDecimal amount) {
}
