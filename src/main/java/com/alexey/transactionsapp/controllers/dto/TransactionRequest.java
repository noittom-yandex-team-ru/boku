package com.alexey.transactionsapp.controllers.dto;

import com.alexey.transactionsapp.model.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record TransactionRequest(
        @NotNull  @Valid Address from,
        @NotNull  @Valid Address to,
        @NotNull  @PositiveOrZero BigDecimal amount
) {

}
