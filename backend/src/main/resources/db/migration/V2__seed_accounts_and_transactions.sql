INSERT INTO customers(username,full_name,document,birth_date,email,phone,address) VALUES
 ('alice','Alice Martins','111.222.333-44','1992-04-18','alice@nexuspay.local','+55 11 99999-1001','Av. Paulista, 1000 - São Paulo/SP'),
 ('bruno','Bruno Costa','222.333.444-55','1988-09-03','bruno@nexuspay.local','+55 21 98888-2002','Rua Visconde, 200 - Rio de Janeiro/RJ'),
 ('carla','Carla Nogueira','333.444.555-66','1995-01-27','carla@nexuspay.local','+55 41 97777-3003','Rua das Flores, 300 - Curitiba/PR');

INSERT INTO accounts(account_number,customer_id,type,balance,credit_limit)
SELECT '0001-01',id,'CHECKING',3850.40,1200 FROM customers WHERE username='alice';
INSERT INTO accounts(account_number,customer_id,type,balance,credit_limit)
SELECT '0002-01',id,'CHECKING',2140.75,800 FROM customers WHERE username='bruno';
INSERT INTO accounts(account_number,customer_id,type,balance,credit_limit)
SELECT '0003-01',id,'SAVINGS',9820.00,0 FROM customers WHERE username='carla';

INSERT INTO cards(account_id,masked_number,brand,credit_limit,expires_at)
SELECT id,'**** **** **** 4411','NEXUS',3200,'2029-12-31' FROM accounts WHERE account_number='0001-01';
INSERT INTO cards(account_id,masked_number,brand,credit_limit,expires_at)
SELECT id,'**** **** **** 8822','NEXUS',1800,'2028-10-31' FROM accounts WHERE account_number='0002-01';

INSERT INTO transactions(account_id,type,amount,description,counterparty,request_id)
SELECT id,'CREDIT',4500.00,'Salário mensal','Nexus Tecnologia','seed-alice-salary' FROM accounts WHERE account_number='0001-01';
INSERT INTO transactions(account_id,type,amount,description,counterparty,request_id)
SELECT id,'DEBIT',49.90,'Assinatura de streaming','StreamPlay','seed-alice-stream' FROM accounts WHERE account_number='0001-01';
INSERT INTO transactions(account_id,type,amount,description,counterparty,request_id)
SELECT id,'CREDIT',2800.00,'Pagamento recebido','Mercado Central','seed-bruno-credit' FROM accounts WHERE account_number='0002-01';
INSERT INTO transactions(account_id,type,amount,description,counterparty,request_id)
SELECT id,'DEBIT',120.00,'Transferência enviada','Conta de serviços','seed-bruno-transfer' FROM accounts WHERE account_number='0002-01';

INSERT INTO audit_events(customer_id,action,details)
SELECT id,'PROFILE_VIEW',concat('customer=',username,' document=',document,' address=',address) FROM customers WHERE username IN ('alice','bruno');
