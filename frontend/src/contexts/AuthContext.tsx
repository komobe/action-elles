import { createContext, useContext, useState, useEffect, ReactNode, useRef, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { authApi } from '@services/api';
import { AuthenticationError, ApiError } from '@services/http';

interface User {
  username: string;
  roles: string[];
}

interface AuthContextType {
  user: User | null;
  isLoading: boolean;
  login: (username: string, password: string) => Promise<void>;
  register: (username: string, password: string) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const navigate = useNavigate();
  const [retryCount, setRetryCount] = useState(0);
  const retryTimeoutRef = useRef<number>();
  const isInitialMount = useRef(true);

  const loadUser = useCallback(async () => {
    try {
      const token = localStorage.getItem('token');

      if (!token) {
        setIsLoading(false);
        if (!isInitialMount.current) {
          navigate('/login', { replace: true });
        }
        return;
      }

      const response = await authApi.getCurrentUser();

      if (response.status === 'success' && response.data) {
        setUser(response.data);
        setIsLoading(false);
      } else {
        localStorage.removeItem('token');
        setUser(null);
        setIsLoading(false);
        if (!isInitialMount.current) {
          navigate('/login', { replace: true });
        }
      }
    } catch (error) {
      if (retryTimeoutRef.current) {
        window.clearTimeout(retryTimeoutRef.current);
      }

      if (error instanceof AuthenticationError ||
        (error instanceof ApiError && error.status === 400)) {
        localStorage.removeItem('token');
        setUser(null);
        setIsLoading(false);
        if (!isInitialMount.current) {
          navigate('/login', { replace: true });
        }
      } else if (retryCount < 2) {
        setRetryCount(prev => prev + 1);
        retryTimeoutRef.current = window.setTimeout(() => {
          loadUser();
        }, 2000);
      } else {
        localStorage.removeItem('token');
        setUser(null);
        setIsLoading(false);
        if (!isInitialMount.current) {
          navigate('/login', { replace: true });
        }
      }
    }
  }, [navigate, retryCount]);

  useEffect(() => {
    setRetryCount(0);
    loadUser();
    isInitialMount.current = false;

    return () => {
      if (retryTimeoutRef.current) {
        window.clearTimeout(retryTimeoutRef.current);
      }
    };
  }, [loadUser]);

  const handleStorageChange = useCallback((e: StorageEvent) => {
    if (e.key === 'token') {
      if (!e.newValue) {
        setUser(null);
        setIsLoading(false);
        navigate('/login', { replace: true });
      } else if (!user) {
        setRetryCount(0);
        loadUser();
      }
    }
  }, [user, navigate, loadUser]);

  useEffect(() => {
    window.addEventListener('storage', handleStorageChange);
    return () => {
      window.removeEventListener('storage', handleStorageChange);
    };
  }, [handleStorageChange]);

  const login = useCallback(async (username: string, password: string): Promise<void> => {
    try {
      setIsLoading(true);
      const response = await authApi.login(username, password);

      if (response.status === 'success' && response.data) {
        const { accessToken, user: userData } = response.data;
        localStorage.setItem('token', accessToken);
        setUser(userData);
        setIsLoading(false);
        navigate('/home', { replace: true });
      } else {
        setIsLoading(false);
        throw new Error('Réponse invalide du serveur');
      }
    } catch (error) {
      localStorage.removeItem('token');
      setUser(null);
      setIsLoading(false);
      throw error;
    }
  }, [navigate]);

  const register = useCallback(async (username: string, password: string): Promise<void> => {
    try {
      setIsLoading(true);
      const response = await authApi.register(username, password);

      if (response.status === 'success' && response.data) {
        const { accessToken, user: userData } = response.data;
        localStorage.setItem('token', accessToken);
        setUser(userData);
        setIsLoading(false);
        navigate('/home', { replace: true });
      } else {
        setIsLoading(false);
        throw new Error('Réponse invalide du serveur');
      }
    } catch (error) {
      localStorage.removeItem('token');
      setUser(null);
      setIsLoading(false);
      throw error;
    }
  }, [navigate]);

  const logout = useCallback(() => {
    localStorage.removeItem('token');
    setUser(null);
    setIsLoading(false);
    navigate('/login', { replace: true });
  }, [navigate]);

  const value = {
    user,
    isLoading,
    login,
    register,
    logout
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth doit être utilisé à l\'intérieur d\'un AuthProvider');
  }
  return context;
} 