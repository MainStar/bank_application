package com.example.demo.controller;

import com.example.demo.dto.TransactionDto;
import com.example.demo.dto.TransferDto;
import com.example.demo.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

  @Autowired
  private TransactionService transactionService;

  @PostMapping("/{id}/deposit")
  public ResponseEntity<Void> deposit(@PathVariable Long id, @Valid @RequestBody TransactionDto transactionDto) {
    transactionService.deposit(id, transactionDto.getAmount());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/{id}/withdraw")
  public ResponseEntity<Void> withdraw(@PathVariable Long id, @Valid @RequestBody TransactionDto transactionDto) {
    transactionService.withdraw(id, transactionDto.getAmount());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/transfer")
  public ResponseEntity<Void> transfer(@Valid @RequestBody TransferDto transferDto) {
    transactionService.transfer(
        transferDto.getFromAccountId(),
        transferDto.getToAccountId(),
        transferDto.getAmount()
    );
    return ResponseEntity.ok().build();
  }
}
