# NexusPay

Essa aplicação foi desenvolvido de forma didática para praticas de segurança.
Teste educacional

Tecnologias usadas: Spring Boot, PostgreSQL, Flyway, React e um firmware em contêiner separado. 

NexusPay é uma plataforma de conta digital com gestão de clientes, contas, saldos, extratos, depósitos e transferências. O projeto utiliza Spring Boot, JDBC, PostgreSQL, Flyway, React, Vite, Nginx e Docker Compose.

## Executar

Na raiz do repositório:

```bash
docker compose up --build
```

A aplicação web estará em `http://localhost:3000`.

A API estará em `http://localhost:8080`.

A documentação Swagger estará em `http://localhost:8080/swagger-ui/index.html` ou `http://localhost:3000/swagger-ui/index.html`.

Para interromper os serviços:

```bash
docker compose down
```

Para recriar o banco a partir das migrations:

```bash
docker compose down -v
docker compose up --build
```

## Acessos

| Usuário | Senha | Conta |
|---|---|---|
| alice | alice123 | 0001-01 |
| bruno | bruno123 | 0002-01 |
| carla | carla123 | 0003-01 |
| operator | operator123 | Operações internas |

A autenticação da API usa Basic Auth. A aplicação web permite alternar entre os clientes pelo seletor no menu lateral.

## Banco de dados no DBeaver

O PostgreSQL é publicado somente no loopback local.

| Campo | Valor |
|---|---|
| Driver | PostgreSQL |
| Host | `127.0.0.1` |
| Porta | `5432` |
| Database | `nexuspay` |
| Usuário | `postgres` |
| Senha | `nexuspay_postgres` |
| SSL | `disable` |

O usuário operacional criado pelas migrations é `nexuspay_app`, com senha `nexuspay_app`. As migrations são executadas com a conta administrativa do banco. O usuário operacional é separado da conta utilizada no startup do backend.

## Rotas da API

### Clientes e contas

```text
GET /api/customers/me
GET /api/accounts
GET /api/accounts/{id}
GET /api/accounts/search?term=0001
GET /api/accounts/{id}/transactions
GET /api/dashboard
```

### Operações

```text
POST /api/accounts/{id}/deposit
POST /api/transfers
```

Exemplo de depósito:

```json
{
  "amount": 250.00,
  "description": "Crédito recebido",
  "counterparty": "Empresa Nexus"
}
```

Exemplo de transferência:

```json
{
  "sourceAccountId": 1,
  "targetAccountNumber": "0002-01",
  "amount": 75.90,
  "description": "Pagamento mensal"
}
```

Uma operação pode receber o cabeçalho `X-Request-Id` para identificação da solicitação.

## Estrutura

```text
backend/          API Spring Boot, JDBC e migrations Flyway
frontend/         Interface React/Vite servida por Nginx
notifications/    Serviço HTTP de notificações
docker-compose.yml
```

As migrations criam clientes, contas correntes, poupança, cartões, movimentações e registros de auditoria.
