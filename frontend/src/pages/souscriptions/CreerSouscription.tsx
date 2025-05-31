import { API_ENDPOINTS } from '@/config/api';
import ActionButton from "@components/ui/ActionButton.tsx";
import { useMediaQuery } from '@hooks/useMediaQuery';
import { http } from '@services/http';
import { MenuItem } from 'primereact/menuitem';
import { Steps } from 'primereact/steps';
import { classNames } from 'primereact/utils';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useToast } from '../../contexts/ToastContext';
import { Button } from 'primereact/button';

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
  const [currentStep, setCurrentStep] = useState<number>(0);
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

  const updateStepStatus = (stepIndex: number) => {
    const stepFields = {
      0: ['dateMiseEnCirculation', 'numeroImmatriculation', 'couleur', 'categorieCode', 'nombreDeSieges', 'nombreDePortes'],
      1: ['nom', 'prenom', 'numeroCarteIdentite', 'adresse', 'telephone', 'ville'],
      2: []
    };

    const fields = stepFields[stepIndex as keyof typeof stepFields];
    let isStepValid = true;

    fields.forEach((field) => {
      let value = '';
      if (stepIndex === 0) {
        value = String(formData.vehicule[field as keyof Vehicule] || '');
      } else if (stepIndex === 1) {
        value = String(formData.assure[field as keyof Assure] || '');
      }

      if (!value || value.trim() === '') {
        isStepValid = false;
      }
    });

    if (isStepValid && !completedSteps.includes(stepIndex)) {
      setCompletedSteps(prev => [...prev, stepIndex]);
    } else if (!isStepValid && completedSteps.includes(stepIndex)) {
      setCompletedSteps(prev => prev.filter(step => step !== stepIndex));
    }
  };

  const steps: MenuItem[] = [
    {
      label: 'Véhicule',
      icon: completedSteps.includes(0) ? 'pi pi-check' : undefined,
      className: classNames({
        'step-completed': completedSteps.includes(0),
        'step-current': currentStep === 0
      }),
      command: () => {
        if (completedSteps.includes(0) || currentStep >= 0) {
          setCurrentStep(0);
        }
      }
    },
    {
      label: 'Assuré',
      icon: completedSteps.includes(1) ? 'pi pi-check' : undefined,
      className: classNames({
        'step-completed': completedSteps.includes(1),
        'step-current': currentStep === 1
      }),
      command: () => {
        if (completedSteps.includes(1) || currentStep >= 1) {
          setCurrentStep(1);
        }
      }
    },
    {
      label: 'Récapitulatif',
      icon: completedSteps.includes(2) ? 'pi pi-check' : undefined,
      className: classNames({
        'step-completed': completedSteps.includes(2),
        'step-current': currentStep === 2
      }),
      command: () => {
        if (completedSteps.includes(2) || currentStep >= 2) {
          setCurrentStep(2);
        }
      }
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
    const stepFields = {
      0: ['dateMiseEnCirculation', 'numeroImmatriculation', 'couleur', 'categorieCode', 'nombreDeSieges', 'nombreDePortes'],
      1: ['nom', 'prenom', 'numeroCarteIdentite', 'adresse', 'telephone', 'ville'],
      2: []
    };
    const currentStepFields = stepFields[step as keyof typeof stepFields];
    const errors: Record<string, string> = {};

    currentStepFields.forEach((field) => {
      let value = '';
      if (step === 0) {
        value = String(formData.vehicule[field as keyof Vehicule] || '');
      } else if (step === 1) {
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
      updateStepStatus(currentStep);
      if (currentStep === steps.length - 1) {
        handleSubmit();
      } else {
        setCurrentStep(currentStep + 1);
      }
    }
  };

  const handleBack = () => {
    if (currentStep > 0) {
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
            } else if (field.startsWith('assure.')) {
              const assureField = field.replace('assure.', '');
              newValidationErrors[assureField] = messages[0];
            } else {
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
          setCurrentStep(0);
        } else if (firstErrorField.startsWith('assure.')) {
          setCurrentStep(1);
        }
      }
    } finally {
      setIsLoading(false);
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
      case 0:
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
      case 1:
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
      case 2:
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
                      <p
                        className="font-medium text-gray-900 dark:text-white">{formatDate(formData.vehicule.dateMiseEnCirculation)}</p>
                    </div>
                    <div>
                      <p className="text-gray-500 dark:text-gray-400">Numéro d'immatriculation</p>
                      <p
                        className="font-medium text-gray-900 dark:text-white">{formData.vehicule.numeroImmatriculation}</p>
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
    <>
      <h1 className="text-center text-3xl font-extrabold text-gray-900 dark:text-white pb-12 pt-5 gap-4">
        Nouvelle Souscription
      </h1>
      <div className="bg-white dark:bg-gray-800 shadow-lg w-full max-w-7xl mx-auto min-h-[calc(100vh-7rem)]">
        <div className="p-6">
          {apiError && (
            <div className="mb-6 bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 p-4 rounded-lg">
              <p className="text-sm text-red-600 dark:text-red-400">
                {apiError}
              </p>
            </div>
          )}

          {/* Stepper */}
          <div className="mb-8">
            <Steps
              model={steps}
              activeIndex={currentStep}
              readOnly={false}
              className={classNames('custom-steps', { 'steps-mobile': !isDesktop })}
            />
          </div>

          <form onSubmit={(e) => e.preventDefault()} className="space-y-6">
            {/* Contenu de l'étape courante */}
            {renderStepContent(currentStep)}

            {/* Boutons de navigation */}
            <div className="w-full mt-6">
              <div className="flex gap-4 justify-end">
                {currentStep > 0 && (
                  <Button
                    severity="secondary"
                    onClick={handleBack}
                    label="Précédent"
                    className="px-4 py-2 text-sm w-24"
                  />
                )}
                <Button
                  onClick={currentStep === steps.length - 1 ? handleSubmit : handleNext}
                  disabled={isLoading}
                  label={currentStep === steps.length - 1 ? "Enregistrer" : "Suivant"}
                  className="px-4 py-2 text-sm w-24"
                />
              </div>
            </div>
          </form>
        </div>
      </div>
    </>

  );
};

export default CreerSouscription; 