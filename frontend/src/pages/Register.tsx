import { InputField, PasswordField, SubmitButton } from '@/components/form';
import { Message } from 'primereact/message';
import React, { useEffect, useRef, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

type FormData = {
  username: string;
  password: string;
  confirmPassword: string;
};

const Register = () => {
  const { register } = useAuth();
  const navigate = useNavigate();
  const [formData, setFormData] = useState<FormData>({
    username: '',
    password: '',
    confirmPassword: ''
  });
  const [errors, setErrors] = useState<Partial<FormData>>({});
  const [genericError, setGenericError] = useState<string>('');
  const [isLoading, setIsLoading] = useState(false);
  const [isSuccess, setIsSuccess] = useState(false);
  const timeoutRef = useRef<NodeJS.Timeout>(null);

  useEffect(() => {
    return () => {
      if (timeoutRef.current) {
        clearTimeout(timeoutRef.current);
      }
    };
  }, []);

  const validateForm = (): boolean => {
    const newErrors: Partial<FormData> = {};

    if (!formData.username.trim()) {
      newErrors.username = 'Le nom d\'utilisateur est requis';
    }

    if (!formData.password) {
      newErrors.password = 'Le mot de passe est requis';
    } else if (formData.password.length < 6) {
      newErrors.password = 'Le mot de passe doit contenir au moins 6 caractères';
    }

    if (!formData.confirmPassword) {
      newErrors.confirmPassword = 'La confirmation du mot de passe est requise';
    } else if (formData.password !== formData.confirmPassword) {
      newErrors.confirmPassword = 'Les mots de passe ne correspondent pas';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
    setGenericError('');
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!validateForm()) {
      return;
    }

    setIsLoading(true);
    setGenericError('');

    try {
      console.log(formData)
      await register({ username: formData.username, password: formData.password });
      setIsSuccess(true);
      timeoutRef.current = setTimeout(() => {
        navigate('/login');
      }, 2000);
    } catch (error) {
      const message = (error as any).message ?? 'Une erreur est survenue lors de l\'inscription'
      setGenericError(message);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-container">
        <div className="auth-header">
          <h1 className="auth-title">Bienvenue</h1>
          <p className="auth-subtitle">Créez votre compte pour commencer</p>
        </div>

        <div className="app-form-fieldset">
          <form className="app-form" onSubmit={handleSubmit}>
            {genericError && (
              <Message className="!w-full" severity="error" text={genericError} />
            )}
            {isSuccess && (
              <Message
                className="!w-full"
                severity="success"
                text="Inscription réussie ! Redirection en cours..."
              />
            )}

            <div className="space-y-6">
              <InputField
                id="username"
                name="username"
                label="Nom d'utilisateur"
                value={formData.username}
                onChange={handleChange}
                required
                error={errors.username}
              />

              <PasswordField
                id="password"
                name="password"
                label="Mot de passe"
                placeholder="Entrez un mot de passe"
                value={formData.password}
                onChange={handleChange}
                disabled={isLoading}
                required
                error={errors.password}
              />

              <PasswordField
                id="confirmPassword"
                name="confirmPassword"
                label="Confirmer le mot de passe"
                placeholder="Répétez le mot de passe"
                value={formData.confirmPassword}
                onChange={handleChange}
                disabled={isLoading}
                required
                error={errors.confirmPassword}
              />
            </div>

            <div className="pt-4 flex flex-col sm:flex-row items-center sm:justify-between w-full">
              <SubmitButton
                isDisabled={isLoading}
                isLoading={isLoading}
                label="S'inscrire"
                isPrimary
              />
              <div className="auth-link-container mt-4 sm:mt-0 sm:text-right">
                <span className="auth-link-text">Déjà un compte ?</span>
                <Link to="/login" className="auth-link-button">
                  Connectez-vous
                </Link>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Register;