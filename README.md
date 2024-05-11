# WowProgressDataCrawler

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

## How to Run

1. Clone this repository
2. Navigate to the project directory
3. Run `mvn spring-boot:run` to start the application

## Future Plans

We are continuously working to add more features, enhance the existing ones, and rectify any bugs. 
