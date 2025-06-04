# Action'Elles - Backend

## Spécifications Techniques

### ⚙️ Architecture Hexagonale (Ports & Adapters)
- `domain/` : Logique métier (entités, services, interfaces)
- `application/` : Cas d’utilisation (orchestration des règles métier)
- `infrastructure/` : Couche technique (API REST, base de données, sécurité, config)

### Technologies
- **Backend**
  - Java 21
  - Spring Boot 3.3.6
    - Spring Data (base de données)
    - Spring Security (sécurité)
  - Maven 3.9+

- **Base de données**
  - PostgreSQL 15
  - JPA/Hibernate (ORM)


#### 📦 Infrastructure
- Docker & Docker Compose
  - Application Spring Boot
  - PostgreSQL
- JWT pour l’authentification
- Swagger / OpenAPI pour la documentation

---

### 🔐 Fonctionnalités Techniques

| Catégorie     | Détails                                |
|---------------|----------------------------------------|
| Sécurité      | Authentification JWT, contrôle d’accès |
| Validation    | Données via annotations Spring         |
| Qualité       | Tests automatisés avec JUnit 5         |
| Documentation | Swagger UI et OpenAPI                  |


## 🚀 Installation & Lancement

### ✅ Prérequis
- [Java 21](https://adoptium.net/)
- [Docker](https://www.docker.com/)
- [Maven 3.9+](https://maven.apache.org/)

### ⚙️ Configuration

Créez un fichier `.env` à la racine :

```env
APP_SERVER_URL=http://localhost
APP_SERVER_PORT=9090

POSTGRES_SERVER=postgres
POSTGRES_DB=actionelle
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
POSTGRES_PORT=5432

JWT_SECRET=votre_cle_secrete
JWT_EXPIRATION=86400000
SEEDING=false
```


### Démarrage
```bash
# Étape 1 : Compiler l'application
mvn clean package

# Étape 2 : Lancer les conteneurs
docker compose up -d

# Étape 3 : (optionnel) Initialiser des données
SEEDING=true docker compose up -d
```

## Documentation
- Swagger UI : `http://localhost:9090/swagger-ui.html`
- OpenAPI : `http://localhost:9090/v3/api-docs`

## 🗂️ Structure du projet

```
src/
├── main/
│   ├── java/com/actionelles/
│   │   ├── domain/          # Modèle métier
│   │   ├── application/     # Cas d’utilisation
│   │   ├── infrastructure/  # Contrôleurs, adaptateurs DB, config
│   │   └── ActionEllesApplication.java
│   └── resources/
│       ├── application.yml
│       └── ...
└── test/
```

## Support
- Issues : https://github.com/komobe/action-elles/issues
- Email : komobesokona@gmail.com