FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY . .

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

CMD ["java", "-jar", "-Dspring.profiles.active=prod", "-Dserver.address=0.0.0.0", "target/project-management-0.0.1-SNAPSHOT.jar"]