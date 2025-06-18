import {httpClient} from './http/http-client';
import {buildUrl} from '@/utils/apiUtils';
import {Credentials} from "@contexts/auth-context.ts";

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
    return httpClient.post<AuthResponse>(buildUrl('api/v1/auth', 'login'), {username, password});
  },

  register: async ({username, password}: Credentials) => {
    return httpClient.post<void>(buildUrl('api/v1/auth', 'register'), {username, password});
  },

  getCurrentUser: async () => {
    return httpClient.get<UserInfo>(buildUrl('api/v1/auth', 'profile'));
  }
}; 