import {HttpResponse} from "./response.type";

interface TokenManager {
  getToken: () => string | null;
  removeToken: () => void;
}

let tokenManager: TokenManager = {
  getToken: () => localStorage.getItem('token'),
  removeToken: () => localStorage.removeItem('token')
};

export const configureHttpClient = (manager: TokenManager) => {
  tokenManager = manager;
};

const getAuthHeader = (): Record<string, string> => {
  const token = tokenManager.getToken();
  const headers: Record<string, string> = {
    'Content-Type': 'application/json'
  };

  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  return headers;
};

const handleResponse = async <T>(response: Response): Promise<HttpResponse<T>> => {
  let responseParsed: any;

  try {
    responseParsed = await response.json();
  } catch (error) {
    responseParsed = null;
  }

  if (response.status === 401) {
    tokenManager.removeToken();
    const error = new Error(responseParsed?.message || 'Session expirée');
    (error as any).data = responseParsed?.data;
    (error as any).isAuthError = true;
    throw error;
  }

  if (responseParsed?.status === 'error') {
    const error = new Error(responseParsed.message || 'Une erreur est survenue');
    if (responseParsed?.data) {
      (error as any).data = responseParsed.data;
    }

    throw error;
  }

  if (!response.ok) {
    const error = new Error(responseParsed?.message || `Erreur HTTP: ${response.status}`);
    if (responseParsed?.data) {
      (error as any).data = responseParsed.data;
    }

    (error as any).status = response.status;

    throw error;
  }

  if (responseParsed?.metadata || responseParsed?.links) {
    return {
      status: responseParsed.status,
      data: responseParsed.data,
      metadata: responseParsed.metadata,
      links: responseParsed.links,
      size: responseParsed.size
    };
  }

  return {
    status: responseParsed.status,
    data: responseParsed?.data !== undefined ? responseParsed.data : responseParsed
  };
};

export const httpClient = {
  async get<T>(url: string): Promise<HttpResponse<T>> {
    const headers = getAuthHeader();
    const response = await fetch(url, { method: 'GET', headers });
    return handleResponse<T>(response);
  },

  async post<T>(url: string, data: unknown): Promise<HttpResponse<T>> {
    const headers = getAuthHeader();
    const response = await fetch(url, {
      method: 'POST',
      headers,
      body: JSON.stringify(data)
    });
    return handleResponse<T>(response);
  },

  async put<T>(url: string, data: unknown): Promise<HttpResponse<T>> {
    const headers = getAuthHeader();
    const response = await fetch(url, {
      method: 'PUT',
      headers,
      body: JSON.stringify(data)
    });
    return handleResponse<T>(response);
  },

  async patch<T>(url: string, data: unknown): Promise<HttpResponse<T>> {
    const headers = getAuthHeader();
    const response = await fetch(url, {
      method: 'PATCH',
      headers,
      body: JSON.stringify(data)
    });
    return handleResponse<T>(response);
  },

  async delete<T = void>(url: string): Promise<HttpResponse<T>> {
    const headers = getAuthHeader();
    const response = await fetch(url, { method: 'DELETE', headers });
    return handleResponse<T>(response);
  }
};