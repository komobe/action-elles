export interface PaginationMetadata {
  number: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
  offset: number;
  remainingElements: number;
  remainingPages: number;
}

export interface PaginationLinks {
  current: string;
  first: string;
  last: string;
  next: string | null;
  prev: string | null;
}

export interface ApiResponse<T> {
  status: 'success' | 'error';
  data?: T;
  message?: string;
  isAuthError?: boolean;
  metadata?: PaginationMetadata;
  links?: PaginationLinks;
  size?: number;
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

const handleResponse = async (response: Response) => {
  try {
    const data = await response.json();

    if (response.status === 401) {
      localStorage.removeItem('token');
      return {
        status: 'error',
        message: 'Session expirée ou invalide',
        _isAuthError: true
      };
    }

    return data;
  } catch (error) {
    console.error('Erreur lors du parsing de la réponse:', error);

    if (response.status === 401) {
      localStorage.removeItem('token');
      return {
        status: 'error',
        message: 'Session expirée ou invalide',
        _isAuthError: true
      };
    }

    return {
      status: 'error',
      message: `Erreur HTTP: ${response.status}`
    };
  }
};

export const http = {
  get: async <T>(url: string): Promise<ApiResponse<T>> => {
    const headers = getAuthHeader();
    const response = await fetch(url, {
      method: 'GET',
      headers
    });
    return handleResponse(response);
  },

  post: async <T>(url: string, data: unknown): Promise<ApiResponse<T>> => {
    const headers = getAuthHeader();
    const response = await fetch(url, {
      method: 'POST',
      headers,
      body: JSON.stringify(data)
    });
    return handleResponse(response);
  },

  put: async <T>(url: string, data: unknown): Promise<ApiResponse<T>> => {
    const headers = getAuthHeader();
    const response = await fetch(url, {
      method: 'PUT',
      headers,
      body: JSON.stringify(data)
    });
    return handleResponse(response);
  },

  delete: async <T>(url: string): Promise<ApiResponse<T>> => {
    const headers = getAuthHeader();
    const response = await fetch(url, {
      method: 'DELETE',
      headers
    });
    return handleResponse(response);
  }
};