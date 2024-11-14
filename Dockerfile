# Use an official OpenJDK 21 image as the base
FROM maven:3.8.8-eclipse-temurin-21-alpine AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
COPY --from=build /target/BlogsBackend-0.0.1-SNAPSHOT BlogsBackend.jar

# # Set the working directory in the container
# WORKDIR /app

# # Copy the JAR file generated by Maven to the container
# COPY target/BlogsBackend-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port (default Spring Boot port is 8080)
EXPOSE 8080

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "BlogsBackend.jar"]

