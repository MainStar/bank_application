package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferDto {

  @NotNull(message = "Sender account ID is required")
  private Long fromAccountId;

  @NotNull(message = "Receiver account ID is required")
  private Long toAccountId;

  @Positive(message = "Transfer amount must be greater than zero")
  private double amount;

}
