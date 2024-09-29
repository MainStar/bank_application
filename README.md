# Getting Started

## To run the application you will need:
### Java 17 and maven

### You can use this command to run the application: `mvn spring-boot:run`
### To generate report you can use this command: mvn clean verify

# Summary of Endpoints:
### POST /api/accounts - Create account
### GET /api/accounts/{id} - Get account details
### GET /api/accounts - List all accounts
### POST /api/transaction/{id}/deposit - Deposit funds
### POST /api/transaction/{id}/withdraw - Withdraw funds
### POST /api/transaction/transfer - Transfer funds between accounts
