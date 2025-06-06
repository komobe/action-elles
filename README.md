## 🎯 Vue d'ensemble

Action'Elle est composée de deux applications principales :

- **Frontend** : Interface utilisateur moderne développée avec React
- **Backend** : API REST développée avec Spring Boot

Pour plus de détails techniques, consultez :
- [Documentation Frontend](frontend/README.md)
- [Documentation Backend](backend/README.md)

## 🚀 Installation

### Option 1 : Installation avec Docker (Recommandée)

#### Prérequis
- Docker Desktop
- Docker Compose(V2)

#### Commandes Docker (Installation rapide)

Pour démarrer le frontend (sans reconstruire l'image) :
```bash
docker compose -f frontend/compose.yaml up -d
```

Pour reconstruire (rebuild) et démarrer le frontend :
```bash
docker compose -f frontend/compose.yaml up -d --build
```

Pour démarrer le backend (sans reconstruire l'image) :
```bash
docker compose -f backend/compose.yaml up -d
```

Pour reconstruire (rebuild) et démarrer le backend :
```bash
docker compose -f backend/compose.yaml up -d --build
```

> **Note** : Dans le fichier backend/compose.yaml, la variable d'environnement (par exemple, POSTGRES_DB) peut être surchargée via un fichier .env ou en ligne de commande (par exemple, en définissant POSTGRES_DB=ma_base).

#### Commandes Docker utiles (Arrêt, logs, redémarrage, etc.)

```bash
# Arrêter tous les services (frontend et backend)
docker compose -f frontend/compose.yaml down
docker compose -f backend/compose.yaml down

# Voir les logs (par exemple, pour le backend)
docker compose -f backend/compose.yaml logs -f

# Redémarrer un service (par exemple, le backend)
docker compose -f backend/compose.yaml restart

# Vérifier l'état des conteneurs (exemple pour le backend)
docker ps | grep action-elles-api

# Consulter les logs (exemple pour le backend) (sans suivre en temps réel)
docker logs action-elles-api

# Vérifier l'état des conteneurs (exemple pour le frontend)
docker ps | grep action-elles-frontend

# Consulter les logs (exemple pour le frontend) (sans suivre en temps réel)
docker logs action-elles-frontend
```

### Variables d'environnement importantes

#### Backend (.env)
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
POSTGRES_HOST=action-elles-db-postgres
POSTGRES_PORT=5432

# Configuration sécurité
JWT_SECRET=votre_cle_secrete_tres_longue_et_securisee
JWT_EXPIRATION=86400000

# Configuration application
SPRING_PROFILES_ACTIVE=dev
SEEDING=false
```

#### Frontend (.env)
```bash
# Configuration API
VITE_API_URL=http://localhost:9093

# Configuration environnement
NODE_ENV=development  # development, production, test
```

### Option 2 : Installation manuelle

#### Prérequis
- Node.js 18+
- Java 21
- PostgreSQL 15
- PNPM 8.15.4+
- Maven

#### Installation
1. Cloner le projet :
```bash
git clone https://github.com/komobe/action-elles.git
cd action-elles
```

2. Installer et démarrer le backend :
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

3. Dans un autre terminal, installer et démarrer le frontend :
```bash
cd frontend
pnpm install
pnpm dev
```

## 🔗 Accès aux applications

Les applications seront accessibles sur :
- **Frontend** : http://localhost:5173
- **Backend** : http://localhost:9093
- **API Documentation** : http://localhost:9093/swagger-ui.html

## 📁 Structure du projet

```
action-elles/
├── frontend/          # Application front
├── backend/           # Application backend
└── README.md          # Ce fichier
```

## 🛠️ Technologies principales

### Frontend
- React 19.0.0 + TypeScript
- Tailwind CSS
- Vite

### Backend
- Spring Boot 3.3.6 + Java 21
- PostgreSQL 15
- Spring Security + JWT
- Architecture hexagonale

## 📚 Documentation

- [Guide Frontend](frontend/README.md) - Documentation détaillée du frontend
- [Guide Backend](backend/README.md) - Documentation détaillée du backend
- [API Documentation](http://localhost:9093/swagger-ui.html) - Documentation interactive de l'API

## 🧪 Tests

### Tests complets
```bash
# Frontend
cd frontend && pnpm test

# Backend
cd backend && mvn test
```

### Tests avec Docker
```bash
# Frontend
docker compose -f frontend/compose.yaml exec action-elles-frontend pnpm test

# Backend
docker compose -f backend/compose.yaml exec action-elles-api mvn test
```

## 🤝 Contribution

1. Fork le projet
2. Créer une branche (`git checkout -b feature/amelioration`)
3. Commit (`git commit -am 'Ajout de fonctionnalité'`)
4. Push (`git push origin feature/amelioration`)
5. Créer une Pull Request

## 📞 Support

- **Issues** : https://github.com/komobe/action-elles/issues
- **Email** : komobesokona@gmail.com

## 📄 Licence

À définir selon les besoins du projet.