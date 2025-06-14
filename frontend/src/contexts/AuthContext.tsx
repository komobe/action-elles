import {HttpResponse} from '@/services/http/response.type';
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
import {useLocation, useNavigate} from 'react-router-dom';
import {configureHttpClient} from '@/services/http/http-client';
import {authHttpService, UserInfo} from '@/services/auth.http-service';


export interface Credentials {
  username: string;
  password: string;
}
interface AuthContextType {
  user: UserInfo | null;
  isLoading: boolean;
  login: (credentials: Credentials) => Promise<void>;
  register: (credentials: Credentials) => Promise<HttpResponse<unknown>>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

const USER_CHECK_INTERVAL = 10 * 60 * 1000; // 10 minutes
const LOGIN_PAGE = '/login';
const REGISTER_PAGE = '/register';
const PUBLIC_ROUTES = [LOGIN_PAGE, REGISTER_PAGE];

export function AuthProvider({ children }: { readonly children: ReactNode }) {
  const [user, setUser] = useState<UserInfo | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [token, setToken] = useState<string | null>(() => {
    return localStorage.getItem('token');
  });
  const isInitialMount = useRef(true);
  const navigate = useNavigate();
  const location = useLocation();

  // Configure httpClient pour recuperer le token 
  useEffect(() => {
    configureHttpClient({
      getToken: () => token,
      removeToken: () => {
        localStorage.removeItem('token');
        setToken(null);
      }
    });
  }, [token]);

  const isPublicRoute = useCallback((pathname: string): boolean => {
    return PUBLIC_ROUTES.includes(pathname);
  }, []);

  const isLoginPage = useCallback((pathname: string): boolean => {
    return LOGIN_PAGE === pathname
  }, [])

  const clearAuth = useCallback(() => {
    localStorage.removeItem('token');
    setToken(null);
    setUser(null);
  }, []);

  const redirectToLogin = useCallback(() => {
    navigate('/login', { replace: true });
  }, [navigate]);

  const redirectToHome = useCallback(() => {
    navigate('/home', { replace: true });
  }, [navigate]);

  const loadUser = useCallback(async (): Promise<void> => {
    try {
      if (!token) {
        clearAuth();
        return;
      }

      const response = await authHttpService.getCurrentUser();
      if (response.data) {
        setUser(response.data);
      }
    } catch (error) {
      clearAuth();
    } finally {
      setIsLoading(false);
    }
  }, [token, clearAuth]);

  useEffect(() => {
    if (isInitialMount.current || isLoading) return;

    // Si on a un token mais pas d'utilisateur, on attend encore le chargement
    if (token && !user) {
      return;
    }

    // Rediriger vers login seulement s'il n'y a ni token ni utilisateur ET qu'on n'est pas sur une route publique
    if (!token && !user && !isPublicRoute(location.pathname)) {
      redirectToLogin();
    }
    // Rediriger vers home si on a un utilisateur ET un token ET qu'on veut aller sur la page login
    else if (user && token && isLoginPage(location.pathname)) {
      redirectToHome();
    }
  }, [user, token, location.pathname, isPublicRoute, redirectToLogin, redirectToHome, isLoading, isLoginPage]);

  const login = useCallback(async ({ username, password }: Credentials): Promise<void> => {
    try {
      setIsLoading(true);
      const response = await authHttpService.login(username, password);

      if (response.status === 'success' && response.data) {
        const { accessToken, user: userData } = response.data;
        localStorage.setItem('token', accessToken);
        setToken(accessToken);
        setUser(userData);
        redirectToHome();
      } else {
        clearAuth();
        throw new Error(response.message ?? 'Erreur de connexion');
      }
    } catch (error) {
      clearAuth();
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [clearAuth, redirectToHome]);

  const register = useCallback(async ({ username, password }: Credentials) => {
    return await authHttpService.register(username, password);
  }, []);

  const logout = useCallback(() => {
    clearAuth();
    redirectToLogin();
  }, [clearAuth, redirectToLogin]);

  // Initialize auth state - only runs once on mount
  useEffect(() => {
    const initialize = async () => {
      await loadUser();
      isInitialMount.current = false;
    };

    initialize();
  }, []); // Empty dependency array - only runs once

  // Set up periodic user check only when user is authenticated
  useEffect(() => {
    if (!user || !token) return;

    const intervalId = setInterval(loadUser, USER_CHECK_INTERVAL);
    return () => clearInterval(intervalId);
  }, [user, token, loadUser]);

  const contextValue = useMemo(() =>
    ({ user, isLoading, login, register, logout }),
    [user, isLoading, login, register, logout]
  );

  return (
    <AuthContext.Provider value={contextValue}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth(): AuthContextType {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}