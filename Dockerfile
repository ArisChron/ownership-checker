# Build stage
FROM maven:3.9-eclipse-temurin-21-alpine AS builder
WORKDIR /app

# Copy pom.xml and download dependencies (cached layer)
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Copy source code and build
COPY src ./src
RUN mvn -B clean package -DskipTests


FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# wget needed for HEALTHCHECK in alpine
RUN apk add --no-cache wget

# non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring


COPY --from=builder /app/target/*.jar app.jar

# Expose port (Render uses $PORT; Spring should read it via server.port=${PORT:8080})
EXPOSE 8080

# Health check (use PORT if set, fallback 8080)
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:${PORT:-8080}/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
