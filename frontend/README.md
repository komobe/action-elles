# Frontend Action'Elles

Interface utilisateur moderne développée avec React, TypeScript et Vite.

## 🚀 Démarrage Rapide

### Prérequis
- Node.js (LTS)
- pnpm (`corepack enable pnpm`)

### Installation
```bash
pnpm install
```

### Scripts Disponibles
```bash
pnpm dev      # Développement
pnpm build    # Production
pnpm lint     # Vérification du code
pnpm preview  # Prévisualisation production
```

## 🏗️ Stack Technique

### Core
- React 19 + TypeScript 5.8
- Vite 6 (build tool)
- React Router 7 (navigation)

### UI/UX
- PrimeReact 10 (composants)
- TailwindCSS 3.4 (styling)
- FontAwesome (icônes)

## 📦 Structure du Projet

```
src/
├── assets/        # Ressources statiques
├── components/    # Composants réutilisables
│   ├── ui/       # Composants UI génériques
│   └── [...]     # Composants spécifiques
├── pages/        # Pages de l'application
├── routes/       # Configuration des routes
└── styles/       # Styles globaux
```

## 🛠️ Déploiement

### Avec Docker
```bash
# Depuis le dossier frontend
docker compose up -d --build
```

### Sans Docker
1. Installer les dépendances : `pnpm install`
2. Configurer l'environnement (optionnel) :
   ```bash
   export VITE_API_URL=http://localhost:9090
   ```
3. Build : `pnpm build`
4. Déployer le contenu de `dist/` sur votre serveur web

## 🔧 Configuration

### Variables d'Environnement
```env
VITE_API_URL=http://localhost:9090  # URL du backend
FRONTEND_PORT=80                    # Port du serveur web
```

### TypeScript & ESLint
- Configuration stricte via `tsconfig.json`
- Règles ESLint modernes avec plugins React

## 🤝 Contribution

1. Fork le projet
2. Créer une branche (`git checkout -b feature/amelioration`)
3. Commit (`git commit -am 'Ajout de fonctionnalité'`)
4. Push (`git push origin feature/amelioration`)
5. Créer une Pull Request

## 📞 Support

- Issues : https://github.com/komobe/action-elles/issues
- Email : komobesokona@gmail.com
