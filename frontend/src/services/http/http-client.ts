import {HttpResponse} from './response.type';
import {HttpError} from "@services/http/ http-error.ts";

interface TokenManager {
  getToken: () => string | null;
  removeToken: () => void;
}

let tokenManager: TokenManager = {
  getToken: () => localStorage.getItem('token'),
  removeToken: () => localStorage.removeItem('token'),
};

export const configureHttpClient = (manager: TokenManager) => {
  tokenManager = manager;
};

const getAuthHeader = (ignoreContentType: boolean = false): Record<string, string> => {
  const token = tokenManager.getToken();
  const headers: Record<string, string> = {};

  if (!ignoreContentType) {
    headers['Content-Type'] = 'application/json';
  }

  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  return headers;
};

const handleResponse = async <T>(response: Response): Promise<HttpResponse<T>> => {
  let responseParsed: HttpResponse<T> | null = null;

  const contentType = response.headers.get('content-type');
  const isJson = contentType?.includes('application/json');

  if (!isJson) {
    const blob = await response.blob();
    const headers: Record<string, string> = {};
    response.headers.forEach((value, key) => {
      headers[key] = value;
    });

    return {
      status: 'success',
      data: blob as T,
      headers,
    };
  }

  try {
    responseParsed = await response.json();
  } catch {
    responseParsed = null;
  }

  if (response.status === 401) {
    tokenManager.removeToken();
    throw new HttpError(responseParsed?.message ?? 'Session expirée', {
      isAuthError: true,
      status: response.status,
      data: responseParsed?.data,
    });
  }

  if (responseParsed?.status === 'error') {
    throw new HttpError(responseParsed?.message ?? 'Une erreur est survenue', {
      status: response.status,
      data: responseParsed?.data,
    });
  }

  if (!response.ok) {
    throw new HttpError(responseParsed?.message ?? `Erreur HTTP: ${response.status}`, {
      status: response.status,
      data: responseParsed?.data,
    });
  }

  const headers: Record<string, string> = {};
  response.headers.forEach((value, key) => {
    headers[key] = value;
  });

  return {
    ...responseParsed,
    headers,
  } as HttpResponse<T>;
};

const createRequest = (method: string, headers: Record<string, string>, data?: unknown): RequestInit => {
  const requestInit: RequestInit = { method, headers };

  if (data !== undefined && ['POST', 'PUT', 'PATCH'].includes(method)) {
    requestInit.body = JSON.stringify(data);
  }

  return requestInit;
};

export const httpClient = {
  async get<T>(url: string): Promise<HttpResponse<T>> {
    const headers = getAuthHeader();
    const response = await fetch(url, createRequest('GET', headers));
    return handleResponse<T>(response);
  },

  async download(url: string): Promise<HttpResponse<Blob>> {
    const headers = getAuthHeader(true);
    const response = await fetch(url, createRequest('GET', headers));
    return handleResponse<Blob>(response);
  },

  async post<T>(url: string, data: unknown): Promise<HttpResponse<T>> {
    const headers = getAuthHeader();
    const response = await fetch(url, createRequest('POST', headers, data));
    return handleResponse<T>(response);
  },

  async put<T>(url: string, data: unknown): Promise<HttpResponse<T>> {
    const headers = getAuthHeader();
    const response = await fetch(url, createRequest('PUT', headers, data));
    return handleResponse<T>(response);
  },

  async patch<T>(url: string, data: unknown): Promise<HttpResponse<T>> {
    const headers = getAuthHeader();
    const response = await fetch(url, createRequest('PATCH', headers, data));
    return handleResponse<T>(response);
  },

  async delete<T = void>(url: string): Promise<HttpResponse<T>> {
    const headers = getAuthHeader();
    const response = await fetch(url, createRequest('DELETE', headers));
    return handleResponse<T>(response);
  },
};