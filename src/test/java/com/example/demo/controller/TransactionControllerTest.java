package com.example.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.dto.TransactionDto;
import com.example.demo.dto.TransferDto;
import com.example.demo.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TransactionService transactionService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void should_returnDepositAndStatusOk() throws Exception {
    TransactionDto transactionDto = new TransactionDto();
    transactionDto.setAmount(100.0);

    mockMvc.perform(post("/api/transaction/1/deposit")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(transactionDto)))
        .andExpect(status().isOk());

    Mockito.verify(transactionService).deposit(1L, 100.0);
  }

  @Test
  void should_doWithdrawAndStatusOk() throws Exception {
    TransactionDto transactionDto = new TransactionDto();
    transactionDto.setAmount(50.0);

    mockMvc.perform(post("/api/transaction/1/withdraw")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(transactionDto)))
        .andExpect(status().isOk());

    Mockito.verify(transactionService).withdraw(1L, 50.0);
  }

  @Test
  void should_transferFoundsAndStatusOk() throws Exception {
    TransferDto transferDto = new TransferDto();
    transferDto.setFromAccountId(1L);
    transferDto.setToAccountId(2L);
    transferDto.setAmount(200.0);

    mockMvc.perform(post("/api/transaction/transfer")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(transferDto)))
        .andExpect(status().isOk());

    Mockito.verify(transactionService).transfer(1L, 2L, 200.0);
  }
}
