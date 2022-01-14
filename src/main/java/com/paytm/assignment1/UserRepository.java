package com.paytm.assignment1;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Integer> {
    public Optional<User> findByUserName(String userName);
    public Optional<User> findByEmail(String email);
    public Optional<User> findByMobile(String mobile);
}
