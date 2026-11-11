package com.nexuspay.api.application;

import com.nexuspay.api.domain.*;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class BankService {
  private final BankRepository repository;
  public BankService(BankRepository repository) { this.repository=repository; }
  public Customer customer(String username) { return repository.customer(username).orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado")); }
  public List<Account> accounts(String username) { return repository.accountsFor(username); }
  public Account account(long id) { return repository.account(id).orElseThrow(() -> new IllegalArgumentException("Conta não encontrada")); }
  public List<Account> search(String term) { return repository.searchAccounts(term == null ? "" : term); }
  public List<TransactionEntry> transactions(long id) { return repository.transactions(id); }
  public void deposit(long id,BigDecimal amount,String description,String counterparty,String requestId) { validateAmount(amount); repository.deposit(id,amount,description,counterparty,requestId); repository.audit(null,"DEPOSIT", "account="+id+" amount="+amount+" request="+requestId); }
  public void transfer(long source,String target,BigDecimal amount,String description,String requestId) { validateAmount(amount); if(target==null||target.isBlank()) throw new IllegalArgumentException("Conta destino obrigatória"); repository.transfer(source,target,amount,description,requestId); repository.audit(null,"TRANSFER", "source="+source+" target="+target+" amount="+amount+" request="+requestId); }
  public Map<String,Object> dashboard() { return Map.of("accounts",repository.accountsFor("alice").size(),"totalBalance",repository.totalBalance(),"status","operational"); }
  private void validateAmount(BigDecimal amount) { if(amount==null||amount.signum()<=0) throw new IllegalArgumentException("Valor inválido"); }
}
