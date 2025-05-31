import { useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { FaEye, FaEyeSlash, FaChevronDown, FaChevronUp } from 'react-icons/fa';
import { useAuth } from '../contexts/AuthContext';
import { AuthenticationError, ApiError } from '../services/http';

const Register = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { register } = useAuth();
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [showCriteria, setShowCriteria] = useState(false);
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    confirmPassword: ''
  });
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  // Récupérer l'URL de redirection si elle existe
  const from = location.state?.from?.pathname || '/';

  const validatePassword = (password: string) => {
    if (password.length < 8) {
      return 'Le mot de passe doit contenir au moins 8 caractères';
    }
    if (!/[A-Za-z]/.test(password)) {
      return 'Le mot de passe doit contenir au moins une lettre';
    }
    if (!/[0-9]/.test(password)) {
      return 'Le mot de passe doit contenir au moins un chiffre';
    }
    return '';
  };

  const isFormValid = () => {
    return (
      formData.username.trim() !== '' &&
      formData.password === formData.confirmPassword &&
      validatePassword(formData.password) === '' &&
      formData.confirmPassword !== ''
    );
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    setError(''); // Réinitialiser l'erreur lors de la saisie
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    const validationError = validatePassword(formData.password);
    if (validationError) {
      setError(validationError);
      return;
    }

    if (formData.password !== formData.confirmPassword) {
      setError('Les mots de passe ne correspondent pas');
      return;
    }

    setIsLoading(true);

    try {
      await register(formData.username, formData.password);
      navigate(from, { replace: true });
    } catch (error) {
      if (error instanceof AuthenticationError) {
        setError('Une erreur est survenue lors de l\'authentification');
      } else if (error instanceof ApiError && error.message.includes('409')) {
        setError('Ce nom d\'utilisateur est déjà pris');
      } else if (error instanceof ApiError) {
        setError(error.message);
      } else {
        setError('Une erreur est survenue lors de l\'inscription');
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-white dark:bg-gray-900">
      <div className="max-w-md w-full bg-white dark:bg-gray-800 shadow-2xl p-8">
        <div>
          <h2 className="text-center text-3xl font-extrabold text-gray-900 dark:text-white">
            Inscription
          </h2>
          <p className="mt-2 text-center text-sm text-gray-600 dark:text-gray-400">
            Créez votre compte
          </p>
        </div>
        <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
          {error && (
            <div className="bg-red-50 dark:bg-red-900/50 border-l-4 border-red-400 p-4" role="alert">
              <p className="text-sm text-red-700 dark:text-red-200">{error}</p>
            </div>
          )}
          <div className="space-y-6">
            <div>
              <label htmlFor="username" className="block text-sm font-medium text-gray-700 dark:text-gray-300">
                Nom d'utilisateur
              </label>
              <div className="mt-1">
                <input
                  id="username"
                  name="username"
                  type="text"
                  required
                  className="appearance-none block w-full px-3 py-2 border border-gray-300 dark:border-gray-600 placeholder-gray-400 dark:placeholder-gray-500 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 bg-white dark:bg-white text-gray-900 sm:text-sm"
                  placeholder="Choisissez un nom d'utilisateur"
                  value={formData.username}
                  onChange={handleChange}
                  disabled={isLoading}
                />
              </div>
            </div>
            <div>
              <label htmlFor="password" className="block text-sm font-medium text-gray-700 dark:text-gray-300">
                Mot de passe
              </label>
              <div className="mt-1 relative">
                <input
                  id="password"
                  name="password"
                  type={showPassword ? "text" : "password"}
                  required
                  className={`appearance-none block w-full px-3 py-2 border ${validatePassword(formData.password) ? 'border-red-300 dark:border-red-500' : 'border-gray-300 dark:border-gray-600'} placeholder-gray-400 dark:placeholder-gray-500 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 bg-white dark:bg-white text-gray-900 sm:text-sm pr-10`}
                  placeholder="Créez votre mot de passe"
                  value={formData.password}
                  onChange={handleChange}
                  disabled={isLoading}
                  onFocus={() => setShowCriteria(true)}
                />
                <button
                  type="button"
                  className="absolute inset-y-0 right-0 pr-3 flex items-center"
                  onClick={() => setShowPassword(!showPassword)}
                  disabled={isLoading}
                >
                  {showPassword ? (
                    <FaEyeSlash className="h-5 w-5 text-gray-400 hover:text-gray-500" />
                  ) : (
                    <FaEye className="h-5 w-5 text-gray-400 hover:text-gray-500" />
                  )}
                </button>
              </div>
            </div>
            <div>
              <label htmlFor="confirmPassword" className="block text-sm font-medium text-gray-700 dark:text-gray-300">
                Confirmer le mot de passe
              </label>
              <div className="mt-1 relative">
                <input
                  id="confirmPassword"
                  name="confirmPassword"
                  type={showConfirmPassword ? "text" : "password"}
                  required
                  className={`appearance-none block w-full px-3 py-2 border ${formData.confirmPassword && formData.password !== formData.confirmPassword ? 'border-red-300 dark:border-red-500' : 'border-gray-300 dark:border-gray-600'} placeholder-gray-400 dark:placeholder-gray-500 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 bg-white dark:bg-white text-gray-900 sm:text-sm pr-10`}
                  placeholder="Confirmez votre mot de passe"
                  value={formData.confirmPassword}
                  onChange={handleChange}
                  disabled={isLoading}
                />
                <button
                  type="button"
                  className="absolute inset-y-0 right-0 pr-3 flex items-center"
                  onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                  disabled={isLoading}
                >
                  {showConfirmPassword ? (
                    <FaEyeSlash className="h-5 w-5 text-gray-400 hover:text-gray-500" />
                  ) : (
                    <FaEye className="h-5 w-5 text-gray-400 hover:text-gray-500" />
                  )}
                </button>
              </div>
              {formData.confirmPassword && formData.password !== formData.confirmPassword && (
                <p className="mt-2 text-sm text-red-600 dark:text-red-400">
                  Les mots de passe ne correspondent pas
                </p>
              )}
            </div>
          </div>

          <div>
            <button
              type="submit"
              className="w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed transition-colors duration-200"
              disabled={isLoading || !isFormValid()}
            >
              {isLoading ? (
                <span className="flex items-center">
                  <svg className="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                    <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                    <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                  </svg>
                  Inscription en cours...
                </span>
              ) : (
                'S\'inscrire'
              )}
            </button>
          </div>

          <div className="flex flex-col items-center space-y-4 w-full">
            <Link
              to="/login"
              className="text-sm font-medium text-indigo-600 hover:text-indigo-500 dark:text-indigo-400 dark:hover:text-indigo-300 transition-colors duration-200"
              tabIndex={isLoading ? -1 : 0}
            >
              Déjà inscrit ? Se connecter
            </Link>

            <div className="w-full border-t border-gray-200 dark:border-gray-700 my-4"></div>

            <button
              type="button"
              className="w-full text-sm text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-300 flex items-center justify-center"
              onClick={() => setShowCriteria(!showCriteria)}
              disabled={isLoading}
            >
              {showCriteria ? <FaChevronUp className="h-3 w-3 mr-1" /> : <FaChevronDown className="h-3 w-3 mr-1" />}
              <span>Critères du mot de passe</span>
            </button>

            {showCriteria && (
              <div className="w-full bg-gray-50 dark:bg-gray-700/50 px-4 py-3 rounded-sm border-l-2 border-indigo-500 dark:border-indigo-400">
                <ul className="space-y-1.5 text-xs">
                  <li className={`flex items-center ${formData.password.length >= 8 ? 'text-green-500 dark:text-green-400' : 'text-gray-500 dark:text-gray-400'}`}>
                    <span className="mr-2">{formData.password.length >= 8 ? '✓' : '•'}</span>
                    8 caractères minimum
                  </li>
                  <li className={`flex items-center ${/[A-Za-z]/.test(formData.password) ? 'text-green-500 dark:text-green-400' : 'text-gray-500 dark:text-gray-400'}`}>
                    <span className="mr-2">{/[A-Za-z]/.test(formData.password) ? '✓' : '•'}</span>
                    Au moins une lettre
                  </li>
                  <li className={`flex items-center ${/[0-9]/.test(formData.password) ? 'text-green-500 dark:text-green-400' : 'text-gray-500 dark:text-gray-400'}`}>
                    <span className="mr-2">{/[0-9]/.test(formData.password) ? '✓' : '•'}</span>
                    Au moins un chiffre
                  </li>
                </ul>
              </div>
            )}
          </div>
        </form>
      </div>
    </div>
  );
};

export default Register; 