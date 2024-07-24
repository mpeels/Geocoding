FROM openjdk:21-jdk-slim

WORKDIR /geocoder

COPY /build/libs/Geocoder-1.0.0-DEMO.jar /geocoder/Geocoder.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "Geocoder.jar"]
