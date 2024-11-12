@echo off
echo Running Maven clean install...
REM call mvn clean install -DskipTests
call mvn clean install

if %ERRORLEVEL% NEQ 0 (
    echo Maven build failed!
    exit /b %ERRORLEVEL%
)

echo Starting Spring Boot application...
REM call mvn spring-boot:run 
call mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xmx512m" 