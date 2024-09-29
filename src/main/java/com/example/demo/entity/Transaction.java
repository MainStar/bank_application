package com.example.demo.entity;

import com.example.demo.dto.TransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false)
  private Account account;

  @Column(name = "amount", nullable = false)
  private double amount;

  @Column(name = "transaction_type", nullable = false)
  private TransactionType transactionType;

  @Column(name = "transaction_date", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date transactionDate;

  public Transaction(Account account, double amount, TransactionType transactionType) {
    this.account = account;
    this.amount = amount;
    this.transactionType = transactionType;
  }
}
