# WowProgressDataCrawler

## Description

`WowProgressDataCrawler` is a simple web scraper using [Jsoup](https://jsoup.org/) to fetch and parse character data from the World of Warcraft rankings on wowprogress.com.

## Features

* Fetch character data from WowProgress.com
* Parse the webpage to extract character details like name, guild, realm, character score
* Log the parsed information
* Return a list of `CharacterInfo` objects containing the parsed information

## Technologies Used

* [Java 17](https://openjdk.java.net/projects/jdk/17/)
* [Spring Boot](https://spring.io/projects/spring-boot)
* [Jsoup](https://jsoup.org/)
* [Lombok](https://projectlombok.org/)

## How to Run

1. Clone this repository
2. Navigate to the project folder
3. Run `mvn spring-boot:run` to start the application

## To Do

1. Complete the `CharacterInfo` class to fully reflect the character data.
2. Add a persistence layer (like a database) to save the scraped character data.
