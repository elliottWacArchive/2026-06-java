#!/usr/bin/env bash
# (mvn test)
set -e
cd "$(dirname "$0")/.."

docker run --rm \
    -v "$PWD":/app \
    -v my_restaurant_m2:/root/.m2 \
    -w /app \
    maven:3.9-eclipse-temurin-21 \
    mvn test -B
