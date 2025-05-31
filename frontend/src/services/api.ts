import { http } from './http';
import { API_ENDPOINTS } from '../config/api';

interface ApiResponse<T> {
  status: string;
  data: T;
}

interface AuthResponse {
  accessToken: string;
  expiresIn: number;
  tokenType: string;
  user: {
    username: string;
    roles: string[];
  };
}

interface UserProfile {
  username: string;
  roles: string[];
}

export const authApi = {
  login: async (username: string, password: string): Promise<ApiResponse<AuthResponse>> => {
    return http.post(API_ENDPOINTS.auth.login, { username, password });
  },

  register: async (username: string, password: string): Promise<ApiResponse<AuthResponse>> => {
    return http.post(API_ENDPOINTS.auth.register, { username, password });
  },

  getCurrentUser: async (): Promise<ApiResponse<UserProfile>> => {
    return http.get(API_ENDPOINTS.auth.profile);
  }
}; 