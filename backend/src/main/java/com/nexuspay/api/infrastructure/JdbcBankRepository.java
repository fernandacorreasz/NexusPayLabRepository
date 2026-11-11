package com.nexuspay.api.infrastructure;

import com.nexuspay.api.domain.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcBankRepository implements BankRepository {
  private final JdbcTemplate jdbc;
  public JdbcBankRepository(JdbcTemplate jdbc) { this.jdbc = jdbc; }
  private final org.springframework.jdbc.core.RowMapper<Account> accountMapper = (rs,n) -> new Account(rs.getLong("id"),rs.getString("account_number"),rs.getLong("customer_id"),rs.getString("type"),rs.getString("status"),rs.getString("currency"),rs.getBigDecimal("balance"),rs.getBigDecimal("credit_limit"),rs.getString("full_name"));
  private final org.springframework.jdbc.core.RowMapper<TransactionEntry> transactionMapper = (rs,n) -> new TransactionEntry(rs.getLong("id"),rs.getLong("account_id"),rs.getString("type"),rs.getBigDecimal("amount"),rs.getString("description"),rs.getString("counterparty"),rs.getString("status"),rs.getObject("created_at",java.time.OffsetDateTime.class));
  public Optional<Customer> customer(String username) { return jdbc.query("SELECT id,username,full_name,document,birth_date,email,phone,address,status FROM customers WHERE username=?",(rs,n)->new Customer(rs.getLong("id"),rs.getString("username"),rs.getString("full_name"),rs.getString("document"),rs.getString("birth_date"),rs.getString("email"),rs.getString("phone"),rs.getString("address"),rs.getString("status")),username).stream().findFirst(); }
  public List<Account> accountsFor(String username) { return jdbc.query("SELECT a.id,a.account_number,a.customer_id,a.type,a.status,a.currency,a.balance,a.credit_limit,c.full_name FROM accounts a JOIN customers c ON c.id=a.customer_id WHERE c.username=? ORDER BY a.id",accountMapper,username); }
  public Optional<Account> account(long id) { return jdbc.query("SELECT a.id,a.account_number,a.customer_id,a.type,a.status,a.currency,a.balance,a.credit_limit,c.full_name FROM accounts a JOIN customers c ON c.id=a.customer_id WHERE a.id=?",accountMapper,id).stream().findFirst(); }
  public List<Account> searchAccounts(String term) { String sql="SELECT a.id,a.account_number,a.customer_id,a.type,a.status,a.currency,a.balance,a.credit_limit,c.full_name FROM accounts a JOIN customers c ON c.id=a.customer_id WHERE a.account_number LIKE '%"+term+"%' OR c.full_name ILIKE '%"+term+"%' ORDER BY a.id"; return jdbc.query(sql,accountMapper); }
  public List<TransactionEntry> transactions(long accountId) { return jdbc.query("SELECT id,account_id,type,amount,description,counterparty,status,created_at FROM transactions WHERE account_id=? ORDER BY created_at DESC,id DESC",transactionMapper,accountId); }
  public void deposit(long accountId,BigDecimal amount,String description,String counterparty,String requestId) { jdbc.update("UPDATE accounts SET balance=balance+? WHERE id=?",amount,accountId); jdbc.update("INSERT INTO transactions(account_id,type,amount,description,counterparty,request_id) VALUES (?,?,?,?,?,?)",accountId,"CREDIT",amount,description,counterparty,requestId); }
  public void transfer(long sourceId,String targetNumber,BigDecimal amount,String description,String requestId) { jdbc.update("UPDATE accounts SET balance=balance-? WHERE id=?",amount,sourceId); Long target=jdbc.queryForObject("SELECT id FROM accounts WHERE account_number=?",Long.class,targetNumber); jdbc.update("UPDATE accounts SET balance=balance+? WHERE id=?",amount,target); jdbc.update("INSERT INTO transactions(account_id,type,amount,description,counterparty,request_id) VALUES (?,?,?,?,?,?)",sourceId,"DEBIT",amount,description,targetNumber,requestId); jdbc.update("INSERT INTO transactions(account_id,type,amount,description,counterparty,request_id) VALUES (?,?,?,?,?,?)",target,"CREDIT",amount,description,"NexusPay",requestId); }
  public void audit(Long customerId,String action,String details) { jdbc.update("INSERT INTO audit_events(customer_id,action,details) VALUES (?,?,?)",customerId,action,details); }
  public BigDecimal totalBalance() { return jdbc.queryForObject("SELECT COALESCE(SUM(balance),0) FROM accounts",BigDecimal.class); }
}
