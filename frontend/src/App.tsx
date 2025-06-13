import { BrowserRouter } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import { ToastProvider } from './contexts/ToastContext';
import AppRoutes from './routes';
import { PrimeReactProvider } from 'primereact/api';
import { PRIMEREACT_CONFIG } from './config/primereact';

// Import PrimeReact styles dans l'ordre correct
import 'primereact/resources/themes/lara-light-indigo/theme.css';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';

import './styles/variables.css';
import './styles/main.css';
import './styles/primereact.css';

const App = () => {
  return (
    <PrimeReactProvider value={PRIMEREACT_CONFIG}>
      <BrowserRouter>
        <ToastProvider>
          <AuthProvider>
            <AppRoutes />
          </AuthProvider>
        </ToastProvider>
      </BrowserRouter>
    </PrimeReactProvider>
  );
};

export default App;