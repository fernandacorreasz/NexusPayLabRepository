package com.nexuspay.api.domain;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TransactionEntry(long id, long accountId, String type, BigDecimal amount, String description,
        String counterparty, String status, OffsetDateTime createdAt) {
}
