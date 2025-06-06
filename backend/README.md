# Backend Action'Elle

API REST développée avec Spring Boot et une architecture hexagonale.

## 🛠️ Technologies

- **Framework** : Spring Boot 3.3.6
- **Java** : Java 21
- **Base de données** : PostgreSQL 15
- **ORM** : Spring Data JPA
- **Sécurité** : Spring Security avec JWT
- **Documentation** : SpringDoc (Swagger)
- **Build** : Maven 3.9+
- **Mapping** : MapStruct 1.6.3
- **Utilitaires** : 
  - Lombok 1.18.30
  - Bucket4j 8.14.0 (Rate limiting)
  - iText 5.5.13.3 (Génération PDF)
  - Apache POI 5.2.5 (Génération Excel)
  - ZXing 3.5.2 (Génération QR codes)

## 🚀 Installation

### Option 1 : Installation avec Docker (Recommandée)

#### Prérequis
- Docker Desktop
- Docker Compose(V2)

#### Installation
```bash
# Démarrer le backend avec Docker
docker compose -f backend/compose.yaml up -d

# Voir les logs du service "backend"
docker compose -f backend/compose.yaml logs -f action-elles-api
```

#### Commandes Docker utiles
```bash
# Arrêter le backend
docker compose -f backend/compose.yaml stop

# Redémarrer le backend
docker compose -f backend/compose.yaml restart

# Reconstruire et redémarrer le backend
docker compose -f backend/compose.yaml up -d --build

# Accéder au shell du conteneur nommé "backend"
docker compose -f backend/compose.yaml exec action-elles-api bash

# Voir les logs du service nommé "postgres"
docker compose -f backend/compose.yaml logs -f action-elles-db-postgres
```

### Option 2 : Installation manuelle

#### Prérequis
- Java 21
- Maven 3.9+
- PostgreSQL 15

#### Installation
```bash
# Compilation
mvn clean compile

# Installation des dépendances
mvn clean install

# Démarrage en mode développement
mvn spring-boot:run

# Build pour la production
mvn clean package

# Lancement du JAR
java -jar target/actionelles-*.jar
```

## 🏗️ Architecture

### Structure Hexagonale

L'application suit une architecture hexagonale (ports et adaptateurs) :

```
src/main/java/
├── domain/                  # Cœur métier
│   ├── entities/            # Entités métier
│   ├── repositories/        # Interfaces des repositories
│   ├── services/            # Services métier
│   └── exceptions/          # Exceptions métier
├── application/             # Couche application
│   ├── usecases/            # Cas d'utilisation
│   ├── ports/               # Interfaces des ports
│   └── mappers/             # Mappers DTO/Entity
└── infrastructure/          # Couche infrastructure
    ├── web/                 # Contrôleurs REST
    ├── persistence/         # Implémentation JPA
    ├── security/            # Configuration sécurité
    └── config/              # Configuration Spring
```

### Avantages de cette architecture
- **Séparation des responsabilités** : Logique métier isolée
- **Testabilité** : Facilite les tests unitaires
- **Flexibilité** : Changement facile des adaptateurs
- **Maintenabilité** : Code plus lisible et organisé

## 🔧 Configuration

### Variables d'environnement

Créez un fichier `.env` ou définissez les variables système :

```bash
# Configuration serveur
APP_SERVER_PORT=9093
APP_SERVER_URL=http://localhost

# Configuration Docker
DOCKER_BUILD_TARGET=development  # development, production

# Configuration base de données
POSTGRES_DB=action_elles
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
POSTGRES_HOST=localhost
POSTGRES_PORT=5432

# Configuration sécurité
JWT_SECRET=votre_cle_secrete_tres_longue_et_securisee
JWT_EXPIRATION=86400000

# Configuration application
SPRING_PROFILES_ACTIVE=dev
SEEDING=false
LOGGING_LEVEL=INFO

# Configuration CORS
CORS_ALLOWED_ORIGINS=http://localhost:5173,http://localhost:3000
```

### Configuration par défaut

Les valeurs par défaut sont définies dans `application.properties` :

```bash
# Server
server.port=${APP_SERVER_PORT:9093}

# Datasource
spring.datasource.url=jdbc:postgresql://${POSTGRES_HOST:localhost}:${POSTGRES_PORT:5432}/${POSTGRES_DB:actionelle}
spring.datasource.username=${POSTGRES_USER:postgres}
spring.datasource.password=${POSTGRES_PASSWORD:postgres}

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# JWT
jwt.secret=${JWT_SECRET:default_secret_key}
jwt.expiration=${JWT_EXPIRATION:86400000}
```

## 🔐 Sécurité

