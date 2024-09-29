package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.dto.CreateAccountDto;
import com.example.demo.entity.Account;
import com.example.demo.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AccountService accountService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void should_createAccount() throws Exception {
    CreateAccountDto createAccountDto = new CreateAccountDto("1234567890", 1000.0);
    Account account = new Account("1234567890", 1000.0);
    account.setId(1L);

    when(accountService.createAccount(anyString(), anyDouble())).thenReturn(account);

    ResultActions result = mockMvc.perform(post("/api/account")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createAccountDto)));

    result.andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.phoneNumber").value("1234567890"))
        .andExpect(jsonPath("$.balance").value(1000.0));
  }

  @Test
  void should_getAccountById() throws Exception {
    Account account = new Account("1234567890", 1000.0);
    account.setId(1L);

    when(accountService.getAccount(1L)).thenReturn(account);

    ResultActions result = mockMvc.perform(get("/api/account/1")
        .contentType(MediaType.APPLICATION_JSON));

    result.andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.phoneNumber").value(1234567890))
        .andExpect(jsonPath("$.balance").value(1000.0));
  }

  @Test
  void should_getAllAccounts() throws Exception {
    Account account1 = new Account("1234567890", 1000.0);
    Account account2 = new Account("1317654321", 2000.0);
    account1.setId(1L);
    account2.setId(2L);
    List<Account> accounts = Arrays.asList(account1, account2);

    when(accountService.getAllAccounts()).thenReturn(accounts);

    ResultActions result = mockMvc.perform(get("/api/account")
        .contentType(MediaType.APPLICATION_JSON));

    result.andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1L))
        .andExpect(jsonPath("$[0].phoneNumber").value("1234567890"))
        .andExpect(jsonPath("$[0].balance").value(1000.0))
        .andExpect(jsonPath("$[1].id").value(2L))
        .andExpect(jsonPath("$[1].phoneNumber").value(1317654321))
        .andExpect(jsonPath("$[1].balance").value(2000.0));
  }
}
