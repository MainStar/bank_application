package com.example.demo.dto;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDto {

  @Positive(message = "Amount must be greater than zero")
  private double amount;

}
