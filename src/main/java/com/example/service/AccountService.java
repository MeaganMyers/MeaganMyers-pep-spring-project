package com.example.service;

import java.util.Optional;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    
    /**
     * user registration
     * create a new account
     * username cannot be blank and cannot already exist
     * password has to be at least 4 characters long
     */
    public Account addAccount(Account account) throws AuthenticationException {
        Optional<Account> username = accountRepository.findUsername(account.getUsername());
        if(username.isEmpty() && account.getPassword().length() >= 4 && !(account.getUsername().isBlank())) {
            return accountRepository.save(account);
        }
        return null;
    }

    /**
     * check login account
     * username and password provided match a real account existing on the database
     */
    public Optional<Account> checkAccount(String username, String password, Account account) throws AuthenticationException {
        if(username.equals(account.getUsername()) && password.equals(account.getPassword())) {
            return accountRepository.getAccount(username, password);
        }
        return null;
    }

} //end AccountService
