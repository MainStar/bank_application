package com.example.demo.repository;

import com.example.demo.entity.AccountBalance;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountBalanceRepository extends JpaRepository<AccountBalance, Long> {
  Optional<AccountBalance> findByAccountId(Long accountId);
}
