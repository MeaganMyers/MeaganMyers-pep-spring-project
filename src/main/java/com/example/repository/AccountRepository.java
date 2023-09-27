package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    //get the username for adding a new account
    @Query("SELECT username FROM Account WHERE username = :username")
    Optional<Account> findUsername(@Param("username") String username);

    //return the account with it's account_id
    @Query("SELECT a FROM Account a WHERE a.username = :user AND a.password = :pass")
    Optional<Account> getAccount(@Param("user") String username, @Param("pass") String password);

} //end AccountRepository
