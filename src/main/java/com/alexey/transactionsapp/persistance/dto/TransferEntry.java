package com.alexey.transactionsapp.persistance.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "transfers")
@NoArgsConstructor
@AllArgsConstructor
public class TransferEntry {
        @Id
        private String transactionId;

}
