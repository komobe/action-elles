import {
  ReactNode,
  useCallback,
  useEffect,
  useMemo,
  useRef,
  useState
} from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { configureHttpClient } from '@/services/http/http-client';
import { authHttpService, UserInfo } from '@/services/auth.http-service';
import { HttpError } from '@/services/http/ http-error';
import { AuthContext, Credentials } from './auth-context';

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
      if (error instanceof HttpError) {
        // Si l'erreur est une erreur HTTP (401, 403, etc.), on déconnecte l'utilisateur
        console.warn('Erreur HTTP lors du chargement de l\'utilisateur:', error.message);
      }
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
    return await authHttpService.register({ username, password });
  }, []);

  const logout = useCallback(() => {
    clearAuth();
    redirectToLogin();
  }, [clearAuth, redirectToLogin]);

  useEffect(() => {
    const initialize = async () => {
      await loadUser();
      isInitialMount.current = false;
    };

    initialize();
  }, [loadUser]);

  // Configurer la vérification périodique de l'utilisateur uniquement lorsque l'utilisateur est authentifié
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

// Déplacer useAuth dans un fichier séparé