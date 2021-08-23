mvn package
docker build . -t simple -f Dockerfile2.dockerfile
docker run -p 8080:8080 simple
