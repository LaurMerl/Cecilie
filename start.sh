#!/usr/bin/env bash

docker network create cecilie
docker run -d -p 27017:27017 --name mongodb   --network cecilie mongo 
docker run -d -p 8910:8910   --name phantomjs --network cecilie wernight/phantomjs phantomjs --webdriver=8910
docker run -d -it --rm -p 8081:8081 --network cecilie --link mongodb:mongo mongo-express
docker run -v ~/Desktop/:/app/stuff --network cecilie  --link mongodb:mongo  bedux/cecilie
