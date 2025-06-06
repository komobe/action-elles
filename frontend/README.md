# Frontend Action'Elles

Application web Action'Elle développée avec React et TypeScript.

## 🛠️ Technologies

- **Framework** : React 19.0.0
- **Language** : TypeScript 5.4.2
- **Build Tool** : Vite 5.2.6
- **Package Manager** : PNPM 8.15.4
- **UI Framework** : 
  - Tailwind CSS 3.4.1
- **Routing** : React Router 6.22.2
- **Linting** : 
  - ESLint 8.57.0
  - Prettier 3.2.5

## 🚀 Installation

### Option 1 : Installation avec Docker (Recommandée)

#### Prérequis
- Docker Desktop
- Docker Compose(V2)

#### Installation
```bash
# Démarrer le frontend avec Docker
docker compose -f frontend/compose.yaml up -d

# Voir les logs
docker compose -f frontend/compose.yaml logs -f
```

#### Commandes Docker utiles
```bash
# Arrêter le frontend
docker compose -f frontend/compose.yaml stop

# Redémarrer le frontend
docker compose -f frontend/compose.yaml restart

# Reconstruire et redémarrer
docker compose -f frontend/compose.yaml up -d --build

# Voir les logs en temps réel
docker compose -f frontend/compose.yaml logs -f

# Accéder au shell du conteneur
docker compose -f frontend/compose.yaml exec action-elles-frontend sh
```

### Option 2 : Installation manuelle

#### Prérequis
- Node.js 20.11.1
- PNPM 8.15.4

#### Installation
```bash
# Installation des dépendances
pnpm install

# Démarrage en mode développement
pnpm dev

# Build pour la production
pnpm build

# Prévisualisation de la production
pnpm preview
```

## 🔧 Configuration

### Variables d'environnement

Créez un fichier `.env` à la racine du projet frontend :

```bash
# Configuration de l'API
VITE_API_URL=http://localhost:9093

# Configuration environnement
NODE_ENV=development  # development, production, test
```

### Configuration par défaut

Les valeurs par défaut sont définies dans le code :
- `VITE_API_URL` : http://localhost:9093
- `NODE_ENV` : development

## 🔐 Sécurité

- **Gestion des tokens JWT** : Stockage sécurisé des tokens d'authentification
- **Protection CSRF** : Protection contre les attaques Cross-Site Request Forgery
- **Headers de sécurité** : Configuration des en-têtes de sécurité


## 🏗️ Scripts disponibles

```bash
# Développement
pnpm dev              # Démarrer en mode développement
pnpm dev:host         # Démarrer avec accès réseau

# Build
pnpm build            # Build pour la production
pnpm preview          # Prévisualiser le build de production

# Qualité du code
pnpm lint             # Vérifier le code avec ESLint
pnpm lint:fix         # Corriger automatiquement les erreurs ESLint
pnpm format           # Formater le code avec Prettier
pnpm typecheck        # Vérification TypeScript

# Nettoyage
pnpm clean            # Nettoyer les fichiers de build
```

## 📦 Docker

### Build manuel

```bash
# Build de l'image
docker build -t action-elles-frontend .

# Exécution du conteneur
docker run -p 5173:5173 \
  -e VITE_API_URL=http://localhost:9093 \
  -e VITE_APP_NAME=Action'Elles \
  --name action-elles-frontend \
  action-elles-frontend
```

### Variables d'environnement Docker

```bash
# Variables disponibles lors de l'exécution Docker
VITE_API_URL=http://localhost:9093
NODE_ENV=development  # development, production, test
```

## 🎨 Guide de style

### Composants
- Utilisez les composants fonctionnels avec hooks
- Préférez TypeScript pour le typage strict
- Suivez les conventions de nommage React

### Styles
- Tailwind CSS pour le styling utilitaire
- CSS modules pour les styles spécifiques

## 📚 Documentation

### Ressources externes
- [Documentation React](https://react.dev/)
- [Documentation TypeScript](https://www.typescriptlang.org/docs/)
- [Documentation Vite](https://vitejs.dev/guide/)
- [Documentation React Router](https://reactrouter.com/en/main)

## 🐛 Résolution de problèmes

### Problèmes fréquents

#### Erreur de port
```bash
# Si le port 5173 est occupé
pnpm dev --port 3000
```

#### Problèmes de cache
```bash
# Nettoyer le cache Vite
pnpm clean
rm -rf node_modules/.cache

# Réinstaller les dépendances
rm -rf node_modules pnpm-lock.yaml
pnpm install
```

## 🤝 Contribution

### Standards de code
- Respectez les règles ESLint et Prettier
- Ajoutez des tests pour les nouvelles fonctionnalités
- Documentez les composants complexes
- Utilisez les types TypeScript appropriés

### Workflow
1. Créer une branche feature
2. Développer et tester localement
3. Vérifier le linting et les tests
4. Créer une Pull Request

## 📞 Support

- **Issues GitHub** : https://github.com/komobe/action-elles/issues
- **Email** : komobesokona@gmail.com
- **Documentation** : Consultez les liens de documentation ci-dessus