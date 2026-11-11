package com.nexuspay.api.domain;
import java.math.BigDecimal;
public record Account(long id,String accountNumber,long customerId,String type,String status,String currency,BigDecimal balance,BigDecimal creditLimit,String ownerName) {}
