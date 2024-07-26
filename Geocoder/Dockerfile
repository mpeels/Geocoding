FROM gradle:8.9-jdk21 AS builder

WORKDIR /app

COPY build.gradle settings.gradle ./
COPY src/ ./src/
RUN gradle build --no-daemon -x test

FROM openjdk:21-jdk-slim

WORKDIR /geocoder

COPY --from=builder /app/build/libs/Geocoder-1.0.0-DEMO.jar /geocoder/Geocoder.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/geocoder/Geocoder.jar"]
