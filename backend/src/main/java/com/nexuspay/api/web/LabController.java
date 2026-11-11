package com.nexuspay.api.web;

import com.nexuspay.api.application.BankService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/lab")
public class LabController {
  private final JdbcTemplate jdbc;
  private final BankService service;

  public LabController(JdbcTemplate jdbc, BankService service) {
    this.jdbc = jdbc;
    this.service = service;
  }

  @PostMapping("/adjust-balance")
  public Map<String, Object> adjust(@RequestParam long accountId, @RequestParam BigDecimal amount) {
    jdbc.update("UPDATE accounts SET balance=balance+? WHERE id=?", amount, accountId);
    return Map.of("accountId", accountId, "balance", service.account(accountId).balance());
  }
}
