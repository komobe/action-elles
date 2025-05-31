// URL de base de l'API
const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

// Configuration des endpoints de l'API
export const API_ENDPOINTS = {
  auth: {
    login: `${API_BASE_URL}/api/login`,
    register: `${API_BASE_URL}/api/v1/utilisateurs/inscrire`,
    refreshToken: `${API_BASE_URL}/api/refresh-token`,
    profile: `${API_BASE_URL}/api/v1/utilisateurs/profile`
  },
  devis: {
    simuler: `${API_BASE_URL}/api/v1/devis/simuler`
  },
  souscription: {
    creer: `${API_BASE_URL}/api/v1/subscriptions`
  }
  // Ajoutez d'autres endpoints ici
} as const;

// Configuration des options par défaut pour les requêtes API
export const API_CONFIG = {
  headers: {
    'Content-Type': 'application/json',
  },
  // Timeout en millisecondes
  timeout: 10000,
} as const; 