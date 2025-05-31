import { BrowserRouter } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import { ToastProvider } from './contexts/ToastContext';
import AppRoutes from './routes';
import { PrimeReactProvider } from 'primereact/api';
import { PRIMEREACT_CONFIG } from './config/primereact';

// Import PrimeReact styles
import 'primereact/resources/themes/lara-light-indigo/theme.css';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';

// Import custom styles
import './styles/primereact-overrides.css';
import './styles/no-shadows.css';
import './styles/no-transitions.css';
import './styles/global.css';

// Add CSS variables
const styleVariables = `
:root {
  --primary-color: #4f46e5;
  --primary-color-text: #ffffff;
  --primary-200: #818cf8;
}
`;

const App = () => {
  return (
    <PrimeReactProvider value={PRIMEREACT_CONFIG}>
      <style>{styleVariables}</style>
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
