import {httpClient} from './http/http-client';
import {API_ENDPOINTS} from '../config/api';

interface AuthResponse {
  accessToken: string;
  expiresIn: number;
  tokenType: string;
  user: UserInfo;
}

export interface UserInfo {
  username: string;
  roles: string[];
}

export const authHttpService = {
  login: async (username: string, password: string) => {
    return httpClient.post<AuthResponse>(API_ENDPOINTS.auth.login, { username, password });
  },

  register: async (username: string, password: string) => {
    return httpClient.post<void>(API_ENDPOINTS.auth.register, { username, password });
  },

  getCurrentUser: async () => {
    return httpClient.get<UserInfo>(API_ENDPOINTS.auth.profile);
  }
}; 