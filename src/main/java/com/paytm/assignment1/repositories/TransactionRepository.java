package com.paytm.assignment1.repositories;

import com.paytm.assignment1.modals.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TransactionRepository extends PagingAndSortingRepository<Transaction,Integer> {

    @Query(value="SELECT * FROM transaction WHERE payee_wallet_id = ?1 OR payer_wallet_id = ?1", nativeQuery = true)
    List<Transaction> findAllByWalletId(int id, Pageable pageable);

}
