# MyBank Account Handling Services
A project with a microservice architecture for working with bank multi-currency accounts

- [Stack of technologies](#stack-of-technologies)
- [Architecture](#architecture)
- [Running a project on a local machine](#running-a-project-on-a-local-machine)
- [API & Endpoints](#api--endpoints)


## Stack of technologies
- Java 21
- Spring Boot 3
- PostgreSQL
- Liquibase
- OpenAPI Generator
- Docker
- WireMock (for testing)

Instructions on how to run this project are provided below.

## Architecture
**MyBank** software consist of 3 microservices: **MyBankAPI**, **AccountService**, and **TransactionService**.

(schema)

**MyBankAPI** is a public API communicating with frontend. This service validates frontend requests, handles them, and sends them to internal microservices, which returns a response. **MyBankAPI** does not have a database connection.

**AccountService** is an internal microservice and has no direct communication with the frontend. This service returns a list of bank accounts and related balances in the specific currency. This service has a database connection.

**TransactionService** is an internal microservice and has no direct communication with the frontend. This service makes debit of money from the account, deposit of money to the account, and also deals with online payments. This service has a database connection.

**AccountService** and **TransactionService** do not communicate with each other. They only send an intermediate response to **MyBankAPI**.

## Running a project on a local machine
Clone the following projects to your local machine:
- [mybank-api](https://github.com/nikopa96/mybank-api)
- [account-service](https://github.com/nikopa96/account-service)
- [transaction-service](https://github.com/nikopa96/transaction-service)
- [deployment-infra](https://github.com/nikopa96/deployment-infra)

### Running using Docker
Go to the deployment-infra project and run ```docker-compose up --build```. MyBankAPI will be run on port 8080. Open the page in your browser: "localhost:8080/swagger-ui/index.html". On this page you can find all features of the project.

### Running using your IDE
In each project run ```mvn clean package```. After open all projects in your IDE and run them. 

**IMPORTANT!** This project uses Lombok, so "Enable annotation processing" must be enabled. In the IntelliJ Annotation Processors menu "Obtain processors from project classpath" must be checked for each microservice.

This project uses OpenAPI Generator to generate DTO and Controller classes. If they are highlighted in "red" in your code, then go to the target/generated-sources folder and mark this folder as "generated source". 

The project structure must look like this: 

(screenshot)

## API & Endpoints
**IMPORTANT!** You can find IBAN and holder names in the **"bank_account"** database table. The amount of money in a specific currency can be found in the **"bank_account_balance"** table. **You can use this database data to test the following endpoints**. These data are also shown in the  **src/main/resources/import-sample-data.yaml** file.

### Get Account Balance with a currency exchange
```POST /accounts/balances/total```

Returns balances in all currencies of the client's multi-currency bank account. Has a feature for exchanging currency balances using fixed conversion rates

Example of request:
```json
{
    "iban": "EE251298943438713614",
    "currencyCode": "USD"
}
```
```iban``` is a search criterion to find the account balance.
```currencyCode``` is the currency into which the money will be converted

Both parameters are required.

### Add Money to Account
```POST /transactions/deposit```

Depositing money into an account in a specific currency. If the client does not have a balance in the requested currency, the money will not be added and the backend will return 422 Unprocessable Entity with the corresponding response body.

Example of request:
```json
{
    "iban": "EE251298943438713614",
    "holderName": "ADAM SMITH",
    "amount": 123.45,
    "currencyCode": "USD"
}
```
All parameters in the request are required. Otherwise, the API returns 400 Bad Request. If the client with the same ```iban``` and ```holderName``` does not exist, then the API returns 422 Unprocessable Entity with the corresponding response body.

### Debit Money from Account
```POST /transactions/debit```

Debiting money from an account in a specific currency. If the client does not have a balance in the requested currency, the money will not be debited and the backend will return 422 Unprocessable Entity with the corresponding response body.

If the client has an account in a specific currency but does not have enough money funds, then a 422 Unprocessable Entity is returned with the corresponding response body.

Example of request:
```json
{
    "iban": "EE251298943438713614",
    "holderName": "ADAM SMITH",
    "amount": 567.89,
    "currencyCode": "EUR"
}
```
All parameters in the request are required. Otherwise, the API returns 400 Bad Request. If the client with the same ```iban``` and ```holderName``` does not exist, then the API returns 422 Unprocessable Entity with the corresponding response body.

### Make online payment
```POST /transactions/onlinepayment```

This endpoint simulates communication with external payment gateway services like Shopify etc. We use https://httpstat.us/ website to simulate callbacks.

If the client does not have a balance in the requested currency, the money will not be debited and the backend will return 422 Unprocessable Entity with the corresponding response body.

If the client has an account in a specific currency but does not have enough money funds, then a 422 Unprocessable Entity is returned with the corresponding response body.

Example of request:
```json
{
  "transactionInfo": {
    "iban": "EE251298943438713614",
    "holderName": "ADAM SMITH",
    "amount": 567.89,
    "currencyCode": "EUR"
  },
  "callbackURL": "https://httpstat.us/200"
}
```
All parameters in the request are required. Otherwise, the API returns 400 Bad Request. If the client with the same ```iban``` and ```holderName``` does not exist, then the API returns 422 Unprocessable Entity with the corresponding response body.

If the external gateway service is temporarily unavailable, then we also return 422 status code with a response.
