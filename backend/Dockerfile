# Étape 1 : Build du projet avec Maven (production et dev)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# 1. Copie uniquement le pom.xml pour télécharger les dépendances
COPY pom.xml .

# 2. Pré-télécharge les dépendances Maven
RUN mvn dependency:go-offline -B

# 3. Copie le reste du code
COPY src ./src

# 4. Build de l'application
RUN mvn clean package -DskipTests


# Étape 2 : Image finale pour exécution (production)
FROM eclipse-temurin:21-jre-jammy AS production
WORKDIR /app

# Copie du .jar depuis le build
COPY --from=build /app/target/actionelle-0.0.1-SNAPSHOT.jar app.jar

# Configuration des variables d'environnement
ENV SEEDING=false
ENV APP_SERVER_PORT=9093

EXPOSE ${APP_SERVER_PORT}

# Commande de démarrage de l'application
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${APP_SERVER_PORT} --seeding=${SEEDING}"]


# Étape 3 : Image développement (avec hot reload)
FROM maven:3.9.6-eclipse-temurin-21 AS development
WORKDIR /app

# Définir le port d'exposition
ENV APP_SERVER_PORT=9093

EXPOSE ${APP_SERVER_PORT}

# Démarrage avec spring-boot:run (hot reload)
CMD ["sh", "-c", "mvn spring-boot:run -Dspring-boot.run.arguments=\"--server.port=${APP_SERVER_PORT} --seeds=${SEEDING:-false}\""]
