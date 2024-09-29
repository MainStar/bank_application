package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.exceptions.AccountException;
import com.example.demo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(String phoneNumber, double initialBalance) {
        validateAccountExistence(phoneNumber);
        Account account = new Account(phoneNumber,initialBalance);
        return accountRepository.save(account);
    }

    public Account getAccount(Long id) {
        return accountRepository.findById(id)
            .orElseThrow(() -> new AccountException("Account not found"));
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    private void validateAccountExistence(String phoneNumber) throws AccountException {
        Account excitingAccount = accountRepository.findByPhoneNumber(phoneNumber);
        if (excitingAccount != null) {
            throw new AccountException(String.format("Account with such phoneNumber: %s is already exists", phoneNumber));
        }
    }
}
