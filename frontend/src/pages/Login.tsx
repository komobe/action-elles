import { InputField, PasswordField } from '@/components/form';
import { Button } from 'primereact/button';
import React, { useCallback, useState } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { Message } from 'primereact/message';

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
    } catch (err) {
      setError('Nom d\'utilisateur ou mot de passe incorrect');
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

            <div className="pt-4 flex flex-col sm:flex-row items-center sm:justify-between w-full">
              <Button
                type="submit"
                label="Se connecter"
                className="app-form-button-primary w-full sm:w-auto"
                disabled={isLoading || !isFormValid}
                loading={isLoading}
              />
              <div className="auth-link-container sm:text-right mt-4 sm:mt-0">
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