import React, {useCallback, useState} from 'react';
import {Link} from 'react-router-dom';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faEye, faEyeSlash} from '@fortawesome/free-solid-svg-icons';
import {useAuth} from '../contexts/AuthContext';

interface FormData {
  username: string;
  password: string;
}

const INITIAL_FORM_DATA: FormData = {
  username: '',
  password: ''
};

const ERROR_MESSAGES = {
  INVALID_CREDENTIALS: 'Identifiants invalides',
  GENERIC_ERROR: 'Une erreur est survenue lors de la connexion',
  NETWORK_ERROR: 'Erreur de connexion. Vérifiez votre connexion internet.'
};

const hasAllRequiredFields = (formData: FormData): boolean => {
  return formData.username.trim() !== '' && formData.password.trim() !== '';
};

const getErrorMessage = (error: unknown): string => {
  if (error instanceof Error) {
    // Vérifier si c'est une erreur de credentials
    if (error.message.toLowerCase().includes('credentials') ||
        error.message.toLowerCase().includes('identifiants') ||
        error.message.toLowerCase().includes('invalid') ||
        error.message.toLowerCase().includes('invalide')) {
      return ERROR_MESSAGES.INVALID_CREDENTIALS;
    }
    return error.message;
  }
  return ERROR_MESSAGES.GENERIC_ERROR;
};

const Login = () => {
  const {login} = useAuth();
  const [formData, setFormData] = useState<FormData>(INITIAL_FORM_DATA);
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const clearError = useCallback(() => {
    setError('');
  }, []);

  const handleInputChange = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
    const {name, value} = e.target;
    setFormData(prev => ({...prev, [name]: value}));

    if (error) {
      clearError();
    }
  }, [error, clearError]);

  const handleSubmit = useCallback(async (e: React.FormEvent) => {
    e.preventDefault();
    clearError();

    if (!hasAllRequiredFields(formData)) {
      setError('Veuillez remplir tous les champs');
      return;
    }

    setIsLoading(true);

    try {
      await login(formData.username.trim(), formData.password);
      // La redirection est gérée par AuthContext
    } catch (error) {
      console.error("Erreur de connexion:", error);
      const errorMessage = getErrorMessage(error);
      setError(errorMessage);
    } finally {
      setIsLoading(false);
    }
  }, [formData, login, clearError]);

  const togglePasswordVisibility = useCallback(() => {
    setShowPassword(prev => !prev);
  }, []);

  const isFormValid = hasAllRequiredFields(formData);

  return (
      <div
          className="min-h-screen flex items-center justify-center bg-gradient-to-br from-indigo-50 to-white dark:from-gray-900 dark:to-gray-800 px-4 sm:px-6 lg:px-8">
        <div className="max-w-md w-full">
          <div className="text-center mb-10">
            <h1 className="text-4xl font-bold text-gray-900 dark:text-white mb-2">
              Bienvenue
            </h1>
            <p className="text-gray-600 dark:text-gray-400">
              Connectez-vous à votre compte
            </p>
          </div>

          <div
              className="bg-white dark:bg-gray-800 shadow-xl rounded-2xl p-8 space-y-8 transition-all duration-300 hover:shadow-2xl">
            <form className="space-y-6" onSubmit={handleSubmit}>
              {error && (
                  <div
                      className="bg-red-50 dark:bg-red-900/50 border-l-4 border-red-400 p-4 rounded-r-lg animate-fadeIn"
                      role="alert">
                    <p className="text-sm text-red-700 dark:text-red-200">{error}</p>
                  </div>
              )}

              <div className="space-y-6">
                <div className="group">
                  <label htmlFor="username"
                         className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                    Nom d'utilisateur
                  </label>
                  <div className="relative">
                    <input
                        id="username"
                        name="username"
                        type="text"
                        required
                        className="block w-full px-4 py-3 rounded-lg border border-gray-300 dark:border-gray-600 focus:ring-2 focus:ring-indigo-500 focus:border-transparent bg-white dark:bg-gray-800 text-gray-900 dark:text-white text-sm transition-all duration-200 ease-in-out transform hover:shadow-sm focus:shadow-md outline-none"
                        placeholder="Entrez votre nom d'utilisateur"
                        value={formData.username}
                        onChange={handleInputChange}
                        disabled={isLoading}
                        autoComplete="username"
                    />
                  </div>
                </div>

                <div className="group">
                  <label htmlFor="password"
                         className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                    Mot de passe
                  </label>
                  <div className="relative">
                    <input
                        id="password"
                        name="password"
                        type={showPassword ? "text" : "password"}
                        required
                        className="block w-full px-4 py-3 rounded-lg border border-gray-300 dark:border-gray-600 focus:ring-2 focus:ring-indigo-500 focus:border-transparent bg-white dark:bg-gray-800 text-gray-900 dark:text-white text-sm transition-all duration-200 ease-in-out transform hover:shadow-sm focus:shadow-md outline-none pr-12"
                        placeholder="Entrez votre mot de passe"
                        value={formData.password}
                        onChange={handleInputChange}
                        disabled={isLoading}
                        autoComplete="current-password"
                    />
                    <button
                        type="button"
                        className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 dark:hover:text-gray-300 transition-colors duration-200 w-10 h-10 flex items-center justify-center bg-transparent border-none p-0"
                        onClick={togglePasswordVisibility}
                        disabled={isLoading}
                        tabIndex={-1}
                        aria-label={showPassword ? "Masquer le mot de passe" : "Afficher le mot de passe"}
                    >
                      <FontAwesomeIcon
                          icon={showPassword ? faEyeSlash : faEye}
                          className="h-5 w-5"
                      />
                    </button>
                  </div>
                </div>
              </div>

              <div className="pt-2">
                <button
                    type="submit"
                    className="w-full flex justify-center items-center px-6 py-3 rounded-lg text-base font-semibold text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-all duration-300 ease-in-out transform hover:-translate-y-0.5 hover:shadow-lg disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:transform-none disabled:hover:shadow-none"
                    disabled={isLoading || !isFormValid}
                >
                  {isLoading ? (
                      <span className="flex items-center space-x-3">
                    <svg className="animate-spin h-5 w-5" xmlns="http://www.w3.org/2000/svg"
                         fill="none" viewBox="0 0 24 24">
                      <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor"
                              strokeWidth="4"/>
                      <path className="opacity-75" fill="currentColor"
                            d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"/>
                    </svg>
                    <span>Connexion en cours...</span>
                  </span>
                  ) : (
                      'Se connecter'
                  )}
                </button>
              </div>

              <div className="text-center mt-8">
                <Link
                    to="/register"
                    className="inline-flex items-center text-sm font-medium text-indigo-600 hover:text-indigo-700 dark:text-indigo-400 dark:hover:text-indigo-300 transition-all duration-200 hover:-translate-y-0.5"
                    tabIndex={isLoading ? -1 : 0}
                >
                  {'Pas encore de compte ?'}
                  <span className="ml-1 group-hover:ml-2 transition-all duration-200">
                  S'inscrire
                </span>
                </Link>
              </div>
            </form>
          </div>
        </div>
      </div>
  );
};

export default Login;