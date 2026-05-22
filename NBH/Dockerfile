# Stage 1: Build the application with Maven
FROM eclipse-temurin:25-jdk-alpine AS build

WORKDIR /app

# Copy Maven wrapper and config
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Download dependencies (cached layer)
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copy source code and build
COPY src/ src/
RUN ./mvnw package -DskipTests -B

# Stage 2: Runtime image
FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/NBH-0.0.1-SNAPSHOT.jar nbhapp.jar

# Expose the port (Railway sets PORT dynamically)
EXPOSE 8080

# Run the app on Railway's assigned PORT (defaults to 8080 if not set)
ENTRYPOINT ["java", "-jar", "nbhapp.jar", "--server.port=${PORT:-8080}"]


