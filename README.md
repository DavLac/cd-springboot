# JDBC training

## Prerequisites

- Install `Postgres` locally
- Be able to request the database (create database, tables...)

## Assignment 1 - Dependency injection and Inversion of Control

- Generate a new Springboot project with Spring Initializr -> version 2.x.x
- Complete exercise of dependency injection in the `assignment/injection/Injection.java#main()` method
- Make the same exercise using Spring context

## Assignment 2 - First controller

- Create a controller to return a String

## Assignment 3 - Implement a CRUD

- Create a Controller, a Service and a Repository to create a CRUD for `Users (id, name, age, height)`
- Don't forget to validate your requests

## Assignment 4 - Swagger

- Add Swagger
- URL: `http://localhost:8080/api/v1/swagger-ui/index.html`

## Assignment 4 - Test your CRUD

- Using unitary tests, integration tests (mocks and in-memory database) and test coverage (Jacoco)

## Assignment 5 - Use profiles

- Add new running configs:
    - Using a VM option: `-Dspring.profiles.active=prod`
    - Using a env var: `spring_profiles_active=dev`
    - Using Maven profile: https://www.baeldung.com/spring-profiles#maven-profile

## Assignment 6 - Call an external API

- Create a new endpoint in User controller
- This endpoint will call an external API `https://swapi.dev/api/people/{peopleId}` and take `(name, age, height)` from
  the response
- Persist a new User with Star Wars API call fields