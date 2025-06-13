import {
  createContext,
  ReactNode,
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useRef,
  useState
} from 'react';
import { useNavigate } from 'react-router-dom';
import { authApi } from '@services/api';
import { ApiResponse } from "@services/http.ts";

interface User {
  username: string;
  roles: string[];
}

interface AuthContextType {
  user: User | null;
  login: (username: string, password: string) => Promise<void>;
  register: (username: string, password: string) => Promise<ApiResponse<unknown>>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { readonly children: ReactNode }) {
  const TOKEN_KEY = 'token';
  const USER_CHECK_INTERVAL = 10 * 60 * 1000;
  const PUBLIC_ROUTES = useMemo(() => ['/login', '/register'], []);

  const isPublicRoute = useCallback((pathname: string): boolean => {
    return PUBLIC_ROUTES.includes(pathname);
  }, [PUBLIC_ROUTES]);

  const isProtectedRoute = useCallback((pathname: string): boolean => {
    return !isPublicRoute(pathname);
  }, [isPublicRoute]);

  const getCurrentPath = useCallback((): string => {
    return window.location.pathname;
  }, []);

  const shouldRedirectToLogin = useCallback((isInitialMount: boolean): boolean => {
    return !isInitialMount && isProtectedRoute(getCurrentPath());
  }, [isProtectedRoute, getCurrentPath]);

  const [user, setUser] = useState<User | null>(null);
  const isInitialMount = useRef(true);
  const retryTimeoutRef = useRef<number | undefined>(undefined);

  const navigate = useNavigate();

  const redirectToLogin = useCallback(() => {
    navigate('/login', { replace: true });
  }, [navigate]);

  const redirectToHome = useCallback(() => {
    navigate('/home', { replace: true });
  }, [navigate]);

  const getToken = useCallback((): string | null => {
    return localStorage.getItem(TOKEN_KEY);
  }, [TOKEN_KEY]);

  const setToken = useCallback((token: string): void => {
    localStorage.setItem(TOKEN_KEY, token);
  }, [TOKEN_KEY]);

  const clearToken = useCallback((): void => {
    localStorage.removeItem(TOKEN_KEY);
  }, [TOKEN_KEY]);

  const resetUserState = useCallback(() => {
    clearToken();
    setUser(null);
  }, [clearToken]);

  const setUserData = useCallback((userData: User, token: string) => {
    setToken(token);
    setUser(userData);
  }, [setToken]);

  const loadUser = useCallback(async (): Promise<void> => {
    try {
      const token = getToken();

      if (!token) {
        if (shouldRedirectToLogin(isInitialMount.current)) {
          redirectToLogin();
        }
        return;
      }

      const response = await authApi.getCurrentUser();

      if (response.isAuthError) {
        if (!isPublicRoute(getCurrentPath())) {
          resetUserState();
          if (shouldRedirectToLogin(isInitialMount.current)) {
            redirectToLogin();
          }
        }
        return;
      }

      if (response.status === 'success' && response.data) {
        setUser(response.data);
      } else {
        resetUserState();
        if (shouldRedirectToLogin(isInitialMount.current)) {
          redirectToLogin();
        }
      }
    } catch (error) {
      console.error('Erreur inattendue dans loadUser:', error);
    }
  }, [getToken, resetUserState, redirectToLogin, shouldRedirectToLogin, isPublicRoute, getCurrentPath]);

  const handleStorageChange = useCallback((event: StorageEvent) => {
    if (event.key !== TOKEN_KEY || event.storageArea !== localStorage) {
      return;
    }

    const hasToken = Boolean(event.newValue);
    const isOnProtectedRoute = isProtectedRoute(getCurrentPath());

    if (!hasToken) {
      setUser(null);
      if (isOnProtectedRoute) {
        redirectToLogin();
      }
    } else if (!user && isOnProtectedRoute) {
      loadUser().catch(error => {
        console.error('Erreur lors du rechargement de l\'utilisateur:', error);
      });
    }
  }, [user, redirectToLogin, loadUser, TOKEN_KEY, isProtectedRoute, getCurrentPath]);

  const login = useCallback(async (username: string, password: string): Promise<void> => {
    try {
      const response = await authApi.login(username, password);

      if (response.status === 'success' && response.data) {
        const { accessToken, user: userData } = response.data;
        setUserData(userData, accessToken);
        redirectToHome();
      } else {
        resetUserState();
        throw new Error(response.message ?? 'Erreur de connexion');
      }
    } catch (error) {
      resetUserState();
      throw error;
    }
  }, [setUserData, redirectToHome, resetUserState]);

  const register = useCallback(async (username: string, password: string): Promise<ApiResponse<unknown>> => {
    return await authApi.register(username, password);
  }, []);

  const logout = useCallback(() => {
    resetUserState();
    redirectToLogin();
  }, [resetUserState, redirectToLogin]);

  useEffect(() => {
    const timeoutId = retryTimeoutRef.current;

    const initializeAuth = async () => {
      try {
        await loadUser();
      } catch (error) {
        console.error('Failed to initialize auth:', error);
      }
    };

    initializeAuth().finally(() => isInitialMount.current = false);

    const intervalId = setInterval(() => {
      loadUser().catch(error => {
        console.error('Erreur lors de la vérification périodique de l\'utilisateur:', error);
      });
    }, USER_CHECK_INTERVAL);

    return () => {
      if (timeoutId) {
        window.clearTimeout(timeoutId);
      }
      clearInterval(intervalId);
    };
  }, [loadUser, USER_CHECK_INTERVAL]);

  useEffect(() => {
    window.addEventListener('storage', handleStorageChange);
    return () => window.removeEventListener('storage', handleStorageChange);
  }, [handleStorageChange]);

  const contextValue: AuthContextType = useMemo(() => ({
    user,
    login,
    register,
    logout
  }), [user, login, register, logout]);

  return (
    <AuthContext.Provider value={contextValue}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth(): AuthContextType {
  const context = useContext(AuthContext);

  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }

  return context;
}
