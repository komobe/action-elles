import { InputField, PasswordField, SubmitButton } from '@/components/form';
import React, { useCallback, useState } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '@/hooks/useAuth';
import { Message } from 'primereact/message';
import { HttpError } from "@services/http/ http-error.ts";

interface FormData {
  username: string;
  password: string;
}

const Login = () => {
  const { login } = useAuth();
  const [formData, setFormData] = useState<FormData>({
    username: '',
    password: ''
  });
  const [error, setError] = useState<string>('');
  const [isLoading, setIsLoading] = useState(false);
  const [isFormValid, setIsFormValid] = useState(false);
  const [genericError, setGenericError] = useState<string>('');

  const handleInputChange = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => {
      const newData = { ...prev, [name]: value };
      setIsFormValid(newData.username.trim() !== '' && newData.password.trim() !== '');
      return newData;
    });
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!isFormValid) return;

    setIsLoading(true);
    setError('');

    try {
      await login({ username: formData.username, password: formData.password });
    } catch (error: unknown) {
      if (error instanceof HttpError) {
        setError(error.message);
        setGenericError(error.message)
      } else {
        setError('Nom d\'utilisateur ou mot de passe incorrect');
        setGenericError('Nom d\'utilisateur ou mot de passe incorrect')
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-container">
        <div className="auth-header">
          <h1 className="auth-title">Bienvenue</h1>
          <p className="auth-subtitle">Connectez-vous à votre compte</p>
        </div>

        <div className="app-form-fieldset">
          <form onSubmit={handleSubmit} className="app-form">
            {genericError && (
              <Message className="!w-full" severity="error" text={genericError} />
            )}

            {error && (
              <div className="error-message" role="alert">
                <p>{error}</p>
              </div>
            )}

            <div className="space-y-6">
              <InputField
                id="username"
                name="username"
                label="Nom d'utilisateur"
                value={formData.username}
                placeholder="Entrez votre nom d'utilisateur"
                disabled={isLoading}
                onChange={handleInputChange}
                required
              />

              <PasswordField
                id="password"
                name="password"
                label="Mot de passe"
                value={formData.password}
                placeholder="Entrez votre mot de passe"
                disabled={isLoading}
                onChange={handleInputChange}
                required
              />
            </div>

            <div className="flex flex-col sm:flex-row items-center sm:justify-between w-full">
              <SubmitButton
                isDisabled={isLoading || !isFormValid}
                isLoading={isLoading}
                label="Se connecter"
                className="w-full sm:w-auto"
                isPrimary
              />
              <div className="auth-link-container mt-4 sm:mt-0 w-full sm:flex-1 text-center flex flex-col items-center">
                <span className="auth-link-text">Pas encore de compte ?</span>
                <Link to="/register" className="auth-link-button">
                  Inscrivez-vous
                </Link>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Login;