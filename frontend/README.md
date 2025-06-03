# Frontend Action'Elles

## 📋 Vue d'ensemble

Interface utilisateur de l'application Action'Elles, développée avec React 19, TypeScript et Vite. L'application utilise PrimeReact pour les composants UI et TailwindCSS pour le styling.

## 🏗 Architecture Technique

### Stack Technique

#### Core Technologies
- **React 19**: 
  - Version la plus récente de React
  - Utilisation des Hooks pour la gestion d'état
  - Composants fonctionnels modernes

- **TypeScript 5.8**: 
  - Typage strict pour une meilleure fiabilité
  - Configuration optimisée via tsconfig
  - Support complet des dernières fonctionnalités ECMAScript

- **Vite 6**:
  - Build tool ultra-rapide
  - Hot Module Replacement (HMR)
  - Configuration optimisée pour React et TypeScript

#### UI et Styling
- **PrimeReact 10**:
  - Bibliothèque complète de composants UI
  - Thème personnalisable
  - Composants accessibles et responsifs

- **TailwindCSS 3.4**:
  - Utilitaires CSS modernes
  - Configuration personnalisée via tailwind.config.js
  - Integration avec PostCSS

#### Routing et Navigation
- **React Router 7**:
  - Routing déclaratif moderne
  - Support des routes imbriquées
  - Gestion avancée de la navigation

#### Utilitaires
- **FontAwesome**:
  - Icônes vectorielles
  - Support SVG optimisé
  - Variantes Regular et Solid

## 🗂 Structure du Projet

```
src/
├── assets/        # Ressources statiques
├── components/    # Composants réutilisables
│   ├── ui/       # Composants UI génériques
│   └── [...]     # Composants spécifiques
├── pages/        # Pages de l'application
├── routes/       # Configuration des routes
├── styles/       # Styles globaux
└── [...]         # Autres dossiers de l'application
```

## 🚀 Installation et Démarrage

### Prérequis
- Node.js (version LTS recommandée)
- pnpm (gestionnaire de paquets)

### Installation
```bash
# Installation des dépendances
pnpm install
```

### Scripts Disponibles
- `pnpm dev` - Lance le serveur de développement
- `pnpm build` - Compile le projet (TypeScript + Vite)
- `pnpm lint` - Vérifie le code avec ESLint
- `pnpm preview` - Prévisualise la version de production

## 🔧 Configuration

### TypeScript
- Configuration stricte via tsconfig.json
- Support des dernières fonctionnalités ECMAScript
- Vérification de types complète

### ESLint
- Configuration moderne avec @eslint/js
- Plugins spécifiques pour React
- Règles strictes pour la qualité du code

### Vite
- Configuration optimisée pour le développement
- Build rapide en production
- Support des modules ES

## 📱 UI/UX

### Composants
- Utilisation de PrimeReact pour les composants UI complexes
- Personnalisation avec TailwindCSS
- Icônes FontAwesome pour une interface moderne

### Responsive Design
- Design mobile-first avec TailwindCSS
- Breakpoints configurables
- Composants adaptatifs

## 🔐 Sécurité

- Types stricts avec TypeScript
- Dépendances maintenues à jour
- Configuration sécurisée de l'environnement

## 🚀 Performance

### Optimisations
- Build optimisé avec Vite
- Code splitting automatique
- Optimisation des assets

### Développement
- Hot Module Replacement
- Rechargement rapide
- Outils de développement modernes

## 📦 Dépendances Principales

### Production
- react: ^19.1.0
- react-router-dom: ^7.6.1
- primereact: ^10.9.5
- tailwindcss: 3.4.1

### Développement
- typescript: ~5.8.3
- vite: ^6.3.5
- eslint: ^9.25.0

## 🤝 Contribution

1. Cloner le repository
2. Créer une branche (`git checkout -b feature/amelioration`)
3. Commit des changements (`git commit -am 'Ajout de fonctionnalité'`)
4. Push vers la branche (`git push origin feature/amelioration`)
5. Créer une Pull Request

## 📝 Standards de Code

- TypeScript strict
- ESLint pour la qualité du code
- Composants fonctionnels React
- Styles avec TailwindCSS
