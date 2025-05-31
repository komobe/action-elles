// URL de base de l'API
const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

// Configuration des endpoints de l'API
export const API_ENDPOINTS = {
  auth: {
    login: `${API_BASE_URL}/api/login`,
    register: `${API_BASE_URL}/api/v1/utilisateurs/inscrire`,
    refreshToken: `${API_BASE_URL}/api/refresh-token`,
    profile: `${API_BASE_URL}/api/v1/utilisateurs/profile`,
  },
  users: {
    list: `${API_BASE_URL}/api/v1/utilisateurs`,
    delete: (id: string) => `${API_BASE_URL}/api/v1/utilisateurs/${id}`,
    update: (id: string) => `${API_BASE_URL}/api/v1/utilisateurs/${id}`
  },
  roles: {
    list: `${API_BASE_URL}/api/v1/roles`
  },
  devis: {
    simuler: `${API_BASE_URL}/api/v1/devis/simuler`,
    enregistrer: `${API_BASE_URL}/api/v1/devis/enregistrer`,
  },
  souscription: {
    creer: `${API_BASE_URL}/api/v1/subscriptions`,
    list: `${API_BASE_URL}/api/v1/subscriptions`,
    details: (id: string) => `${API_BASE_URL}/api/v1/subscriptions/${id}`,
  },
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