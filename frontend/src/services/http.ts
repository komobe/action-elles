import { API_CONFIG, API_ENDPOINTS } from '../config/api';

// Routes qui ne nécessitent pas de token
const PUBLIC_ROUTES = [
  API_ENDPOINTS.auth.login,
  API_ENDPOINTS.auth.register
];

const isPublicRoute = (url: string): boolean => {
  return PUBLIC_ROUTES.some(route => route && url.includes(route));
};

export class HttpError extends Error {
  constructor(message: string) {
    super(message);
    this.name = 'HttpError';
  }
}

export class AuthenticationError extends Error {
  constructor(message: string) {
    super(message);
    this.name = 'AuthenticationError';
  }
}

export class ApiError extends Error {
  constructor(public status: number, message: string) {
    super(message);
    this.name = 'ApiError';
  }
}

const getAuthHeader = (): Record<string, string> => {
  const token = localStorage.getItem('token');
  if (!token) {
    return {
      'Content-Type': 'application/json'
    };
  }
  return {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}`
  };
};

const createAbortController = (timeout: number) => {
  const controller = new AbortController();
  setTimeout(() => controller.abort(), timeout);
  return controller;
};

const handleResponse = async (response: Response) => {
  if (!response.ok) {
    if (response.status === 401) {
      localStorage.removeItem('token');
      throw new AuthenticationError('Session expirée ou invalide');
    }
    const errorData = await response.json().catch(() => ({}));
    throw new ApiError(response.status, errorData.message || `Erreur HTTP: ${response.status}`);
  }

  try {
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Erreur lors du parsing de la réponse:', error);
    throw new ApiError(500, 'Erreur lors du traitement de la réponse');
  }
};

export const http = {
  get: async <T>(url: string): Promise<T> => {
    const headers = getAuthHeader();
    const response = await fetch(url, {
      method: 'GET',
      headers
    });
    return handleResponse(response);
  },

  post: async <T>(url: string, data: unknown): Promise<T> => {
    const headers = getAuthHeader();
    const response = await fetch(url, {
      method: 'POST',
      headers,
      body: JSON.stringify(data)
    });
    return handleResponse(response);
  },

  put: async <T>(url: string, data: unknown): Promise<T> => {
    const headers = getAuthHeader();
    const response = await fetch(url, {
      method: 'PUT',
      headers,
      body: JSON.stringify(data)
    });
    return handleResponse(response);
  },

  delete: async <T>(url: string): Promise<T> => {
    const headers = getAuthHeader();
    const response = await fetch(url, {
      method: 'DELETE',
      headers
    });
    return handleResponse(response);
  }
}; 