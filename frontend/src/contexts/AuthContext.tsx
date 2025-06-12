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
import {useNavigate} from 'react-router-dom';
import {authApi} from '@services/api';
import {ApiResponse} from "@services/http.ts";

interface User {
  username: string;
  roles: string[];
}

interface AuthContextType {
  user: User | null;
  isLoading: boolean;
  login: (username: string, password: string) => Promise<void>;
  register: (username: string, password: string) => Promise<ApiResponse<unknown>>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({children}: { readonly children: ReactNode }) {
  // Constantes déplacées dans le composant
  const TOKEN_KEY = 'token';
  const USER_CHECK_INTERVAL = 10 * 60 * 1000;
  const PUBLIC_ROUTES = useMemo(() => ['/login', '/register'], []);

  // Helper functions
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

  // State
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  // Refs
  const isInitialMount = useRef(true);
  const retryTimeoutRef = useRef<number | undefined>(undefined);

  // Navigation
  const navigate = useNavigate();

  const redirectToLogin = useCallback(() => {
    navigate('/login', {replace: true});
  }, [navigate]);

  const redirectToHome = useCallback(() => {
    navigate('/home', {replace: true});
  }, [navigate]);

  // Token management
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
    setIsLoading(false);
  }, [clearToken]);

  const setUserData = useCallback((userData: User, token: string) => {
    setToken(token);
    setUser(userData);
    setIsLoading(false);
  }, [setToken]);

  // User loading
  const loadUser = useCallback(async (): Promise<void> => {
    try {
      const token = getToken();

      if (!token) {
        setIsLoading(false);
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
        } else {
          setIsLoading(false);
        }
        return;
      }

      if (response.status === 'success' && response.data) {
        setUser(response.data);
        setIsLoading(false);
      } else {
        resetUserState();
        if (shouldRedirectToLogin(isInitialMount.current)) {
          redirectToLogin();
        }
      }
    } catch (error) {
      console.error('Erreur inattendue dans loadUser:', error);
      setIsLoading(false);
    }
  }, [getToken, resetUserState, redirectToLogin, shouldRedirectToLogin, isPublicRoute, getCurrentPath]);

  // Storage change handler
  const handleStorageChange = useCallback((event: StorageEvent) => {
    if (event.key !== TOKEN_KEY || event.storageArea !== localStorage) {
      return;
    }

    const hasToken = Boolean(event.newValue);
    const isOnProtectedRoute = isProtectedRoute(getCurrentPath());

    if (!hasToken) {
      setUser(null);
      setIsLoading(false);
      if (isOnProtectedRoute) {
        redirectToLogin();
      }
    } else if (!user && isOnProtectedRoute) {
      loadUser().catch(error => {
        console.error('Erreur lors du rechargement de l\'utilisateur:', error);
      });
    }
  }, [user, redirectToLogin, loadUser, TOKEN_KEY, isProtectedRoute, getCurrentPath]);

  // Auth actions
  const login = useCallback(async (username: string, password: string): Promise<void> => {
    try {
      setIsLoading(true);
      const response = await authApi.login(username, password);

      if (response.status === 'success' && response.data) {
        const {accessToken, user: userData} = response.data;
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
    setIsLoading(true);
    try {
      const response = await authApi.register(username, password);
      setIsLoading(false);
      return response;
    } catch (error) {
      setIsLoading(false);
      throw error;
    }
  }, []);

  const logout = useCallback(() => {
    resetUserState();
    redirectToLogin();
  }, [resetUserState, redirectToLogin]);

  // Effects
  useEffect(() => {
    // Capture ref value at the start of effect to avoid stale closure
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

  // Context value
  const contextValue: AuthContextType = useMemo(() => ({
    user,
    isLoading,
    login,
    register,
    logout
  }), [user, isLoading, login, register, logout]);

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