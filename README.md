# Subject Voting Service

This service intends to provide a way for Sicredi associates voting on subjects through REST API based solution. 

### Application Requirements

In order to run this application, make sure the following dependencies are configured in your local environment:

* [Java Development Toolkit 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
* [MongoDB](https://www.mongodb.com/download-center#community)

 In case of running the application from IDE, do not forget to enable annotation processing for lombok and set active profile to **local**
 
 ```
 spring.profiles.active=local
 ```

##### Important: 

* It's mandatory to have MongoDB running at default port 27017 in your local environment.

### Application Documentation

* [Swagger](http://localhost:8081/swagger-ui/index.html#) [Only when application is running]

### Running the application locally (Windows OS)

Clone github repository:

```
git clone https://github.com/mourabd/voting.git
```

Run the following commands from root application folder: 
```
gradlew build bootRun
```

The application should be running at port [8081](http://localhost:8081/).

### Running the application on docker container (docker-compose)

Clone github repository:

```
git clone https://github.com/mourabd/voting.git
```

Run the following commands from root application folder: 
```
gradlew clean build
docker-compose build
docker-compose up
```

The application should be running at port [8081](http://localhost:8081/).

### Technical Decisions:

* **Programming language:** Java 11 + Spring Webflux
* **Build automation tool:** [Gradle](https://docs.gradle.org)
* **Tests (unit and integration):** [JUnit](https://junit.org/) and [mockito](https://site.mockito.org/)

### Implementation details:

For implementation details please refer to this [file](implementation_details.md) [pt-BR]