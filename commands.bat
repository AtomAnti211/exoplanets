mvn package
docker build . -t exoplanets
docker run -p 8080:8080 exoplanets
