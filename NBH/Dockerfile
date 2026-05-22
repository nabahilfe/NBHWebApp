# =========================
# Stage 1: Build the app
# =========================
FROM maven:3.9.9-eclipse-temurin-25-alpine AS builder

WORKDIR /app

# Copy Maven config first
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the Spring Boot JAR
RUN mvn clean package -DskipTests

# =========================
# Stage 2: Runtime image
# =========================
FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

# Copy built JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
