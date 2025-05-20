# Banking Application

## Application Overview
***Account Creation & Verification**
***Current Money Operations**
- Debit
- Credit
- Transfers
***//Future Operations//***
- Credit Points
- Points Shop
- Credit Score

***Documentation**
- Swagger w/ Swagger UI

## Start Application Services
1. Initialize `Postgres` database locally with: `psql -h localhost -U postgres -d the_alvey_bank`
2. Start up `pgAdmin4` for database GUI & select databse name `the_alvey_bank`
    - Select "Query Tool" on top left near Object Explorer or try shortcut: `Option + Shift + Q`
    - `SELECT * FROM users;` to display all data within database
3. Compile & Run application through Maven Run Command: `spring-boot:run`
    - Go to Maven tab on explorer panel > right click > Run Maven Commands... > Custom > `spring-boot:run`
    - Any complications during compiling or run, view error codes with: `spring-boot:run -X`
    - Perform conflict/resolution for any errors and try step 3 again
## API Testing w/ Postman
1. For API testing locally: initialize `ngrok` instance locally with: `ngrok http 8080`
2. Open <a href="https://navidalvey.postman.co/workspace/Navid-Alvey's-Workspace~5700eab0-164a-4a6d-88b7-377e2fb695fc/request/44583731-bcbb1d5f-8393-4bb6-8c1d-1636b4c9fa29" target="_blank">`Postman`</a> in browser to test & run APIs to application
    - Navigate to Workspaces > Navid Alvey's Workspace > Collections > the_alvey_bank > Add Request
    - From dropdown selection, choose corresponding API call for request from `UserController.java` ie: `GET, POST, etc...`
3. Copy & paste `ngrok URL` into `Postman` URL field and ensure to update end tag to corresponding request ie: `/api/user/debit, credit, balance, etc...`
    - Select `Body > raw` and input `JSON` formatted fields to test API
    - View results in "Responses" tab below
4. After API call is made, `Postgres` database in `pgAdmin4` will update: `refresh` database and view changes

## Documentation w/ Swagger
1. In new window tab, enter URL: <a href="localhost:8080/swagger-ui/index.html#/" target="_blank">`localhost:8080/swagger-ui/index.html#/`</a>


## JSON Structure of Each API Call
**Create Account** - `POST`
```{
    "firstName":"string"
    "lastName":"string"
    "otherName":"string"
    "gender":"string"
    "address":"string"
    "stateOfOrigin":"string"
    "email":"string"
    "phoneNumber":"string"
    "alternativePhoneNumber":"string"
 }
 ```

**Balance Inquiry** - `GET`
```
{
    "accountNumber":"string"
}
```

**Name Inquiry** - `GET`
```
{
    "accountNumber":"string"
}
```

**Credit Account** - `POST`
```
{
    "accountNumber":"string"
    "amount":0
}
```

**Debit Account** - `POST`
```
{
    "accountNumber":"string"
    "amount":0
}
```

**Transfer Debit to Credit** - `POST`
```
{
    "sourceAccountNumber":"string"
    "destinationAccountNumber":"string"
    "amount":0
}
```
