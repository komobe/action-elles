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
  constructor(public status: number, message: string) {
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
  constructor(message: string) {
    super(message);
    this.name = 'ApiError';
  }
}

const getAuthHeader = (): Record<string, string> => {
  const token = localStorage.getItem('token');
  return {
    'Content-Type': 'application/json',
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
  };
};

const handleResponse = async (response: Response) => {
  const data = await response.json().catch(() => ({}));

  if (!response.ok) {
    if (response.status === 401) {
      throw new AuthenticationError('Session expirée ou invalide');
    }
    throw new ApiError(`Erreur HTTP: ${response.status}`);
  }

  return data;
};

const createAbortController = (timeout: number) => {
  const controller = new AbortController();
  setTimeout(() => controller.abort(), timeout);
  return controller;
};

export const http = {
  request: async (url: string, options: RequestInit = {}) => {
    const controller = createAbortController(API_CONFIG.timeout);

    const baseHeaders: HeadersInit = {
      ...API_CONFIG.headers,
    };

    // Ajouter le token seulement si ce n'est pas une route publique
    if (!isPublicRoute(url)) {
      const token = getAuthHeader();
      if (token) {
        baseHeaders.Authorization = `Bearer ${token}`;
      }
    }

    const config: RequestInit = {
      ...options,
      headers: {
        ...baseHeaders,
        ...(options.headers || {}),
      },
      signal: controller.signal,
    };

    try {
      const response = await fetch(url, config);
      return handleResponse(response);
    } catch (error) {
      if (error instanceof Error) {
        if (error.name === 'AbortError') {
          throw new ApiError(408, 'La requête a pris trop de temps');
        }
      }
      if (error instanceof HttpError) {
        throw error;
      }
      throw new ApiError(0, 'Erreur de connexion au serveur');
    }
  },

  get: async <T>(url: string): Promise<T> => {
    const response = await fetch(url, {
      method: 'GET',
      headers: getAuthHeader(),
    });

    if (!response.ok) {
      if (response.status === 401) {
        throw new AuthenticationError('Session expirée ou invalide');
      }
      throw new ApiError(`Erreur HTTP: ${response.status}`);
    }

    return response.json();
  },

  post: async <T>(url: string, data: unknown): Promise<T> => {
    const response = await fetch(url, {
      method: 'POST',
      headers: getAuthHeader(),
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      if (response.status === 401) {
        throw new AuthenticationError('Session expirée ou invalide');
      }
      throw new ApiError(`Erreur HTTP: ${response.status}`);
    }

    return response.json();
  },

  put: async <T>(url: string, data: unknown): Promise<T> => {
    const response = await fetch(url, {
      method: 'PUT',
      headers: getAuthHeader(),
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      if (response.status === 401) {
        throw new AuthenticationError('Session expirée ou invalide');
      }
      throw new ApiError(`Erreur HTTP: ${response.status}`);
    }

    return response.json();
  },

  delete: async <T>(url: string): Promise<T> => {
    const response = await fetch(url, {
      method: 'DELETE',
      headers: getAuthHeader(),
    });

    if (!response.ok) {
      if (response.status === 401) {
        throw new AuthenticationError('Session expirée ou invalide');
      }
      throw new ApiError(`Erreur HTTP: ${response.status}`);
    }

    return response.json();
  },
}; 