import { httpClient } from './http/http-client';
import { API_ENDPOINTS } from '../config/api';
import { Credentials } from '@/contexts/AuthContext';

interface AuthResponse {
  accessToken: string;
  expiresIn: number;
  tokenType: string;
  user: UserInfo;
}

export interface UserInfo {
  username: string;
  roles: string[];
  email?: string;
}

export const authHttpService = {
  login: async (username: string, password: string) => {
    return httpClient.post<AuthResponse>(API_ENDPOINTS.auth.login, { username, password });
  },

  register: async ({ username, password }: Credentials) => {
    return httpClient.post<void>(API_ENDPOINTS.auth.register, { username, password });
  },

  getCurrentUser: async () => {
    return httpClient.get<UserInfo>(API_ENDPOINTS.auth.profile);
  }
}; 