FROM maven

COPY pom.xml .
COPY src ./src

RUN mvn clean package -Dskiptests

FROM openjdk:17