# userRegistration-with-Email-verification
This repository contains a Spring Boot application that implements a user registration system. Users can sign up using POST requests via POSTMAN, 
and a verification token will be sent to their email address to complete the registration process.
Prerequisites
Before running the application, make sure you have the following installed:

Java Development Kit (JDK) 8 or higher
Apache Maven
MySQL or any other compatible database

Setup
Clone the repository:
git clone https://Popsyynn/userRegistration-with-Email-verification.git

Navigate to the project directory:
cd userRegistration-with-Email-verification

Configure the application.properties file:

Open the src/main/resources/application.properties file and update the database configuration with your MySQL credentials:

Build the application:
mvn clean install

Run the application
mvn spring-boot:run

Access the application:

Open POSTMAN and start using the user registration API endpoints.
Register User:

Endpoint: POST http://localhost:8085/register

User Registration:

Send a POST request to http://localhost:8085/register using POSTMAN with the email, firstName,lastName, password ,role in the request body.
The server will send a verification email to the provided email address.

Email Verification:

Access the verification link sent to the email address. You can use the link in a web browser or use POSTMAN to send a GET request to http://localhost:8085/verify/{token}, where {token} is the verification token received in the email.
If the token is valid, the email will be verified, and you will receive a success message.

Technologies Used
Spring Boot
Spring Data JPA
Java Mail API (for email verification)
Bcrypt (for password hashing)
MySQL (or any compatible database)



