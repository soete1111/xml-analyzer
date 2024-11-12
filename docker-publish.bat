@echo off
set DOCKER_USERNAME=soete
set VERSION=1.0.0

echo Building Docker image...
docker build -t xml-analyzer .

echo Tagging Docker images...
docker tag xml-analyzer %DOCKER_USERNAME%/xml-analyzer:%VERSION%
docker tag xml-analyzer %DOCKER_USERNAME%/xml-analyzer:latest

echo Pushing to Docker Hub...
docker push %DOCKER_USERNAME%/xml-analyzer:%VERSION%
docker push %DOCKER_USERNAME%/xml-analyzer:latest