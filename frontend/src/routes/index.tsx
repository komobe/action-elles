import ListerSouscriptions from "@/pages/souscriptions/ListerSouscriptions";
import ListerUtilisateurs from "@/pages/users/ListerUtilisateurs";
import { Navigate, Route, Routes, useLocation } from 'react-router-dom';
import Layout from '../components/Layout';
import { useAuth } from '../contexts/AuthContext';
import About from '../pages/About';
import Home from '../pages/Home';
import Login from '../pages/Login';
import Register from '../pages/Register';
import SimulerDevis from '@pages/devis/SimulerDevis.tsx';
import CreerSouscription from '../pages/souscriptions/CreerSouscription';
import { memo } from 'react';

const PrivateRoute = memo(({ children }: { children: React.ReactNode }) => {
  const { user, isLoading } = useAuth();
  const location = useLocation();

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50 dark:bg-gray-900">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-indigo-500"></div>
      </div>
    );
  }

  if (!user) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  return <>{children}</>;
});

const PublicRoute = memo(({ children }: { children: React.ReactNode }) => {
  const { user, isLoading } = useAuth();
  const location = useLocation();

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50 dark:bg-gray-900">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-indigo-500"></div>
      </div>
    );
  }

  if (user) {
    const from = location.state?.from?.pathname || '/home';
    return <Navigate to={from} replace />;
  }

  return <>{children}</>;
});

const AppRoutes = memo(() => {
  const { user, isLoading } = useAuth();

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50 dark:bg-gray-900">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-indigo-500"></div>
      </div>
    );
  }

  return (
    <Routes>
      {/* Routes publiques */}
      <Route path="/login" element={<PublicRoute><Login /></PublicRoute>} />
      <Route path="/register" element={<PublicRoute><Register /></PublicRoute>} />

      {/* Routes protégées avec Layout */}
      <Route path="/" element={<PrivateRoute><Layout /></PrivateRoute>}>
        <Route index element={<Navigate to="/home" replace />} />
        <Route path="/home" element={<Home />} />
        <Route path="/about" element={<About />} />
        <Route path="/simuler-devis" element={<SimulerDevis />} />
        <Route path="/souscription/creer" element={<CreerSouscription />} />
        <Route path="/utilisateurs" element={<ListerUtilisateurs />} />
        <Route path="/souscriptions" element={<ListerSouscriptions />} />
        <Route path="*" element={<Navigate to="/home" replace />} />
      </Route>

      {/* Redirection par défaut */}
      <Route path="*" element={<Navigate to={user ? "/home" : "/login"} replace />} />
    </Routes>
  );
});

// Ajout des displayNames pour le débogage
PrivateRoute.displayName = 'PrivateRoute';
PublicRoute.displayName = 'PublicRoute';
AppRoutes.displayName = 'AppRoutes';

export default AppRoutes; 