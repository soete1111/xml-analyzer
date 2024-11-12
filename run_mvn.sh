#!/bin/bash
echo "Running Maven clean install..."
mvn clean install

if [ $? -ne 0 ]; then
    echo "Maven build failed!"
    exit 1
fi

echo "Starting Spring Boot application..."
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xmx512m" 