package com.paytm.assignment1.repositories;

import com.paytm.assignment1.modals.UserWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<UserWallet,Integer> {
    @Query(value = "Select * from user_wallet w where w.id = (Select wallet_id from user where mobile = ?1)", nativeQuery = true)
    Optional<UserWallet> findByUserMobile(String mobile);
}
