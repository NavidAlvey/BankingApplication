# SpringBoot FinTech Banking w/ Feature Services

## Tech Stack
- Java 
- SpringBoot
- Maven
- ngrok
- Postman
- cURL
- Postgres
- pgAdmin4
- Swagger

## Application Overview
**Account Creation & Verification**
- Authorization & Authentication
- User Account Login
- OTP Verification

**Email Services**
- Email Notificaitons w/ Each Type of Transaction
- Transaction History Statement Report
- OTP Code Generation

**Current Money Operations**
- Debit
- Credit
- Transfers

**Documentation**
- Swagger w/ Swagger UI

***//Future Feature Operations//***
- Credit Points
- Points Shop
- Credit Score

## Start Application Services
1. Initialize **Postgres** database locally with: `psql -h localhost -U postgres -d the_alvey_bank`
2. Start up **pgAdmin4** for database GUI & select databse name `the_alvey_bank`
    - Select "Query Tool" on top left near Object Explorer or try shortcut: `Option + Shift + Q`
    - `SELECT * FROM users;` to display all data within database
3. Compile & Run application through Maven Run Command: `spring-boot:run`
    - Go to Maven tab on explorer panel > right click > Run Maven Commands... > Custom > `spring-boot:run`
    - Any complications during compiling or run, view error codes with: `spring-boot:run -X`
    - Perform conflict/resolution for any errors and try step 3 again

## API Testing w/ Postman
1. For API testing locally: initialize `ngrok` instance locally with: `ngrok http 8080`
 - Otherwise use Postman's desktop application and use the URL `http://localhost:8080` followed by whichever ending tags ie: `/api/user/...`
2. Open <a href="https://www.postman.com/" target="_blank">`Postman`</a> in browser to test & run APIs to application
    - Using Postman requires an account, so either Sign Up or Login
    - Navigate to **Workspaces > Your Workspace > Collections**
    - Click the " + " icon and open **Blank collection**
    - Right click your new collection & **Add request**
    - Select what kind of API call you want to do ie: `POST, GET, DELETE, UPDATE, etc...`
3. Copy & paste **ngrok URL** into **Postman** URL field and ensure to update end tag to corresponding request ie: `/api/user/debit, credit, balance, etc...`
    - In **Headers** tab, make sure the following inputs are present: 
        - **Key**: Content-type, **Value**: application/json
        - **Key**: Authorization, **Value**: Bearer "token" *(Only availabe after a user's account email has passed through login API request)*
        - *Any other Key/Value tags already present are fine as is*
    - Select `Body > raw` and input `JSON` formatted fields to test API
    - View results in "Responses" tab below
4. After API call is made, **Postgres** database in **pgAdmin4** will update: **refresh** database and view changes
5. After any form of transaction is made (ie. CREDIT, DEBIT, TRANSFER) view transaction history records in the transaction table

## API Testing w/ cURL
- In some cases, Postman API testing can run into unforseen errors even when program logic is valid. Ensure these problems don't stop API testing with **cURL!**
- **cURL** lets you make HTTP requests directly from your terminal without needing a separate application like Postman. Below is an example of a HTTP request using cURL:
 ```
 curl -X POST http://localhost:8080/api/otp/sendOtp \
 -H "Content-Type: application/json" \
 -d "{\"email\": \theperfecthuman@gmail.com\"}"
 ```
- `http://localhost:8080` *can also be replaced by the **ngrok** URL*
- This cURL HTTP request will execute the sendOtp API and send an OTP code to the email "theperfecthuman@gmail.com"
- Using different headers (-H) and payload data (-d) among other commands in cURL allows for any form of testing for this application
- For **debugging** API requests with cURL the following commands directly following `curl` are helpful:
    - `-v` **Verbose**: shows ALL request/response details
    - `-vvv` Even more verbose
    - `-I` **Inspect**: shows only response headers
    - `-i` Shows headers with response body

## Documentation w/ Swagger
1. In new window tab, enter URL: `localhost:8080/swagger-ui/index.html#/`

## JSON Structure of Each API Call
**Authorization**
- After account creation, send a login request using account's `email` & `password`
- Upon successful account login, an encrypted authorization token will be provided under `responseMessage`
- In the `Headers` tab of each request, enter the Key/Value pair of "Authorization"/"Bearer (token)"

**Create Account** - `POST`
```{
    "firstName": "string",
    "lastName": "string",
    "otherName": "string",
    "gender": "string",
    "address": "string",
    "stateOfOrigin": "string",
    "email": "string",
    "phoneNumber": "string",
    "password": "string",
    "alternativePhoneNumber": "string",
 }
 ```

**Login** - `POST`
```{
    "email": "string",
    "password": "string"
}
```

**Balance Inquiry** - `GET`
```
{
    "accountNumber": "string"
}
```

**Name Inquiry** - `GET`
```
{
    "accountNumber": "string"
}
```

**Credit Account** - `POST`
```
{
    "accountNumber": "string",
    "amount": 0
}
```

**Debit Account** - `POST`
```
{
    "accountNumber": "string",
    "amount": 0
}
```

**Transfer Debit to Credit** - `POST`
```
{
    "sourceAccountNumber": "string",
    "destinationAccountNumber": "string",
    "amount": 0
}
```
**Bank Statement** - `GET`
1. Use ending tag /bankStatement on URL
2. Fill out associated Key/Value pairs using data from transaction table in Params tab
3. Keys: accountNumber, startDate, endDate
Example Results:
```
[
    {
        "transactionID": "00f37da2-30a9-43da-bd6e-99375a418525",
        "transactionType": "TRANSFER",
        "amount": 100.00,
        "accountNumber": "052225992067",
        "status": "SUCCESS",
        "createdAt": "2025-05-23",
        "modifiedAt": "2025-05-23"
    }
]
```
4. Once Bank Statement request is sent, document will generate called "MyStatements.pdf" containing statement account transactions
5. A document will be emailed to the email associated with the accountNumber used in API request

**The following OTP requests try testing with cURL if Postman is showing errors**

**Send OTP** - `POST`
```
{
    "email": "string"
}
```

**Validate OTP** - `POST`
```
{
    "email": "string",
    "otp": "string"
}