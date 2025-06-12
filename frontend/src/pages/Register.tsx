import React, { useState, useCallback } from 'react';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEye, faEyeSlash, faCheck } from '@fortawesome/free-solid-svg-icons';
import { useAuth } from '../contexts/AuthContext';

// Types
interface FormData {
  username: string;
  password: string;
  confirmPassword: string;
}

interface FormErrors {
  username?: string;
  password?: string;
  confirmPassword?: string;
}

interface ErrorResponse {
  status?: string;
  data?: unknown;
  message?: string;
}

// Constants
const INITIAL_FORM_DATA: FormData = {
  username: '',
  password: '',
  confirmPassword: ''
};

const VALIDATION = {
  PASSWORD_MIN_LENGTH: 8,
  PASSWORD_LETTER_REGEX: /[A-Za-z]/,
  PASSWORD_NUMBER_REGEX: /\d/,
  USERNAME_EXISTS_REGEX: /un utilisateur avec le username .+ existe déjà/i
} as const;

const MESSAGES = {
  PASSWORD_TOO_SHORT: 'Le mot de passe doit contenir au moins 8 caractères',
  PASSWORD_NO_LETTER: 'Le mot de passe doit contenir au moins une lettre',
  PASSWORD_NO_NUMBER: 'Le mot de passe doit contenir au moins un chiffre',
  PASSWORDS_DONT_MATCH: 'Les mots de passe ne correspondent pas',
  USERNAME_TAKEN: 'Ce nom d\'utilisateur est déjà pris',
  GENERIC_ERROR: 'Une erreur est survenue lors de l\'inscription',
  SUCCESS: 'Votre compte a été créé avec succès ! Vous pouvez maintenant vous connecter.'
} as const;

// Validation functions
const validatePassword = (password: string): string => {
  if (password.length < VALIDATION.PASSWORD_MIN_LENGTH) return MESSAGES.PASSWORD_TOO_SHORT;
  if (!VALIDATION.PASSWORD_LETTER_REGEX.test(password)) return MESSAGES.PASSWORD_NO_LETTER;
  if (!VALIDATION.PASSWORD_NUMBER_REGEX.test(password)) return MESSAGES.PASSWORD_NO_NUMBER;
  return '';
};

const validateConfirmPassword = (password: string, confirmPassword: string): string => {
  return confirmPassword && password !== confirmPassword ? MESSAGES.PASSWORDS_DONT_MATCH : '';
};

const hasRequiredFields = (formData: FormData): boolean => {
  return Object.values(formData).every(field => field.trim() !== '');
};

const isFormValid = (formData: FormData, errors: FormErrors): boolean => {
  const hasNoErrors = Object.values(errors).every(error => !error);
  const fieldsValid = hasRequiredFields(formData);
  const passwordsMatch = formData.password === formData.confirmPassword;
  const passwordValid = !validatePassword(formData.password);

  return hasNoErrors && fieldsValid && passwordsMatch && passwordValid;
};

