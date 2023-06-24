package com.alexey.transactionsapp.model;

import jakarta.validation.constraints.NotEmpty;

public record Transaction(@NotEmpty String id) {
}
