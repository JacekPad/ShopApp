FROM maven:3.6.3-openjdk-17 AS build
COPY src /home/app/src
#looking for swagger yaml in root folder
COPY src/main/resources/swagger /src/main/resources/swagger
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:17-jdk-alpine
ARG JAR_FILE
COPY --from=build /home/app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]