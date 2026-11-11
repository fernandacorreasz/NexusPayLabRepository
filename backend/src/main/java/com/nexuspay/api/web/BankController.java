package com.nexuspay.api.web;

import com.nexuspay.api.application.BankService;
import com.nexuspay.api.domain.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "basicAuth")
@Tag(name = "NexusPay Banking")
@CrossOrigin(origins = "*")
public class BankController {
  private final BankService service;

  public BankController(BankService service) {
    this.service = service;
  }

  public record DepositRequest(@NotNull @DecimalMin("0.01") BigDecimal amount,
      @NotBlank @Size(max = 240) String description, String counterparty) {
  }

  public record TransferRequest(@NotNull Long sourceAccountId, @NotBlank String targetAccountNumber,
      @NotNull @DecimalMin("0.01") BigDecimal amount, @NotBlank @Size(max = 240) String description) {
  }

  @GetMapping("/customers/me")
  public Customer me(Authentication auth) {
    return service.customer(auth.getName());
  }

  @GetMapping("/accounts")
  public List<Account> accounts(Authentication auth) {
    return service.accounts(auth.getName());
  }

  @GetMapping("/accounts/search")
  public List<Account> search(@RequestParam String term) {
    return service.search(term);
  }

  @GetMapping("/accounts/{id}")
  public Account account(@PathVariable long id) {
    return service.account(id);
  }

  @GetMapping("/accounts/{id}/transactions")
  public List<TransactionEntry> transactions(@PathVariable long id) {
    return service.transactions(id);
  }

  @PostMapping("/accounts/{id}/deposit")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Map<String, Object> deposit(@PathVariable long id,
      @RequestHeader(value = "X-Request-Id", required = false) String requestId,
      @RequestBody @Valid DepositRequest req) {
    service.deposit(id, req.amount(), req.description(), req.counterparty(), requestId);
    return Map.of("status", "CONFIRMED", "accountId", id, "amount", req.amount());
  }

  @PostMapping("/transfers")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Map<String, Object> transfer(@RequestHeader(value = "X-Request-Id", required = false) String requestId,
      @RequestBody @Valid TransferRequest req) {
    service.transfer(req.sourceAccountId(), req.targetAccountNumber(), req.amount(), req.description(), requestId);
    return Map.of("status", "CONFIRMED", "amount", req.amount());
  }

  @GetMapping("/dashboard")
  public Map<String, Object> dashboard() {
    return service.dashboard();
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, String> bad(IllegalArgumentException ex) {
    return Map.of("error", ex.getMessage());
  }
}
