FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY target/actionelle-0.0.1-SNAPSHOT.jar app.jar

# Variable d'environnement pour contrôler l'exécution des seeds
ENV SEEDING=false

EXPOSE ${APP_SERVER_PORT}

ENTRYPOINT ["sh", "-c", "java -jar app.jar --seeds=$SEEDING"]
