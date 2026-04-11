# Use Eclipse Temurin JDK 17 (official OpenJDK distribution)
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the built JAR file (build stage)
COPY target/*.jar app.jar

# Expose the port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]