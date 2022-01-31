package com.paytm.assignment1.repositories;

import com.paytm.assignment1.modals.UserWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<UserWallet,Integer> {
    @Query("SELECT w from UserWallet w JOIN w.user u WHERE u.mobile = :mobile")
    Optional<UserWallet> findByUserMobile(@Param("mobile") String mobile);
}
