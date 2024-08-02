# WowProgressDataCrawler

**This project is an open-source service designed for use by [Azerite.app](https://azerite.app).**

## Introduction

WowProgressDataCrawler is a web application built using Java 17, Jakarta EE, Spring Data JPA, Spring MVC, Lombok, and Jsoup. The application is designed to scrape character data from wowprogress.com, parse it, and update a list that can be reused.

## Key Features

- Extract character data from WowProgress.com
- Parsing the extracted webpage to obtain character details such as name, guild, realm, and character score.
- Logging the obtained information
- Transforming the parsed data into a list of `CharacterInfo` objects
- Keeping the data on an up-to-date list and tracking changes
- Logging of HTTP requests and responses

## Technologies Used

- [Java 17](https://openjdk.java.net/projects/jdk/17/)
- [Jakarta EE with jakarta imports](https://jakarta.ee/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring MVC](https://spring.io/projects/spring-mvc)
- [Lombok](https://projectlombok.org/)
- [Jsoup](https://jsoup.org/)

## Security

To protect the data and maintain the integrity of the service, the WowProgressDataCrawler incorporates a robust security structure. 

Before any request is processed, the service verifies the client's authentication by applying the HTTP Basic authentication scheme. Incoming requests must carry valid credentials in the Authorization header of the HTTP request. Any request that doesn't pass this authentication check is immediately rejected, ensuring that only authorized clients can access the controller services.

Additionally, input validation is performed on receiving each request, which ensures that the received inputs do not carry any malicious intent that might harm the service or compromise the data. By validating inputs, the service shields itself against several common forms of attacks, including SQL injection and XSS attacks. 

In situations where sensitive data is stored, advanced encryption schemes are applied to the data before storing them in the databases. This practice further secures the data and prevents it from being accessed or understood even when the data or the database gets compromised in any way.

## How to Run

1. Clone this repository
2. Navigate to the project directory
3. Run `mvn spring-boot:run` to start the application

## Making a `latest-lfg` Request

Once your server is up and running, a `latest-lfg` request can be made from your command line using `cURL` or from a REST Client like Postman.

Here's an example of the command you'll run on your terminal to simulate the request:

```
curl -X GET http://localhost:8080/api/v1/wdc/latest-lfg
```

This command fires a `GET` request to your server running locally at port 8080, and specifically to the endpoint `/api/v1/wdc/latest-lfg`. This `latest-lfg` request returns a list of the latest Looking For Group (LFG) postings.

Note: Ensure to replace `http://localhost:8080` with your server's appropriate address and port if it is different.

## Future Plans

We are continuously working to add more features, enhance the existing ones, and rectify any bugs. 
