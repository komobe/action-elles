# Action'Elles

Application de gestion d'assurance développée avec une architecture moderne (frontend React + backend Spring Boot).

## 🚀 Démarrage Rapide

### Prérequis
- Docker Engine
- Docker Compose V2

### Installation et Lancement

1. **Lancer l'application complète** (frontend + backend + base de données) :
```bash
docker compose up -d --build
```

2. **Accès aux services** :
- Frontend : http://localhost
- Backend : http://localhost:9090
- Documentation API : http://localhost:9090/swagger-ui.html

### Configuration des Variables d'Environnement

⚠️ **Important** : L'application utilise trois niveaux de configuration `.env` :

1. **Racine** (`.env`) : Configuration globale
   - Copiez `.env.example` à la racine vers `.env`
   - Contient les variables partagées entre frontend et backend

2. **Frontend** (`frontend/.env`) : Configuration spécifique au frontend
   - Copiez `frontend/.env.example` vers `frontend/.env`
   - Surcharge les variables globales si nécessaire

3. **Backend** (`backend/.env`) : Configuration spécifique au backend
   - Copiez `backend/.env.example` vers `backend/.env`
   - Surcharge les variables globales si nécessaire

**Ordre de priorité** (de la plus haute à la plus basse) :
1. Variables d'environnement du système
2. `.env` spécifique au composant (frontend/backend)
3. `.env` global à la racine
4. Valeurs par défaut dans les fichiers de configuration

Exemple de configuration minimale à la racine :
```env
# Backend
APP_SERVER_PORT=9090
SPRING_PROFILES_ACTIVE=prod
POSTGRES_DB=actionelle
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres

# Frontend
FRONTEND_PORT=80
VITE_API_URL=http://localhost:9090
```

## 📚 Documentation Détaillée

Pour plus d'informations sur chaque composant :

- [Documentation Frontend](frontend/README.md)
- [Documentation Backend](backend/README.md)

## 🛠️ Commandes Utiles

```bash
# Démarrer tous les services
docker compose up -d

# Arrêter tous les services
docker compose down

# Voir les logs
docker compose logs -f

# Reconstruire et redémarrer
docker compose up -d --build
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