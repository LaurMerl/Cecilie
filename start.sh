#!/usr/bin/env bash

sbt docker
docker run -v ~/Desktop/:/app/stuff --network cecilie webcrawler/webcrawler