### Authentification JWT
- **Génération** : Tokens JWT avec expiration configurable
- **Validation** : Middleware de validation automatique
- **Refresh** : Système de refresh token

### Rate Limiting
- Protection contre les attaques DDoS
- Limitation configurable par endpoint
- Utilisation de Bucket4j

## 📚 Documentation API

### Swagger UI
Accédez à la documentation interactive :
- **URL** : http://localhost:9093/swagger-ui.html
- **OpenAPI** : http://localhost:9093/v3/api-docs


## 🧪 Tests

### Exécution des tests

#### Avec Docker
```bash
# Tests unitaires
docker compose -f backend/compose.yaml exec action-elles-api mvn test

# Tests d'intégration
docker compose -f backend/compose.yaml exec action-elles-api mvn verify
```

#### Installation manuelle
```bash
# Tests unitaires
mvn test

# Tests d'intégration
mvn verify

# Tests spécifiques
.mvn test -Dtest=UserServiceTest
```

### Structure des tests
```
src/test/java/
├── unit/                # Tests unitaires
│   ├── domain/          # Tests du domaine
│   └── application/     # Tests des cas d'usage
├── integration/         # Tests d'intégration
│   ├── web/             # Tests des contrôleurs
│   └── persistence/     # Tests des repositories
└── ...
```

## 🏗️ Scripts Maven disponibles

```bash
# Développement
mvn spring-boot:run              # Démarrer l'application
mvn spring-boot:run -Dspring.profiles.active=dev

# Build
mvn clean compile                # Compilation
mvn clean package                # Build JAR
mvn clean install                # Install dans repo local
```

## 📦 Déploiement

### Build Docker manuel

```bash
# Build de l'image Docker avec un tag clair
docker build -t action-elles-backend .

# Exécution du conteneur avec publication du port et variables d'environnement
docker run -p 9093:9093 \
  -e POSTGRES_HOST=host.docker.internal \
  -e POSTGRES_DB=actionelle \
  -e JWT_SECRET=your_secret_key \
  --name action-elles-api \
  action-elles-backend

```

### Profils Spring

#### Profil développement
```bash
mvn spring-boot:run -Dspring.profiles.active=dev
```

#### Profil production
```bash
java -jar target/actionelles-*.jar --spring.profiles.active=prod
```

## Logging
```bash
# Voir les logs en temps réel du service 'backend' avec Docker Compose
docker compose logs -f action-elles-api

# Exemple : définir le niveau de log via une variable d'environnement dans Docker Compose ou Docker Run
# Par exemple, dans docker-compose.yaml ou via docker run -e LOGGING_LEVEL=DEBUG
LOGGING_LEVEL=DEBUG
```

## 🚀 Fonctionnalités

### Génération de documents
- **PDF** : Génération de rapports avec iText
- **Excel** : Export de données avec Apache POI
- **QR Codes** : Génération avec ZXing

### API Features
- **Pagination** : Support natif avec Spring Data
- **Validation** : Validation automatique des DTOs
- **Mapping** : Conversion automatique avec MapStruct

## 🐛 Résolution de problèmes

### Problèmes fréquents

#### Erreur de connexion base de données
```bash
# Vérifier que PostgreSQL est bien démarré et voir ses logs
docker compose logs -f action-elles-db-postgres

# Tester la connexion à la base PostgreSQL (depuis ta machine locale)
psql -h localhost -p 5432 -U postgres -d action_elles
```

#### Erreur de port
```bash
# Modifier le port de l'application via la variable d'environnement
APP_SERVER_PORT=8080

# Ou directement dans le fichier application.properties
server.port=8080
```

#### Problèmes de mémoire
```bash
# Augmenter la mémoire allouée à la JVM
export JAVA_OPTS="-Xmx512m -Xms256m"

# Puis lancer l'application avec Maven
mvn spring-boot:run
```

## 🤝 Contribution

### Standards de code
- Respectez les conventions Java
- Utilisez Lombok pour réduire le boilerplate
- Documentez les APIs avec Swagger
- Écrivez des tests pour chaque fonctionnalité

### Architecture
- Respectez l'architecture hexagonale
- Séparez la logique métier de l'infrastructure
- Utilisez les patterns appropriés (Repository, Service)
- Évitez les dépendances circulaires

### Workflow
1. Créer une branche feature
2. Implémenter en suivant l'architecture
3. Ajouter des tests appropriés
4. Vérifier la couverture de code
5. Créer une Pull Request

## 📞 Support

- **Issues GitHub** : https://github.com/komobe/action-elles/issues
- **Email** : komobesokona@gmail.com
- **Documentation API** : http://localhost:9093/swagger-ui.html
