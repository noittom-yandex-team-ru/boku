package com.alexey.transactionsapp.dto;

import com.alexey.transactionsapp.model.Address;
import com.alexey.transactionsapp.model.Money;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record TransactionRequest(
        @NotNull  @Valid Address from,
        @NotNull  @Valid Address to,
        @NotNull  @Valid Money money
) {

}
