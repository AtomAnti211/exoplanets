FROM openjdk:11-slim
WORKDIR /app
COPY ./target/extrasolar-systems-0.0.1-SNAPSHOT.jar /app/exoplanets.jar
EXPOSE 8080
CMD "java" "-jar" "/app/exoplanets.jar"