package com.paytm.assignment1.repositories;

import com.paytm.assignment1.modals.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TransactionRepository extends JpaRepository<Transaction,Integer> {

    @Query(value="SELECT * FROM transaction WHERE payee_wallet_id = ?1 OR payer_wallet_id = ?1 ORDER BY create_time", nativeQuery = true)
    Iterable<Transaction> findAllByWalletId(int id);

}
