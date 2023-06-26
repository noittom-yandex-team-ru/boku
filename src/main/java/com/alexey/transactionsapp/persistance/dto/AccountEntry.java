package com.alexey.transactionsapp.persistance.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "accounts")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountEntry {
        @Id
        private String address;
        private BigDecimal balance;

}
