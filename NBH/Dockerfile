# Use Eclipse Temurin 25 JRE (JDK 25 LTS in production scenarios)
FROM eclipse-temurin:25-jre-alpine

# Working directory for the app
WORKDIR /app

# Copy the Spring Boot fat JAR (adjust name to match your JAR)
COPY target/NBH-0.0.1-SNAPSHOT.jar nbhapp.jar

# Expose the default Spring Boot HTTP port
EXPOSE 8080

# Run the app; adjust memory if needed
ENTRYPOINT ["java", "-jar", "nbhapp.jar"]

