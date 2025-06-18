import { httpClient } from './http/http-client';
import { Credentials } from '@/contexts/AuthContext';
import { buildUrl } from '@/utils/apiUtils';

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
    return httpClient.post(buildUrl('api/v1/auth', 'login'), { username, password });
  },

  register: async ({ username, password }: Credentials) => {
    return httpClient.post(buildUrl('api/v1/auth', 'register'), { username, password });
  },

  getCurrentUser: async () => {
    return httpClient.get(buildUrl('api/v1/auth', 'profile'));
  }
}; 