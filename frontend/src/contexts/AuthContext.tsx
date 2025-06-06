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
  const retryTimeoutRef = useRef<number | undefined>(undefined);
  const isInitialMount = useRef(true);

  const loadUser = useCallback(async () => {
    try {
      const token = localStorage.getItem('token');

      if (!token) {
        setIsLoading(false);
        if (!isInitialMount.current && window.location.pathname !== '/login' && window.location.pathname !== '/register') {
          console.log('Redirection vers /login car pas de token');
          navigate('/login', { replace: true });
        }
        return;
      }

      const response = await authApi.getCurrentUser();

      if (response.status === 'success' && response.data) {
        setUser(response.data);
        setIsLoading(false);
        setRetryCount(0);
      } else {
        localStorage.removeItem('token');
        setUser(null);
        setIsLoading(false);
        if (!isInitialMount.current && window.location.pathname !== '/login' && window.location.pathname !== '/register') {
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
        if (!isInitialMount.current && window.location.pathname !== '/login' && window.location.pathname !== '/register') {
          navigate('/login', { replace: true });
        }
      } else if (retryCount < 1) {
        setRetryCount(prev => prev + 1);
        retryTimeoutRef.current = window.setTimeout(() => {
          loadUser();
        }, 1_000);
      } else {
        localStorage.removeItem('token');
        setUser(null);
        setIsLoading(false);
        if (!isInitialMount.current && window.location.pathname !== '/login' && window.location.pathname !== '/register') {
          navigate('/login', { replace: true });
        }
      }
    }
  }, [navigate, retryCount]);

  // Vérification périodique de l'utilisateur toutes les 10 minutes
  useEffect(() => {
    setRetryCount(0);
    loadUser();
    isInitialMount.current = false;

    const intervalId = setInterval(() => {
      loadUser();
    }, 10 * 60 * 1000); // 10 minutes

    return () => {
      if (retryTimeoutRef.current) {
        window.clearTimeout(retryTimeoutRef.current);
      }
      clearInterval(intervalId);
    };
  }, [loadUser]);

  // Gestion améliorée des événements de stockage
  const handleStorageChange = useCallback((e: StorageEvent) => {
    // Ne traiter que les événements provenant d'autres onglets
    if (e.key === 'token' && e.storageArea === localStorage) {
      console.log('Changement détecté dans le token depuis un autre onglet');

      // Si le token a été supprimé dans un autre onglet
      if (!e.newValue) {
        setUser(null);
        setIsLoading(false);
        if (window.location.pathname !== '/login' && window.location.pathname !== '/register') {
          navigate('/login', { replace: true });
        }
      }
      // Si un nouveau token a été défini dans un autre onglet et que nous ne sommes pas connectés
      else if (!user && window.location.pathname !== '/login' && window.location.pathname !== '/register') {
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

  const value = { user, isLoading, login, register, logout };

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