package com.paytm.assignment1.repositories;

import com.paytm.assignment1.modals.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,Integer> {
}
