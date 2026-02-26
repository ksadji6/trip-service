# Étape 1 : Build avec Maven et Java 21
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Installation Maven
RUN apk add --no-cache maven

# Cache des dépendances
COPY pom.xml .
RUN mvn dependency:go-offline

# Build du JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Étape 2 : Image d'exécution
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copie du JAR
COPY --from=build /app/target/trip-service-*.jar app.jar

# Port 8082 pour le Trip-Service
EXPOSE 8082

# Optimisation mémoire
ENTRYPOINT ["java", "-Xmx512m", "-jar", "app.jar"]