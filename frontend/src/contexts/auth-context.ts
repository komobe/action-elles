import { createContext } from 'react';
import { HttpResponse } from '@/services/http/response.type';
import { UserInfo } from '@/services/auth.http-service';

export interface Credentials {
  username: string;
  password: string;
}

export interface AuthContextType {
  user: UserInfo | null;
  isLoading: boolean;
  login: (credentials: Credentials) => Promise<void>;
  register: (credentials: Credentials) => Promise<HttpResponse<unknown>>;
  logout: () => void;
}

export const AuthContext = createContext<AuthContextType | undefined>(undefined); 