package com.example.demo.service;

import com.example.demo.dto.TransactionType;
import com.example.demo.entity.Account;
import com.example.demo.entity.AccountBalance;
import com.example.demo.entity.Transaction;
import com.example.demo.exceptions.AccountException;
import com.example.demo.repository.AccountBalanceRepository;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

  @Autowired
  private TransactionRepository transactionRepository;
  @Autowired
  private AccountBalanceRepository accountBalanceRepository;
  @Autowired
  private AccountService accountService;
  @Autowired
  private AccountRepository accountRepository;

  @Transactional
  public void transfer(Long fromAccountId, Long toAccountId, double amount) {
    Account fromAccount = accountService.getAccount(fromAccountId);
    Account toAccount = accountService.getAccount(toAccountId);

    if (fromAccount.getBalance() < amount) {
      throw new AccountException("Insufficient founds for transfer");
    }

    fromAccount.setBalance(fromAccount.getBalance() - amount);
    toAccount.setBalance(toAccount.getBalance() + amount);

    accountRepository.save(fromAccount);
    accountRepository.save(toAccount);

    Transaction withdrawTransaction = new Transaction(fromAccount, amount, TransactionType.TRANSFER_OUT);
    Transaction depositTransaction = new Transaction(toAccount, amount, TransactionType.TRANSFER_IN);

    transactionRepository.save(withdrawTransaction);
    transactionRepository.save(depositTransaction);
  }

  @Transactional
  public void deposit(Long accountId, double amount) {
    Account account = accountService.getAccount(accountId);
    AccountBalance accountBalance = accountBalanceRepository.findByAccountId(accountId)
        .orElseThrow(() -> new AccountException(String.format("Balance not found for accountId %d", accountId)));

    accountBalance.setBalance(accountBalance.getBalance() + amount);
    accountBalanceRepository.save(accountBalance);

    Transaction transaction = new Transaction(account, amount, TransactionType.DEPOSIT);
    transactionRepository.save(transaction);
  }

  @Transactional
  public void withdraw(Long accountId, double amount) {
    Account account = accountService.getAccount(accountId);
    AccountBalance accountBalance = accountBalanceRepository.findByAccountId(accountId)
        .orElseThrow(() -> new AccountException(String.format("Balance not found for accountId %s", accountId)));

    if (accountBalance.getBalance() < amount) {
      throw new AccountException("Insufficient funds");
    }

    accountBalance.setBalance(accountBalance.getBalance() - amount);
    accountBalanceRepository.save(accountBalance);

    Transaction transaction = new Transaction(account, amount, TransactionType.WITHDRAW);
    transactionRepository.save(transaction);
  }
}
