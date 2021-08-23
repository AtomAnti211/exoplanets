mvn package
docker build . -t simple -f
docker run -p 8080:8080 simple
