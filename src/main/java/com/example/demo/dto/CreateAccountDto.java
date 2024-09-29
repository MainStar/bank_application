package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountDto {

  @NotBlank(message = "Account holder name is required")
  @Size(min = 10, message = "Phone number must be at least 10 digits long")
  private String phoneNumber;

  @Positive(message = "Initial balance must be greater than zero")
  private double initialBalance;

}
