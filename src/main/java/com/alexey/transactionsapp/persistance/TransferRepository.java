package com.alexey.transactionsapp.persistance;

import com.alexey.transactionsapp.model.Address;
import com.alexey.transactionsapp.model.Transaction;
import com.alexey.transactionsapp.persistance.dto.TransferEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public interface TransferRepository extends JpaRepository<TransferEntry, String> {

    default Transaction saveTransfer(Address from, Address to, BigDecimal amount) {
        String id = UUID.randomUUID().toString();
        TransferEntry save = save(new TransferEntry(id, from.address(), to.address(), amount));
        return new Transaction(save.getTransactionId(), from, to, amount);
    }

    default List<Transaction> findAllTransactions() {
        return findAll().stream().map(t->
                new Transaction(t.getTransactionId(), new Address(t.getFromAddress()), new Address(t.getToAddress()), t.getAmount()))
                .collect(Collectors.toList());
    }

    default Optional<Transaction> findTransactionById(String transactionId) {
        return findById(transactionId).map(t->
                new Transaction(t.getTransactionId(), new Address(t.getFromAddress()), new Address(t.getToAddress()), t.getAmount()));
    }
}
