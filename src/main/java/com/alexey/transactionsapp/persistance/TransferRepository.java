package com.alexey.transactionsapp.persistance;

import com.alexey.transactionsapp.persistance.dto.TransferEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<TransferEntry, String> {


}
