package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.entity.Account;
import com.example.demo.exceptions.AccountException;
import com.example.demo.repository.AccountRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AccountServiceTest {

  private static final Double INITIAL_BALANCE_100 = 100.0;
  private static final Double INITIAL_BALANCE_50 = 50.0;
  private static final String PHONE_NUMBER = "1234567890";
  private static final String PHONE_NUMBER_1 = "1234567891";

  @Mock
  private AccountRepository accountRepository;

  @InjectMocks
  private AccountService accountService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void shouldCreateAccount() {

    when(accountRepository.findByPhoneNumber(PHONE_NUMBER)).thenReturn(null);
    when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Account account = accountService.createAccount(PHONE_NUMBER, INITIAL_BALANCE_100);

    assertNotNull(account);
    assertEquals(PHONE_NUMBER, account.getPhoneNumber());
    assertEquals(INITIAL_BALANCE_100, account.getBalance());
    verify(accountRepository, times(1)).save(any(Account.class));
  }

  @Test
  public void shouldThrowAccountException_whenAccountExistsByProvidedPhoneNumber() {
    Account existingAccount = new Account(PHONE_NUMBER, INITIAL_BALANCE_50);

    when(accountRepository.findByPhoneNumber(PHONE_NUMBER)).thenReturn(existingAccount);

    AccountException thrown = assertThrows(AccountException.class, () -> {
      accountService.createAccount(PHONE_NUMBER, INITIAL_BALANCE_50);
    });

    assertEquals(String.format("Account with such phoneNumber: %s is already exists", PHONE_NUMBER), thrown.getMessage());
  }

  @Test
  public void shouldNotThrowAccountException_whenAccountFoundByProvidedAccountId() {
    Long accountId = 1L;
    Account expectedAccount = createAccount(PHONE_NUMBER);

    when(accountRepository.findById(accountId)).thenReturn(Optional.of(expectedAccount));

    Account account = accountService.getAccount(accountId);

    assertNotNull(account);
    assertEquals(expectedAccount, account);
    verify(accountRepository, times(1)).findById(accountId);
  }

  @Test
  public void shouldThrowAccountException_whenAccountNotFoundByAccountId() {
    Long accountId = 1L;

    when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

    AccountException thrown = assertThrows(AccountException.class, () -> {
      accountService.getAccount(accountId);
    });

    assertEquals("Account not found", thrown.getMessage());
    verify(accountRepository, times(1)).findById(accountId);
  }

  @Test
  public void shouldFindAllAccounts() {
    Account account1 = createAccount(PHONE_NUMBER);
    Account account2 = createAccount(PHONE_NUMBER_1);
    List<Account> accounts = Arrays.asList(account1, account2);

    when(accountRepository.findAll()).thenReturn(accounts);

    List<Account> result = accountService.getAllAccounts();

    assertNotNull(result);
    assertEquals(2, result.size());
    verify(accountRepository, times(1)).findAll();
  }

  private Account createAccount(String phoneNumber) {
    return new Account(phoneNumber, INITIAL_BALANCE_100);
  }
}
