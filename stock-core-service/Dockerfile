# Stage 1: Build JAR using Maven with Eclipse Temurin JDK 17
FROM maven:3.9.8-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom.xml first to take advantage of Docker layer caching
COPY pom.xml .

# Download dependencies offline
RUN mvn -B -e -C dependency:go-offline

# Copy source code
COPY src/ ./src

# Build the executable JAR without running tests
RUN mvn clean package -DskipTests

# Find and rename generated JAR to app.jar
RUN cp target/stock-*.jar app.jar

# ------------------------------------------------------------
# Stage 2: Runtime image using lightweight JRE 17
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy built JAR from builder
COPY --from=builder /app/app.jar /app/app.jar

# Run as non-root user for security
RUN groupadd -r appgroup && useradd -r -g appgroup -d /app -s /sbin/nologin appuser && \
    chown -R appuser:appgroup /app

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
