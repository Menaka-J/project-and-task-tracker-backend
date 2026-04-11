FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY . .

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

CMD ["java", "-Dserver.port=8080", "-Dserver.address=0.0.0.0", "-Dspring.profiles.active=prod", "-jar", "target/project-management-0.0.1-SNAPSHOT.jar"]