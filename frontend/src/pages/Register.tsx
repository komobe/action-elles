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
  const [isFormValid, setIsFormValid] = useState(false);
  const [touched, setTouched] = useState<{ [K in keyof FormData]?: boolean }>({});

  useEffect(() => {
    return () => {
      if (timeoutRef.current) {
        clearTimeout(timeoutRef.current);
      }
    };
  }, []);

  const validateForm = (): Partial<FormData> => {
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

    return newErrors;
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
    setTouched(prev => ({ ...prev, [name]: true }));
    setGenericError('');
  };

  useEffect(() => {
    const errors = validateForm();
    setErrors(errors);
    setIsFormValid(Object.keys(errors).length === 0);
  }, [formData]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!validateForm()) {
      return;
    }

    setIsLoading(true);
    setGenericError('');

    try {
      await register({ username: formData.username, password: formData.password });
      setIsSuccess(true);
      timeoutRef.current = setTimeout(() => {
        navigate('/login');
      }, 2000);
    } catch (error: any) {
      const message = error.message ?? 'Une erreur est survenue lors de l\'inscription';
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
                error={touched.username ? errors.username : ''}
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
                error={touched.password ? errors.password : ''}
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
                error={touched.confirmPassword ? errors.confirmPassword : ''}
              />
            </div>

            <div className="flex flex-col sm:flex-row items-center sm:justify-between w-full">
              <SubmitButton
                isDisabled={isLoading || !isFormValid}
                isLoading={isLoading}
                label="S'inscrire"
                className="w-full sm:w-auto"
                isPrimary
              />
              <div className="auth-link-container mt-4 sm:mt-0 w-full sm:flex-1 text-center flex flex-col items-center">
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