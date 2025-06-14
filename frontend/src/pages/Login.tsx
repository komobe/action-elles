import LabeledInput from '@/components/form/LabeledInput';
import LabeledPassword from '@/components/form/LabeledPassword';
import {Button} from 'primereact/button';
import React, {useCallback, useState} from 'react';
import {Link} from 'react-router-dom';
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
  const { login } = useAuth();
  const [formData, setFormData] = useState<FormData>(INITIAL_FORM_DATA);
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const clearError = useCallback(() => setError(''), []);

  const handleInputChange = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));

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
      await login({ ...formData });
    } catch (error) {
      console.error("Erreur de connexion:", error);
      const errorMessage = getErrorMessage(error);
      setError(errorMessage);
    } finally {
      setIsLoading(false);
    }
  }, [formData, login, clearError]);

  const isFormValid = hasAllRequiredFields(formData);

  return (
    <div className="auth-page">
      <div className="auth-container">

        {/* Header */}
        <div className="auth-header">
          <h1 className="auth-title">Bienvenue</h1>
          <p className="auth-subtitle">Connectez-vous à votre compte</p>
        </div>

        {/* Form Container */}
        <div className="card">
          <form className="auth-form" onSubmit={handleSubmit}>

            {/* Error Message */}
            {error && (
              <div className="bg-red-50 dark:bg-red-900/50 border-l-4 border-red-400 p-4" role="alert">
                <p className="text-sm text-red-700 dark:text-red-200">{error}</p>
              </div>
            )}

            {/* Form Fields */}
            <div className="space-y-6">

              {/* Username Field */}
              <LabeledInput
                id="username"
                name="username"
                label="Nom d'utilisateur"
                value={formData.username}
                placeholder="Entrez votre nom d'utilisateur"
                disabled={isLoading}
                className='auth-form-group'
                onChange={handleInputChange}
              />

              <LabeledPassword
                id="password"
                name="password"
                label="Mot de passe"
                value={formData.password}
                placeholder="Entrez votre mot de passe"
                disabled={isLoading}
                className="auth-form-group"
                onChange={handleInputChange}
              />
            </div>

            {/* Submit Button */}
            <div className="pt-4">
              <Button
                type="submit"
                label="Se connecter"
                className="w-full"
                disabled={isLoading || !isFormValid}
                loading={isLoading}
              />
            </div>

            {/* Register Link */}
            <div className="auth-link">
              <Link to="/register" tabIndex={isLoading ? -1 : 0}>
                Pas encore de compte ? S'inscrire
              </Link>
            </div>

          </form>
        </div>
      </div>
    </div>
  );
};

export default Login;