DO $$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'nexuspay_app') THEN
    CREATE ROLE nexuspay_app LOGIN PASSWORD 'nexuspay_app';
  END IF;
END $$;

CREATE TABLE customers (
  id BIGSERIAL PRIMARY KEY,
  username VARCHAR(60) NOT NULL UNIQUE,
  full_name VARCHAR(160) NOT NULL,
  document VARCHAR(20) NOT NULL UNIQUE,
  birth_date DATE NOT NULL,
  email VARCHAR(160) NOT NULL,
  phone VARCHAR(30) NOT NULL,
  address VARCHAR(240) NOT NULL,
  status VARCHAR(24) NOT NULL DEFAULT 'ACTIVE',
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE accounts (
  id BIGSERIAL PRIMARY KEY,
  account_number VARCHAR(24) NOT NULL UNIQUE,
  customer_id BIGINT NOT NULL REFERENCES customers(id),
  type VARCHAR(24) NOT NULL,
  status VARCHAR(24) NOT NULL DEFAULT 'ACTIVE',
  currency CHAR(3) NOT NULL DEFAULT 'BRL',
  balance NUMERIC(14,2) NOT NULL DEFAULT 0,
  credit_limit NUMERIC(14,2) NOT NULL DEFAULT 0,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE transactions (
  id BIGSERIAL PRIMARY KEY,
  account_id BIGINT NOT NULL REFERENCES accounts(id),
  type VARCHAR(32) NOT NULL,
  amount NUMERIC(14,2) NOT NULL,
  description VARCHAR(240) NOT NULL,
  counterparty VARCHAR(160),
  request_id VARCHAR(120),
  status VARCHAR(24) NOT NULL DEFAULT 'CONFIRMED',
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE cards (
  id BIGSERIAL PRIMARY KEY,
  account_id BIGINT NOT NULL REFERENCES accounts(id),
  masked_number VARCHAR(24) NOT NULL,
  brand VARCHAR(24) NOT NULL,
  status VARCHAR(24) NOT NULL DEFAULT 'ACTIVE',
  credit_limit NUMERIC(14,2) NOT NULL DEFAULT 0,
  expires_at DATE NOT NULL
);

CREATE TABLE audit_events (
  id BIGSERIAL PRIMARY KEY,
  customer_id BIGINT REFERENCES customers(id),
  action VARCHAR(80) NOT NULL,
  details TEXT NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_accounts_customer ON accounts(customer_id);
CREATE INDEX idx_transactions_account ON transactions(account_id, created_at DESC);
