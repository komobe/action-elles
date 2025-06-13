import LabeledInput from '@/components/form/LabeledInput';
import LabeledPassword from '@/components/form/LabeledPassword';
import SubmitButton from '@/components/form/SubmitButton';
import { Message } from 'primereact/message';
import React, { useEffect, useRef, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

type FormData = {
  username: string;
  password: string;
  confirmPassword: string;
};

const INITIAL_FORM_DATA: FormData = {
  username: '',
  password: '',
  confirmPassword: ''
};

const Register = () => {
  const { register } = useAuth();
  const navigate = useNavigate();
  const redirectTimerRef = useRef<NodeJS.Timeout | null>(null);

  const [formData, setFormData] = useState<FormData>(INITIAL_FORM_DATA);
  const [genericError, setGenericError] = useState('');
  const [errors, setErrors] = useState<Partial<Record<keyof FormData, string>>>({});
  const [isSuccess, setIsSuccess] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    return () => {
      if (redirectTimerRef.current) {
        clearTimeout(redirectTimerRef.current);
      }
    };
  }, []);

  const validateField = (name: keyof FormData, value: string): string | undefined => {
    switch (name) {
      case 'username':
        if (!value.trim()) return "Le nom d'utilisateur est requis";
        break;
      case 'password':
        if (value.length < 6) return "Le mot de passe doit contenir au moins 6 caractères";
        if (formData.confirmPassword && value !== formData.confirmPassword) {
          setErrors(prev => ({ ...prev, confirmPassword: "Les mots de passe ne correspondent pas" }));
        } else {
          setErrors(prev => ({ ...prev, confirmPassword: undefined }));
        }
        break;
      case 'confirmPassword':
        if (value !== formData.password) return "Les mots de passe ne correspondent pas";
        break;
    }
    return undefined;
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
    const errorMessage = validateField(name as keyof FormData, value);
    setErrors(prev => ({ ...prev, [name]: errorMessage }));
  };

  const isFormCurrentlyValid = formData.username.trim() !== ''
    && formData.password.length >= 6
    && formData.password === formData.confirmPassword;

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setGenericError('');
    setIsSuccess(false);

    const newErrors: Partial<Record<keyof FormData, string>> = {};
    if (!formData.username.trim()) newErrors.username = "Le nom d'utilisateur est requis";
    if (formData.password.length < 6) newErrors.password = "Le mot de passe doit contenir au moins 6 caractères";
    if (formData.password !== formData.confirmPassword) newErrors.confirmPassword = "Les mots de passe ne correspondent pas";
    setErrors(newErrors);

    if (Object.keys(newErrors).length > 0) return;

    try {
      setIsLoading(true);
      const result = await register(formData.username, formData.password);

      if (result?.status === 'success') {
        setIsSuccess(true);
        setFormData(INITIAL_FORM_DATA);
        redirectTimerRef.current = setTimeout(() => navigate('/login'), 10000);
      } else {
        setGenericError(result?.message || "Erreur lors de l'inscription");
      }
    } catch {
      setGenericError('Une erreur inattendue est survenue');
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

        <div className="card">
          <form className="auth-form" onSubmit={handleSubmit}>
            {genericError && (
              <Message className="!w-full" severity="error" text={genericError} />
            )}
            {isSuccess && (
              <Message
                className="!w-full"
                severity="success"
                text="✅ Inscription réussie ! Redirection en cours..."
              />
            )}

            <div className="space-y-6">
              <LabeledInput
                id="username"
                name="username"
                label="Nom d'utilisateur"
                value={formData.username}
                onChange={handleChange}
                required
                error={errors.username}
              />

              <LabeledPassword
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

              <LabeledPassword
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

            <SubmitButton
              label="S'inscrire"
              isLoading={isLoading}
              isDisabled={isLoading || !isFormCurrentlyValid}
            />

            <div className="auth-link text-sm text-gray-500 text-center mt-4">
              <Link
                to="/login"
                className="text-blue-600 hover:underline"
                tabIndex={isLoading ? -1 : 0}
              >
                Vous avez déjà un compte ? Se connecter
              </Link>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Register;