// URL de base de l'API
const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:9083';

// Configuration des endpoints de l'API
export const API_ENDPOINTS = {
  auth: {
    login: `${API_BASE_URL}/api/v1/auth/login`,
    register: `${API_BASE_URL}/api/v1/auth/register`,
    refreshToken: `${API_BASE_URL}/api/v1/auth/refresh-token`,
    profile: `${API_BASE_URL}/api/v1/auth/profile`,
  },
  users: {
    list: `${API_BASE_URL}/api/v1/utilisateurs`,
    delete: (id: string) => `${API_BASE_URL}/api/v1/utilisateurs/${id}`,
    update: `${API_BASE_URL}/api/v1/utilisateurs`,
    resetPassword: `${API_BASE_URL}/api/v1/utilisateurs/reset-password`
  },
  roles: {
    list: `${API_BASE_URL}/api/v1/roles`,
    delete: (id: string) => `${API_BASE_URL}/api/v1/roles/${id}`,
    update: (id: string) => `${API_BASE_URL}/api/v1/roles/${id}`,
    create: `${API_BASE_URL}/api/v1/roles`,
  },
  devis: {
    simuler: `${API_BASE_URL}/api/v1/devis/simuler`,
    enregistrer: `${API_BASE_URL}/api/v1/devis/enregistrer`,
    produits: `${API_BASE_URL}/api/v1/devis/produits`,
    categories: `${API_BASE_URL}/api/v1/devis/categories`,
  },
  souscription: {
    creer: `${API_BASE_URL}/api/v1/subscriptions`,
    list: `${API_BASE_URL}/api/v1/subscriptions`,
    details: (id: string) => `${API_BASE_URL}/api/v1/subscriptions/${id}`,
    statut: (id: string) => `${API_BASE_URL}/api/v1/subscriptions/${id}/statut`,
    gerererAttestation: (id: string) => `${API_BASE_URL}/api/v1/subscriptions/${id}/attestation`,
  },
  produit: {
    list: `${API_BASE_URL}/api/v1/produits`,
  },
  categorieVehicule: {
    list: `${API_BASE_URL}/api/v1/categories-vehicules`,
  },
} as const;

// Configuration des options par défaut pour les requêtes API
export const API_CONFIG = {
  headers: {
    'Content-Type': 'application/json',
  },
  // Timeout en millisecondes
  timeout: 10000,
} as const; 