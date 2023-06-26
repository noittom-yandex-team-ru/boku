package com.alexey.transactionsapp.model;

import jakarta.validation.constraints.NotEmpty;

public record Address(@NotEmpty String address) {
}
