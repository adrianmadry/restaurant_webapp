# Etap 1: Budowanie aplikacji z Maven
FROM maven:3.8-openjdk-11 AS builder

WORKDIR /app

# Kopiowanie pliku pom.xml i pobieranie zależności
COPY pom.xml .
RUN mvn dependency:go-offline

# Kopiowanie kodu źródłowego i budowanie aplikacji
COPY src ./src
RUN mvn clean package -DskipTests

# Etap 2: Uruchomienie aplikacji na Tomcat
FROM tomcat:9-jdk11

# Usunięcie domyślnych aplikacji Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Kopiowanie zbudowanego pliku WAR z etapu buildowania
COPY --from=builder /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Ekspozycja portu
EXPOSE 8080

# Uruchomienie Tomcat
CMD ["catalina.sh", "run"]