// Component
const Register = () => {
  const { register } = useAuth();

  // State
  const [formData, setFormData] = useState<FormData>(INITIAL_FORM_DATA);
  const [errors, setErrors] = useState<FormErrors>({});
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');

  // Handlers
  const clearErrors = useCallback(() => {
    setErrors({});
    setSuccessMessage('');
  }, []);

  const setFieldError = useCallback((field: keyof FormErrors, message: string) => {
    setErrors(prev => ({ ...prev, [field]: message }));
  }, []);

  const handleErrorResponse = useCallback((response: ErrorResponse) => {
    if (response.data && typeof response.data === 'object') {
      const validationData = response.data as Record<string, unknown>;
      let hasFieldErrors = false;

      for (const field of ['username', 'password', 'confirmPassword']) {
        if (typeof validationData[field] === 'string') {
          setFieldError(field as keyof FormErrors, validationData[field]);
          hasFieldErrors = true;
        }
      }

      if (hasFieldErrors) return;
    }

    if (response.message) {
      const isUsernameTaken = VALIDATION.USERNAME_EXISTS_REGEX.test(response.message);
      setFieldError('username', isUsernameTaken ? MESSAGES.USERNAME_TAKEN : response.message);
    } else {
      setFieldError('username', MESSAGES.GENERIC_ERROR);
    }
  }, [setFieldError]);

  const validateForm = useCallback((data: FormData): FormErrors => {
    const newErrors: FormErrors = {};

    const passwordError = validatePassword(data.password);
    if (passwordError) newErrors.password = passwordError;

    const confirmPasswordError = validateConfirmPassword(data.password, data.confirmPassword);
    if (confirmPasswordError) newErrors.confirmPassword = confirmPasswordError;

    return newErrors;
  }, []);

  const handleInputChange = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;

    setFormData(prev => ({ ...prev, [name]: value }));

    if (errors[name as keyof FormErrors]) {
      setErrors(prev => ({ ...prev, [name]: undefined }));
    }

    if (successMessage) setSuccessMessage('');
  }, [errors, successMessage]);

  const handleSubmit = useCallback(async (e: React.FormEvent) => {
    e.preventDefault();
    clearErrors();

    const validationErrors = validateForm(formData);
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    setIsLoading(true);

    try {
      const response = await register(formData.username, formData.password);

      if (response.status === 'success') {
        setSuccessMessage(MESSAGES.SUCCESS);
      } else {
        handleErrorResponse(response);
      }
    } catch (error) {
      console.error('Registration error:', error);
      setFieldError('username', MESSAGES.GENERIC_ERROR);
    } finally {
      setIsLoading(false);
    }
  }, [formData, register, clearErrors, validateForm, setFieldError, handleErrorResponse]);

  const togglePasswordVisibility = useCallback(() => setShowPassword(prev => !prev), []);
  const toggleConfirmPasswordVisibility = useCallback(() => setShowConfirmPassword(prev => !prev), []);

  // Render helpers
  const renderPasswordField = useCallback((
      name: 'password' | 'confirmPassword',
      label: string,
      placeholder: string,
      isVisible: boolean,
      toggle: () => void
  ) => (
      <div className="group">
        <label htmlFor={name} className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
          {label}
        </label>
        <div className="relative">
          <input
              id={name}
              name={name}
              type={isVisible ? "text" : "password"}
              required
              className={`block w-full px-4 py-3 rounded-lg border ${
                  errors[name]
                      ? 'border-red-300 dark:border-red-500 focus:ring-red-500'
                      : 'border-gray-300 dark:border-gray-600 focus:ring-indigo-500'
              } focus:ring-2 focus:border-transparent bg-white dark:bg-gray-800 text-gray-900 dark:text-white text-sm transition-all duration-200 ease-in-out transform hover:shadow-sm focus:shadow-md outline-none pr-12`}
              placeholder={placeholder}
              value={formData[name]}
              onChange={handleInputChange}
              disabled={isLoading}
          />
          <button
              type="button"
              className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 dark:hover:text-gray-300 transition-colors duration-200 w-10 h-10 flex items-center justify-center bg-transparent border-none p-0"
              onClick={toggle}
              disabled={isLoading}
              tabIndex={-1}
              aria-label={isVisible ? "Masquer le mot de passe" : "Afficher le mot de passe"}
          >
            <FontAwesomeIcon
                icon={isVisible ? faEyeSlash : faEye}
                className="h-5 w-5"
            />
          </button>
        </div>
        {errors[name] && (
            <p className="mt-2 text-sm text-red-600 dark:text-red-400">
              {errors[name]}
            </p>
        )}
      </div>
  ), [formData, errors, handleInputChange, isLoading]);

  const renderSuccessMessage = () => successMessage && (
      <div className="bg-green-50 dark:bg-green-900/50 border-l-4 border-green-400 p-4 rounded-r-lg animate-fadeIn" role="alert">
        <div className="flex items-center">
          <FontAwesomeIcon icon={faCheck} className="h-5 w-5 text-green-400 mr-3" />
          <p className="text-sm text-green-700 dark:text-green-200">{successMessage}</p>
        </div>
      </div>
  );

  const renderUsernameField = () => (
      <div className="group">
        <label htmlFor="username" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
          Nom d'utilisateur
        </label>
        <input
            id="username"
            name="username"
            type="text"
            required
            className={`block w-full px-4 py-3 rounded-lg border ${
                errors.username
                    ? 'border-red-300 dark:border-red-500 focus:ring-red-500'
                    : 'border-gray-300 dark:border-gray-600 focus:ring-indigo-500'
            } focus:ring-2 focus:border-transparent bg-white dark:bg-gray-800 text-gray-900 dark:text-white text-sm transition-all duration-200 ease-in-out transform hover:shadow-sm focus:shadow-md outline-none`}
            placeholder="Choisissez un nom d'utilisateur"
            value={formData.username}
            onChange={handleInputChange}
            disabled={isLoading}
            autoComplete="username"
        />
        {errors.username && (
            <p className="mt-2 text-sm text-red-600 dark:text-red-400">
              {errors.username}
            </p>
        )}
      </div>
  );

  const renderSubmitButton = () => (
      <button
          type="submit"
          className="w-full flex justify-center items-center px-6 py-3 rounded-lg text-base font-semibold text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-all duration-300 ease-in-out transform hover:-translate-y-0.5 hover:shadow-lg disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:transform-none disabled:hover:shadow-none"
          disabled={isLoading || !isFormValid(formData, errors)}
      >
        {isLoading ? (
            <span className="flex items-center space-x-3">
          <svg className="animate-spin h-5 w-5" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" />
            <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z" />
          </svg>
          <span>Inscription en cours...</span>
        </span>
        ) : (
            'S\'inscrire'
        )}
      </button>
  );

  return (
      <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-indigo-50 to-white dark:from-gray-900 dark:to-gray-800 px-4 sm:px-6 lg:px-8">
        <div className="max-w-md w-full">
          <div className="text-center mb-10">
            <h1 className="text-4xl font-bold text-gray-900 dark:text-white mb-2">
              Bienvenue
            </h1>
            <p className="text-gray-600 dark:text-gray-400">
              Créez votre compte pour commencer
            </p>
          </div>

          <div className="bg-white dark:bg-gray-800 shadow-xl rounded-2xl p-8 space-y-8 transition-all duration-300 hover:shadow-2xl">
            <form className="space-y-6" onSubmit={handleSubmit}>
              {renderSuccessMessage()}

              <div className="space-y-6">
                {renderUsernameField()}

                {renderPasswordField(
                    'password',
                    'Mot de passe',
                    'Créez votre mot de passe',
                    showPassword,
                    togglePasswordVisibility
                )}

                {renderPasswordField(
                    'confirmPassword',
                    'Confirmer le mot de passe',
                    'Confirmez votre mot de passe',
                    showConfirmPassword,
                    toggleConfirmPasswordVisibility
                )}
              </div>

              <div className="pt-2">
                {renderSubmitButton()}
              </div>

              <div className="text-center mt-8">
                <Link
                    to="/login"
                    className="inline-flex items-center text-sm font-medium text-indigo-600 hover:text-indigo-700 dark:text-indigo-400 dark:hover:text-indigo-300 transition-all duration-200 hover:-translate-y-0.5"
                    tabIndex={isLoading ? -1 : 0}
                >
                  {'Déjà un compte ?'}
                  <span className="ml-1 group-hover:ml-2 transition-all duration-200">
                  Se connecter
                </span>
                </Link>
              </div>
            </form>
          </div>
        </div>
      </div>
  );
};

export default Register;