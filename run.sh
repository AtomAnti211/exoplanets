#!/bin/bash

docker run --name exoplanets -it --rm -p 8080:8080 exoplanets java -jar exoplanets.jar exoplanets