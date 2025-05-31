import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useMediaQuery } from '@hooks/useMediaQuery';
import { http } from '@services/http';
import { API_ENDPOINTS } from '@/config/api';
import { useToast } from '@components/ui/toast';

interface Vehicule {
  dateMiseEnCirculation: string;
  numeroImmatriculation: string;
  couleur: string;
  nombreDeSieges: number;
  nombreDePortes: number;
  categorieCode: string;
}

interface Assure {
  adresse: string;
  telephone: string;
  nom: string;
  prenom: string;
  numeroCarteIdentite: string;
  ville: string;
}

interface SouscriptionData {
  vehicule: Vehicule;
  assure: Assure;
}

interface ApiError {
  message: string;
  errors?: Record<string, string[]>;
}

const CreerSouscription = () => {
  const navigate = useNavigate();
  const { success: showSuccess, error: showError } = useToast();
  const isDesktop = useMediaQuery('(min-width: 1024px)');
  const [currentStep, setCurrentStep] = useState<number>(1);
  const [isLoading, setIsLoading] = useState(false);
  const [validationErrors, setValidationErrors] = useState<Partial<Record<string, string>>>({});
  const [apiError, setApiError] = useState<string | null>(null);
  const [completedSteps, setCompletedSteps] = useState<number[]>([]);

  const [formData, setFormData] = useState<SouscriptionData>({
    vehicule: {
      dateMiseEnCirculation: '',
      numeroImmatriculation: '',
      couleur: '',
      nombreDeSieges: 5,
      nombreDePortes: 4,
      categorieCode: ''
    },
    assure: {
      adresse: '',
      telephone: '',
      nom: '',
      prenom: '',
      numeroCarteIdentite: '',
      ville: ''
    }
  });

  const steps = [
    {
      number: 1,
      title: 'Informations du véhicule',
      fields: ['dateMiseEnCirculation', 'numeroImmatriculation', 'couleur', 'categorieCode', 'nombreDeSieges', 'nombreDePortes']
    },
    {
      number: 2,
      title: 'Informations de l\'assuré',
      fields: ['nom', 'prenom', 'numeroCarteIdentite', 'adresse', 'telephone', 'ville']
    },
    {
      number: 3,
      title: 'Récapitulatif',
      fields: []
    }
  ];

  const handleVehiculeChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      vehicule: {
        ...prev.vehicule,
        [name]: ['nombreDeSieges', 'nombreDePortes'].includes(name) ? Number(value) : value
      }
    }));
    if (validationErrors[name]) {
      setValidationErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
  };

  const handleAssureChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      assure: {
        ...prev.assure,
        [name]: value
      }
    }));
    if (validationErrors[name]) {
      setValidationErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
  };

  const validateStep = (step: number): boolean => {
    const currentStepFields = steps[step - 1].fields;
    const errors: Record<string, string> = {};

    currentStepFields.forEach((field) => {
      let value = '';
      if (step === 1) {
        value = String(formData.vehicule[field as keyof Vehicule] || '');
      } else if (step === 2) {
        value = String(formData.assure[field as keyof Assure] || '');
      }

      if (!value || value.trim() === '') {
        errors[field] = 'Ce champ est requis';
      } else {
        // Validations spécifiques
        switch (field) {
          case 'telephone':
            if (!/^\+?[0-9]{8,}$/.test(value)) {
              errors[field] = 'Numéro de téléphone invalide';
            }
            break;
          case 'numeroImmatriculation':
            if (!/^[A-Z0-9]{1,10}$/.test(value)) {
              errors[field] = 'Numéro d\'immatriculation invalide';
            }
            break;
          case 'dateMiseEnCirculation':
            const date = new Date(value);
            const now = new Date();
            if (date > now) {
              errors[field] = 'La date ne peut pas être dans le futur';
            }
            break;
          case 'nombreDeSieges':
            const sieges = parseInt(value);
            if (isNaN(sieges) || sieges < 1 || sieges > 9) {
              errors[field] = 'Le nombre de sièges doit être entre 1 et 9';
            }
            break;
          case 'nombreDePortes':
            const portes = parseInt(value);
            if (isNaN(portes) || portes < 2 || portes > 5) {
              errors[field] = 'Le nombre de portes doit être entre 2 et 5';
            }
            break;
          case 'numeroCarteIdentite':
            if (!/^[A-Z0-9]{5,}$/.test(value.toUpperCase())) {
              errors[field] = 'Numéro de carte d\'identité invalide';
            }
            break;
        }
      }
    });

    setValidationErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleNext = () => {
    if (validateStep(currentStep)) {
      if (currentStep === steps.length) {
        handleSubmit();
      } else {
        setCompletedSteps(prev => [...new Set([...prev, currentStep])]);
        setCurrentStep(currentStep + 1);
      }
    }
  };

  const handleBack = () => {
    if (currentStep > 1) {
      setCurrentStep(currentStep - 1);
    }
  };

  const handleSubmit = async () => {
    setIsLoading(true);
    setApiError(null);

    try {
      const response = await http.post<{ status: string, data: any }>(API_ENDPOINTS.souscription.creer, formData);
      if (response.status === 'success') {
        showSuccess('Souscription créée avec succès');
        navigate('/souscriptions');
      } else {
        throw new Error('Une erreur est survenue lors de la création de la souscription');
      }
    } catch (error: any) {
      let errorMessage = 'Une erreur est survenue lors de la création de la souscription';

      if (error.response?.data) {
        const apiError = error.response.data as ApiError;

        if (apiError.errors) {
          const newValidationErrors: Record<string, string> = {};

          Object.entries(apiError.errors).forEach(([field, messages]) => {
            if (field.startsWith('vehicule.')) {
              const vehiculeField = field.replace('vehicule.', '');
              newValidationErrors[vehiculeField] = messages[0];
            }
            else if (field.startsWith('assure.')) {
              const assureField = field.replace('assure.', '');
              newValidationErrors[assureField] = messages[0];
            }
            else {
              newValidationErrors[field] = messages[0];
            }
          });

          setValidationErrors(newValidationErrors);
          errorMessage = 'Veuillez corriger les erreurs dans le formulaire';
        } else if (apiError.message) {
          errorMessage = apiError.message;
        }
      }

      setApiError(errorMessage);
      showError(errorMessage);

      if (error.response?.data?.errors) {
        const firstErrorField = Object.keys(error.response.data.errors)[0];
        if (firstErrorField.startsWith('vehicule.')) {
          setCurrentStep(1);
        } else if (firstErrorField.startsWith('assure.')) {
          setCurrentStep(2);
        }
      }
    } finally {
      setIsLoading(false);
    }
  };

  const handleStepClick = (stepNumber: number) => {
    if (completedSteps.includes(stepNumber - 1) || stepNumber <= Math.max(...completedSteps, 1)) {
      setCurrentStep(stepNumber);
    }
  };

  const renderField = (
    name: string,
    label: string,
    type: 'text' | 'number' | 'date' | 'tel' | 'select',
    options?: { value: string; label: string }[],
    isVehiculeField: boolean = true
  ) => {
    const error = validationErrors[name];
    const value = isVehiculeField
      ? formData.vehicule[name as keyof Vehicule]
      : formData.assure[name as keyof Assure];
    const handleChange = isVehiculeField
      ? handleVehiculeChange
      : handleAssureChange as React.ChangeEventHandler<HTMLInputElement | HTMLSelectElement>;

    return (
      <div className="mb-4">
        <label htmlFor={name} className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
          {label}
        </label>
        <div>
          {type === 'select' ? (
            <select
              id={name}
              name={name}
              value={value?.toString()}
              onChange={handleChange}
              className="appearance-none block w-full px-3 py-2 border border-gray-300 dark:border-gray-600 placeholder-gray-400 dark:placeholder-gray-500 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 bg-white dark:bg-white text-gray-900 sm:text-sm"
            >
              <option value="">Sélectionnez une option</option>
              {options?.map(option => (
                <option key={option.value} value={option.value}>
                  {option.label}
                </option>
              ))}
            </select>
          ) : (
            <input
              id={name}
              type={type}
              name={name}
              value={value?.toString()}
              onChange={handleChange}
              className="appearance-none block w-full px-3 py-2 border border-gray-300 dark:border-gray-600 placeholder-gray-400 dark:placeholder-gray-500 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 bg-white dark:bg-white text-gray-900 sm:text-sm"
              placeholder={`Entrez ${label.toLowerCase()}`}
              min={type === 'number' ? '0' : undefined}
              disabled={isLoading}
            />
          )}
          {error && (
            <p className="mt-1 text-xs text-red-600 dark:text-red-400">{error}</p>
          )}
        </div>
      </div>
    );
  };

  const formatDate = (date: string) => {
    return new Date(date).toLocaleDateString('fr-FR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  };

  const renderStepContent = (step: number) => {
    switch (step) {
      case 1:
        return (
          <div className="bg-gray-50 dark:bg-gray-700/50 p-6 rounded-lg space-y-8">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {renderField('dateMiseEnCirculation', 'Date de mise en circulation', 'date')}
              {renderField('numeroImmatriculation', 'Numéro d\'immatriculation', 'text')}
              {renderField('couleur', 'Couleur', 'text')}
              {renderField('categorieCode', 'Catégorie', 'select', [
                { value: 'SUV', label: 'SUV' },
                { value: 'BERLINE', label: 'Berline' },
                { value: 'CITADINE', label: 'Citadine' },
                { value: '4X4', label: '4x4' }
              ])}
              {renderField('nombreDeSieges', 'Nombre de sièges', 'number')}
              {renderField('nombreDePortes', 'Nombre de portes', 'number')}
            </div>
          </div>
        );
      case 2:
        return (
          <div className="bg-gray-50 dark:bg-gray-700/50 p-6 rounded-lg space-y-8">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {renderField('nom', 'Nom', 'text', undefined, false)}
              {renderField('prenom', 'Prénom', 'text', undefined, false)}
              {renderField('numeroCarteIdentite', 'Numéro de carte d\'identité', 'text', undefined, false)}
              {renderField('adresse', 'Adresse', 'text', undefined, false)}
              {renderField('telephone', 'Téléphone', 'tel', undefined, false)}
              {renderField('ville', 'Ville', 'text', undefined, false)}
            </div>
          </div>
        );
      case 3:
        return (
          <div className="bg-gray-50 dark:bg-gray-700/50 p-6 rounded-lg">
            <h2 className="text-xl font-semibold text-gray-900 dark:text-white mb-6">
              Récapitulatif de la souscription
            </h2>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              {/* Informations du véhicule */}
              <div className="bg-white dark:bg-gray-800 p-6 rounded-lg shadow-sm">
                <h3 className="text-lg font-medium text-gray-900 dark:text-white mb-4">
                  Informations du véhicule
                </h3>
                <div className="space-y-3">
                  <div className="grid grid-cols-2 gap-x-4 gap-y-3 text-sm">
                    <div>
                      <p className="text-gray-500 dark:text-gray-400">Date de mise en circulation</p>
                      <p className="font-medium text-gray-900 dark:text-white">{formatDate(formData.vehicule.dateMiseEnCirculation)}</p>
                    </div>
                    <div>
                      <p className="text-gray-500 dark:text-gray-400">Numéro d'immatriculation</p>
                      <p className="font-medium text-gray-900 dark:text-white">{formData.vehicule.numeroImmatriculation}</p>
                    </div>
                    <div>
                      <p className="text-gray-500 dark:text-gray-400">Couleur</p>
                      <p className="font-medium text-gray-900 dark:text-white">{formData.vehicule.couleur}</p>
                    </div>
                    <div>
                      <p className="text-gray-500 dark:text-gray-400">Catégorie</p>
                      <p className="font-medium text-gray-900 dark:text-white">{formData.vehicule.categorieCode}</p>
                    </div>
                    <div>
                      <p className="text-gray-500 dark:text-gray-400">Nombre de sièges</p>
                      <p className="font-medium text-gray-900 dark:text-white">{formData.vehicule.nombreDeSieges}</p>
                    </div>
                    <div>
                      <p className="text-gray-500 dark:text-gray-400">Nombre de portes</p>
                      <p className="font-medium text-gray-900 dark:text-white">{formData.vehicule.nombreDePortes}</p>
                    </div>
                  </div>
                </div>
              </div>

              {/* Informations de l'assuré */}
              <div className="bg-white dark:bg-gray-800 p-6 rounded-lg shadow-sm">
                <h3 className="text-lg font-medium text-gray-900 dark:text-white mb-4">
                  Informations de l'assuré
                </h3>
                <div className="space-y-3">
                  <div className="grid grid-cols-2 gap-x-4 gap-y-3 text-sm">
                    <div>
                      <p className="text-gray-500 dark:text-gray-400">Nom</p>
                      <p className="font-medium text-gray-900 dark:text-white">{formData.assure.nom}</p>
                    </div>
                    <div>
                      <p className="text-gray-500 dark:text-gray-400">Prénom</p>
                      <p className="font-medium text-gray-900 dark:text-white">{formData.assure.prenom}</p>
                    </div>
                    <div>
                      <p className="text-gray-500 dark:text-gray-400">Numéro de carte d'identité</p>
                      <p className="font-medium text-gray-900 dark:text-white">{formData.assure.numeroCarteIdentite}</p>
                    </div>
                    <div>
                      <p className="text-gray-500 dark:text-gray-400">Téléphone</p>
                      <p className="font-medium text-gray-900 dark:text-white">{formData.assure.telephone}</p>
                    </div>
                    <div>
                      <p className="text-gray-500 dark:text-gray-400">Ville</p>
                      <p className="font-medium text-gray-900 dark:text-white">{formData.assure.ville}</p>
                    </div>
                    <div>
                      <p className="text-gray-500 dark:text-gray-400">Adresse</p>
                      <p className="font-medium text-gray-900 dark:text-white">{formData.assure.adresse}</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div className="mt-6">
              <div className="bg-yellow-50 dark:bg-yellow-900/20 p-4 rounded-lg">
                <p className="text-sm text-yellow-800 dark:text-yellow-200">
                  Veuillez vérifier toutes les informations avant de créer la souscription.
                  Une fois créée, certaines informations ne pourront plus être modifiées.
                </p>
              </div>
            </div>
          </div>
        );
      default:
        return null;
    }
  };

  return (
    <div className="bg-white dark:bg-gray-800 shadow-lg w-full max-w-7xl mx-auto min-h-[calc(100vh-7rem)]">
      <div className="p-6">
        <h1 className="text-3xl font-bold text-gray-900 dark:text-white mb-6">
          Nouvelle Souscription
        </h1>

        {apiError && (
          <div className="mb-6 bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 p-4 rounded-lg">
            <p className="text-sm text-red-600 dark:text-red-400">
              {apiError}
            </p>
          </div>
        )}

        {/* Stepper */}
        <div className="mb-8">
          <div className="relative flex items-center justify-between">
            {steps.map((step, index) => (
              <div
                key={step.number}
                className={`flex flex-col items-center flex-1 ${completedSteps.includes(step.number) || step.number <= Math.max(...completedSteps, 1)
                  ? 'cursor-pointer'
                  : 'cursor-not-allowed'
                  }`}
                onClick={() => handleStepClick(step.number)}
              >
                <div className={`flex items-center justify-center w-8 h-8 border-2 rounded-full mb-2 
                    ${currentStep >= step.number
                    ? 'border-indigo-600 bg-indigo-600 text-white'
                    : completedSteps.includes(step.number)
                      ? 'border-indigo-600 bg-white text-indigo-600'
                      : 'border-gray-300 text-gray-500'
                  }`}
                >
                  {step.number}
                </div>
                <div className={`text-xs text-center ${currentStep >= step.number || completedSteps.includes(step.number)
                  ? 'text-indigo-600 font-medium'
                  : 'text-gray-500'
                  }`}>
                  {step.title}
                </div>
              </div>
            ))}
            {/* Lignes de connexion */}
            <div className="absolute top-4 left-0 right-0 flex -z-10">
              {steps.map((_, index) => (
                index < steps.length - 1 && (
                  <div
                    key={index}
                    className={`h-0.5 flex-1 mx-4 ${currentStep > index + 1 || completedSteps.includes(index + 2)
                      ? 'bg-indigo-600'
                      : 'bg-gray-300'
                      }`}
                  />
                )
              ))}
            </div>
          </div>
        </div>

        <form onSubmit={(e) => e.preventDefault()} className="space-y-6">
          {/* Contenu de l'étape courante */}
          {renderStepContent(currentStep)}

          {/* Boutons de navigation */}
          <div className="flex justify-between mt-6">
            <button
              type="button"
              onClick={handleBack}
              className={`px-4 py-2 text-sm border border-gray-300 text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 ${currentStep === 1 ? 'invisible' : ''}`}
            >
              Précédent
            </button>
            <button
              type="button"
              onClick={currentStep === steps.length ? handleSubmit : handleNext}
              disabled={isLoading}
              className={`px-4 py-2 text-sm border border-transparent text-white focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50
                ${apiError ? 'bg-red-600 hover:bg-red-700' : 'bg-indigo-600 hover:bg-indigo-700'}`}
            >
              {isLoading ? (
                <span className="flex items-center">
                  <svg className="animate-spin -ml-1 mr-2 h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                    <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                    <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                  </svg>
                  Création en cours...
                </span>
              ) : currentStep === steps.length ? (
                'Créer la souscription'
              ) : (
                'Suivant'
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default CreerSouscription; 