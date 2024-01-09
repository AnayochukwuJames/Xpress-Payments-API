# Xpress Payments API is a Java Spring Boot project API for authentication using JWT.
Build a robust and secure backend application with Java Spring Boot, Spring Security, JWT, RestTemplate, RESTAPI and Postgres Database.
This repo demonstrates how to create an authenticated backend using Spring Security, Spring Data JPA for database access, JWT generation for enhanced security, How to consume external API and harnessing capabilities through HTTP POST requests

# The Technology used in this project are as follows;


Core Java,
Spring Boot,
Spring Security,
JWT,
RestAPI,
RestTemplate,
Spring Data JPA,
Postsgres Database,
Dependency Injection,
Swagger-ui.


# The Dependencies Used are;

Spring Boot Starter Data JPA:
Function: Provides a set of opinionated defaults and configuration for Spring Data JPA.

Spring Boot Starter Security:
Function: Starter for building secure Spring MVC applications.

Spring Boot Starter Validation:
Function: Starter for using JSR 380 Bean Validation with Hibernate Validator.

Spring Boot Starter Web:
Function: Starter for building web applications using Spring MVC.

PostgreSQL Driver:
Function: JDBC driver for connecting to PostgreSQL databases.

Commons Codec:
Function: Provides implementations of common encoders and decoders.

Spring Boot Starter Test:
Function: Starter for testing Spring Boot applications.

Spring Boot Configuration Processor (Optional):
Function: Generates metadata for IDE support.

Spring Security Test:
Function: Starter for testing Spring Security.

JUnit Jupiter API:
Function: The API for writing tests with JUnit Jupiter.

JUnit Jupiter Engine:
Function: The engine for running JUnit Jupiter tests.

SpringDoc OpenAPI Starter WebMVC UI:
Function: Starter for generating OpenAPI documentation for Spring Boot applications.

Mockito Core:
Function: A mocking framework for unit tests in Java.

JJWT API:
Function: A library for JSON Web Token (JWT) support in Java.

JJWT Implementation:
Function: The implementation of the JWT API.

JJWT Jackson:
Function: Jackson integration for the JWT library.

Lombok (Optional):
Function: A library to reduce boilerplate code in Java by providing annotations like @Getter, @Setter, @Builder, etc.


# How To use the Project
1. Crone the Repo
2. Configure your own Postgres database in your laptop. Change the prefied database name and password in your own device (Computer)
3. Run the code in your Device and Test the endpoints

# How to Test the endpoint
# You can Test eirther in Postman or Swagger-ui
To Test in Swagger-ui you can follow the following steps

Naviate to http://localhost:8080/swagger-ui/index.html in your brouser and start.

User Management Endpoint:
1.  User Registration
URL: api/v1/user 
HTTP Method: POST
Description: Allows users to sign up for the Xpress Payment API.
Request:
Method Type: POST
Endpoint: /signup (api/v1/user/sign-up)
Request Body:
Type: SignUpRequest (JSON)
Example:

{

 "first_name": "James",
 
"last_name": "Anayochukwu",

 "password": "securepassword",
 
 "email_address": "john.doe@example.com"
 
}

Response:
Success Response (HTTP 200 OK):

Example:

{
  	
Response body


{
  "responseCode": 200,
  "responseMessage": "user registered successfully"
}


  
Error Response
(HTTP 400 Bad Request):


Example:

{

  "responseCode": 400,
  
  "message": "Invalid registration request",
  
  "errorDetails": "Username already exists"
  
}


2. User Login
URL: /login (api/v1/user/login)
HTTP Method: POST
Description: Allows users to log in to the Xpress Payment API.
Request:
Method Type: POST
Endpoint: api/v1/user/login

Request Body:
Type: LoginRequest (JSON)

Example:
json
Copy code
{
  "username": "john_doe",
  "password": "securepassword"
}
Response:
Success Response (HTTP 200 OK):

Example:
json

{
  "responseCode": 200,
  
  "message": "Login successful",
  
  "data":
  
  {
  "responseCode": 200,
  "responseMessage": "user login successful",
  "payload": {
    "access_token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYW1lc0BnbWFpbC5jb20iLCJpc3MiOiJ4cHJlc3NwYXltZW50cyIsImlhdCI6MTcwNDc5MDU0NywiZXhwIjoxNzA0ODc2OTQ3fQ.hXNGf3ToXGk4r-vqAlDLhLVKm5hIPYmBYp9vKfnl2je6h3N_JKANyGOIMTN1U6jLKfGiwRh7aUETV9mK9jK9Cw",
    "refresh_token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYW1lc0BnbWFpbC5jb20iLCJpc3MiOiJ4cHJlc3NwYXltZW50cyIsImlhdCI6MTcwNDc5MDU0NywiZXhwIjoxNzA1Mzk1MzQ3fQ.NfGiWeUfYpGFmJoY4kmhlmn14SzT3nSkg0BClee08kuy1y-bfXJRwFBm8f3NNOEy9paCaNLDtwlKk3QOnTKb8g"
  }


Error Response (HTTP 400 Bad Request):

Example:
{
  "responseCode": 400,
  
  "message": "Invalid login request",
  
  "errorDetails": "Invalid credentials"
  
}


To purchase Airtime you have to access the endpoint with the Token generated when you login

 **Airtime Purchase**
 
Endpoint:
URL: /purchase
HTTP Method: POST
Description: Allows users to purchase airtime using the Xpress Payment API.
Request:
Method Type: POST

purchase Airtime Endpoint
URL: /api/v1/vtu/airtime/purchase


Request Body:
Type: PurchaseAirtimeRequest (JSON)
Example:
json

{
   "phone_number": "07066929216",
  "amount": 5000,
  "network_provider": "MTN"
  

}
Response:
Success Response (HTTP 200 OK):

Example:
json


{
  
  {
  "responseCode": 200,

  "responseMessage": "airtime purchase successful",
  
  "payload": {
  
    "amount": 5000,
    
    "mobile_number": "07066929216",
    
    "transaction_status": "FAILED",
    
    "created_at": "2024-01-09T17:55:37.24088"
  }
}
   {
  

Error Response

(HTTP 400 Bad Request):

Example:
json


{
  "responseCode": 400,
  
  "message": "Invalid request",
  
  "errorDetails": "Insufficient balance"
}

