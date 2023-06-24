package com.alexey.transactionsapp.service;

import com.alexey.transactionsapp.model.Address;
import com.alexey.transactionsapp.model.Money;
import com.alexey.transactionsapp.model.Transaction;
import com.alexey.transactionsapp.persistance.TransferRepository;
import com.alexey.transactionsapp.persistance.dto.TransferEntry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransfersService {
    private final TransferRepository transferRepository;

    public Transaction sendMoney(Address from, Address to, Money money){
        Transaction t = new Transaction(UUID.randomUUID().toString());
        transferRepository.save(new TransferEntry(t.id()));
        log.info("money sent, from: {}, to: {}, money: {}, transaction: {}",
                from, to, money, t);
        return t;
    }

    public List<Transaction> getAllTransactions(){
        return transferRepository.findAll().stream().map(t->new Transaction(t.getTransactionId())).collect(Collectors.toList());
    }

    public Optional<Transaction> getTransaction(String transactionId) {
        return transferRepository.findById(transactionId)
                .map(t->new Transaction(t.getTransactionId()));
    }

}
