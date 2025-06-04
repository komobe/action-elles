# Backend Action'Elles

API REST développée avec Spring Boot et une architecture hexagonale.

## 🚀 Démarrage Rapide

### Prérequis
- Java 21
- Maven 3.9+
- Docker (optionnel)

### Installation et Lancement

#### Avec Docker
```bash
docker compose up -d --build
```

#### Sans Docker
```bash
# Compilation
mvn clean package

# Lancement
java -jar target/actionelles-*.jar
```

## 🏗️ Architecture

### Structure Hexagonale
```
src/
├── domain/        # Logique métier
├── application/   # Cas d'utilisation
└── infrastructure/# API REST, DB, sécurité
```

### Stack Technique
- Java 21
- Spring Boot 3.3.6
  - Spring Data (JPA/Hibernate)
  - Spring Security (JWT)
- PostgreSQL 15
- Swagger/OpenAPI

## 🔧 Configuration

### Variables d'Environnement
```env
# Serveur
APP_SERVER_PORT=9090
APP_SERVER_URL=http://localhost

# Base de données
POSTGRES_DB=actionelle
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
POSTGRES_PORT=5432

# Sécurité
JWT_SECRET=votre_cle_secrete
JWT_EXPIRATION=86400000

# Données
SEEDING=false
```

## 📚 Documentation API

- Swagger UI : http://localhost:9090/swagger-ui.html
- OpenAPI : http://localhost:9090/v3/api-docs

## 🧪 Tests

```bash
# Exécuter tous les tests
mvn test

# Tests avec couverture
mvn verify
```

## 🤝 Contribution

1. Fork le projet
2. Créer une branche (`git checkout -b feature/amelioration`)
3. Commit (`git commit -am 'Ajout de fonctionnalité'`)
4. Push (`git push origin feature/amelioration`)
5. Créer une Pull Request

## 📞 Support

- Issues : https://github.com/komobe/action-elles/issues
- Email : komobesokona@gmail.com