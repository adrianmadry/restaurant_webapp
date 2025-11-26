# Stage 1: Build the application
FROM maven:3.8-openjdk-11 AS builder

WORKDIR /app

# Copy pom.xml and download dependencies (cached layer)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests && ls -la target

# Stage 2: Runtime environment
FROM tomcat:9-jdk11

# Remove default Tomcat webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy the WAR file from build stage
COPY --from=builder /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Expose Tomcat port
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]