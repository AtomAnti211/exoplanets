# Exoplanets

It's a Spring Boot (JPA Hibernate) MVC application with 3 entities (star, planet, moon), 
h2 database, CRUD (and some extra) endpoints, some integration and unit test cases.

## Project:

Humanity has currently found about 5000 exoplanets using various detection methods.
This database can be used for the theory of planet formation around stars, 
and other statistical data can be derived.
The James Webb telescope will be launched soon 
and is expected to provide a wealth of new data.


## Running exoplanets locally

### With Maven command line
```
git clone https://github.com/AtomAnti211/exoplanets.git
cd exoplanets
mvn spring-boot:run
```

You can then access exoplanets here:
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
