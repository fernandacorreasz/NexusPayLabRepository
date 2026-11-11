package com.nexuspay.api.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BankRepository {
  Optional<Customer> customer(String username);

  List<Account> accountsFor(String username);

  Optional<Account> account(long id);

  List<Account> searchAccounts(String term);

  List<TransactionEntry> transactions(long accountId);

  void deposit(long accountId, BigDecimal amount, String description, String counterparty, String requestId);

  void transfer(long sourceId, String targetNumber, BigDecimal amount, String description, String requestId);

  void audit(Long customerId, String action, String details);

  BigDecimal totalBalance();
}
