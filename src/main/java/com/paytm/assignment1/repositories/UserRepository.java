package com.paytm.assignment1.repositories;

import com.paytm.assignment1.modals.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Integer> {
    public Optional<User> findByUserName(String userName);
    public Optional<User> findByEmail(String email);
    public Optional<User> findByMobile(String mobile);

    @Modifying
    @Query("update User u set u.isActive = :val where u.id = :id")
    public int setUserActiveState(@Param("id") Integer id, @Param("val") boolean value);

    public boolean existsByUserName(String userName);
    public boolean existsByEmail(String email);
    public boolean existsByMobile(String mobile);

}
