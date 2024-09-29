package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.entity.Account;
import com.example.demo.entity.AccountBalance;
import com.example.demo.entity.Transaction;
import com.example.demo.exceptions.AccountException;
import com.example.demo.repository.AccountBalanceRepository;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.TransactionRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class TransactionServiceTest {

  private static final Long ACCOUNT_ID = 1L;
  private static final String PHONE_NUMBER = "1234567890";
  private static final String PHONE_NUMBER_1 = "1234567891";
  private static final Double AMOUNT_200 = 200.0;
  private static final Double AMOUNT_50 = 50.0;

  @Mock
  private TransactionRepository transactionRepository;

  @Mock
  private AccountBalanceRepository accountBalanceRepository;

  @Mock
  private AccountService accountService;

  @Mock
  private AccountRepository accountRepository;

  @InjectMocks
  private TransactionService transactionService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void should_transferFoundsSuccessfully() {
    Long fromAccountId = 1L;
    Long toAccountId = 2L;
    double amount = 100.0;

    Account fromAccount = new Account(PHONE_NUMBER, AMOUNT_200);
    Account toAccount = new Account(PHONE_NUMBER_1, AMOUNT_50);
    fromAccount.setId(1L);
    toAccount.setId(2L);

    when(accountService.getAccount(fromAccountId)).thenReturn(fromAccount);
    when(accountService.getAccount(toAccountId)).thenReturn(toAccount);

    assertDoesNotThrow(() -> transactionService.transfer(fromAccountId, toAccountId, amount));
    assertEquals(100.0, fromAccount.getBalance());
    assertEquals(150.0, toAccount.getBalance());
    verify(accountRepository, times(1)).save(fromAccount);
    verify(accountRepository, times(1)).save(toAccount);
    verify(transactionRepository, times(2)).save(any(Transaction.class));
  }

  @Test
  void shouldThrowAccountException_whenAccountHasNotEnoughBalance() {
    Long fromAccountId = 1L;
    Long toAccountId = 2L;
    double amount = 300.0;

    Account fromAccount = new Account(PHONE_NUMBER, AMOUNT_200);
    Account toAccount = new Account(PHONE_NUMBER_1, AMOUNT_50);

    when(accountService.getAccount(fromAccountId)).thenReturn(fromAccount);
    when(accountService.getAccount(toAccountId)).thenReturn(toAccount);

    AccountException exception = assertThrows(AccountException.class,
        () -> transactionService.transfer(fromAccountId, toAccountId, amount));

    assertEquals("Insufficient founds for transfer", exception.getMessage());
    verify(accountRepository, Mockito.never()).save(any(Account.class));
    verify(transactionRepository, Mockito.never()).save(any(Transaction.class));
  }

  @Test
  public void shouldUpdateBalance_forProvidedAmountAndAccountId() {
    Account account = new Account(PHONE_NUMBER, AMOUNT_200);
    AccountBalance accountBalance = new AccountBalance();
    accountBalance.setAccount(account);
    accountBalance.setBalance(AMOUNT_200);

    when(accountService.getAccount(ACCOUNT_ID)).thenReturn(account);
    when(accountBalanceRepository.findByAccountId(ACCOUNT_ID)).thenReturn(Optional.of(accountBalance));

    transactionService.deposit(ACCOUNT_ID, 400);

    assertEquals(600.0, accountBalance.getBalance());
    verify(transactionRepository).save(any(Transaction.class));
    verify(accountBalanceRepository).save(accountBalance);
  }

  @Test
  public void shouldThrowAccountException_whenAccountNotFoundById() {
    when(accountService.getAccount(ACCOUNT_ID)).thenThrow(new AccountException("Account not found"));

    Exception exception = assertThrows(AccountException.class, () -> {
      transactionService.deposit(ACCOUNT_ID, AMOUNT_50);
    });

    assertEquals("Account not found", exception.getMessage());
  }

  @Test
  public void shouldNotThrowAccountException_whenWithdrawFounds() {
    Account account = new Account(PHONE_NUMBER, AMOUNT_200);
    AccountBalance accountBalance = new AccountBalance();
    accountBalance.setAccount(account);
    accountBalance.setBalance(AMOUNT_200);

    when(accountService.getAccount(ACCOUNT_ID)).thenReturn(account);
    when(accountBalanceRepository.findByAccountId(ACCOUNT_ID)).thenReturn(Optional.of(accountBalance));

    transactionService.withdraw(ACCOUNT_ID, 100);

    assertEquals(100.0, accountBalance.getBalance());
    verify(transactionRepository).save(any(Transaction.class));
    verify(accountBalanceRepository).save(accountBalance);
  }

  @Test
  public void shouldThrowAccountException_whenBalanceInInsufficientWhileWithdraw() {
    Account account = new Account(PHONE_NUMBER, AMOUNT_200);
    AccountBalance accountBalance = new AccountBalance();
    accountBalance.setAccount(account);
    accountBalance.setBalance(AMOUNT_200);

    when(accountService.getAccount(ACCOUNT_ID)).thenReturn(account);
    when(accountBalanceRepository.findByAccountId(ACCOUNT_ID)).thenReturn(Optional.of(accountBalance));

    Exception exception = assertThrows(AccountException.class, () -> {
      transactionService.withdraw(ACCOUNT_ID, 600);
    });

    assertEquals("Insufficient funds", exception.getMessage());
  }

  @Test
  public void shouldThrowAccountException_whenAccountNotFoundWhileWithdraw() {
    when(accountService.getAccount(ACCOUNT_ID)).thenThrow(new AccountException("Account not found"));

    Exception exception = assertThrows(AccountException.class, () -> {
      transactionService.withdraw(ACCOUNT_ID, 100);
    });

    assertEquals("Account not found", exception.getMessage());
  }
}
