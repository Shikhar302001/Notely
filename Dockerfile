FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/target/Notely-0.0.1-SNAPSHOT.jar app.jar


EXPOSE 10000

ENTRYPOINT ["java", "-jar", "app.jar"]
