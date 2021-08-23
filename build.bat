@echo OFF
CD c:\Users\Anti\exoplanets\
mvn clean package -DskipTests
docker build -t medical .
pause