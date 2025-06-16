import { API_ENDPOINTS } from '@/config/api';
import { useMediaQuery } from '@hooks/useMediaQuery';
import { httpClient } from '@/services/http/http-client';
import { MenuItem } from 'primereact/menuitem';
import { Steps } from 'primereact/steps';
import { classNames } from 'primereact/utils';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useToast } from '@contexts/ToastContext.tsx';
import { Button } from 'primereact/button';
import { produitHttpService } from "@services/produit.http-service.ts";
import { formaterDate } from '@/utils/dateUtils';

interface Vehicule {
  dateMiseEnCirculation: string;
  immatriculation: string;
  couleur: string;
  nombreDeSieges: number;
  nombreDePortes: number;
  categorieCode: string;
  puissanceFiscale: number;
  valeurNeuf: number;
}

interface PuissanceFiscale {
  debut: number;
  fin: number;
  exactMatch: boolean;
}

interface Prime {
  type: 'MONTANT' | 'POURCENTAGE';
  valeur: number;
}

interface Garantie {
  id: string;
  libelle: string;
  description: string;
  code: string;
  puissanceFiscale: PuissanceFiscale | null;
  baseDeCalcul: string;
  prime: Prime;
  primeMinimum: number | null;
  maxAge: number;
  plafonne: boolean;
}

interface Categorie {
  id: string;
  code: string;
  libelle: string;
  description: string;
}

interface Produit {
  id: string;
  code: string | null;
  nom: string;
  description: string;
  garanties: Garantie[];
  categoriesVehicules: Categorie[];
}

interface Assure {
  nom: string;
  prenoms: string;
  sexe: string;
  dateNaissance: string;
  lieuNaissance: string;
  numeroCarteIdentite: string;
  telephone: string;
  adresse: string;
  email: string;
}

interface SouscriptionData {
  vehicule: Vehicule;
  assure: Assure;
  vehiculeValeurVenale: number;
  produit: string;
}

interface ApiError {
  message: string;
  errors?: Record<string, string[]>;
}


const CreerSouscription = () => {
  const navigate = useNavigate();
  const { success: showSuccess, error: showError } = useToast();
  const isDesktop = useMediaQuery('(min-width: 1024px)');
  const [currentStep, setCurrentStep] = useState(0);
  const [isLoading, setIsLoading] = useState(false);
  const [apiError, setApiError] = useState<string | null>(null);
  const [touchedFields, setTouchedFields] = useState<Set<string>>(new Set());
  const [validationErrors, setValidationErrors] = useState<Record<string, string>>({});
  const [completedSteps, setCompletedSteps] = useState<number[]>([]);
  const [produits, setProduits] = useState<Produit[]>([]);
  const [categoriesDisponibles, setCategoriesDisponibles] = useState<Categorie[]>([]);
  const [forceUpdate, setForceUpdate] = useState(0);

  const [formData, setFormData] = useState<SouscriptionData>({
    vehicule: {
      dateMiseEnCirculation: '',
      immatriculation: '',
      couleur: '',
      nombreDeSieges: 5,
      nombreDePortes: 4,
      categorieCode: '',
      puissanceFiscale: 0,
      valeurNeuf: 0
    },
    assure: {
      nom: '',
      prenoms: '',
      sexe: '',
      dateNaissance: '',
      lieuNaissance: '',
      numeroCarteIdentite: '',
      telephone: '',
      adresse: '',
      email: ''
    },
    vehiculeValeurVenale: 0,
    produit: ''
  });

  // Hook personnalisé pour détecter la taille de l'écran
  const useScreenSize = () => {
    const [screenWidth, setScreenWidth] = useState(window.innerWidth);

    useEffect(() => {
      const handleResize = () => {
        const newWidth = window.innerWidth;
        setScreenWidth(newWidth);
        // Forcer le re-rendu du composant
        setForceUpdate(prev => prev + 1);
      };

      window.addEventListener('resize', handleResize);
      return () => window.removeEventListener('resize', handleResize);
    }, []);

    return screenWidth;
  };

  const screenWidth = useScreenSize();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const produitsResponse = await produitHttpService.lister();

        if (produitsResponse.data) {
          setProduits(produitsResponse.data);
        }
      } catch (error) {
        showError('Erreur lors du chargement des données');
        console.error('Erreur:', error);
      }
    };

    fetchData();
  }, [showError]);

  const updateStepStatus = (stepIndex: number) => {
    const stepFields = {
      0: ['produit', 'dateMiseEnCirculation', 'immatriculation', 'couleur', 'categorieCode', 'nombreDeSieges', 'nombreDePortes', 'puissanceFiscale', 'valeurNeuf'],
      1: ['nom', 'prenoms', 'numeroCarteIdentite', 'email', 'sexe', 'dateNaissance', 'lieuNaissance', 'adresse', 'telephone'],
      2: []
    };

    const fields = stepFields[stepIndex as keyof typeof stepFields];
    let isStepValid = true;

    fields.forEach((field) => {
      let value = '';
      if (stepIndex === 0) {
        if (field === 'produit') {
          value = String(formData.produit || '');
        } else {
          value = String(formData.vehicule[field as keyof Vehicule] || '');
        }
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

    // Marquer le champ comme touché
    setTouchedFields(prev => new Set([...prev, name]));

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

    // Valider seulement le champ modifié
    setTimeout(() => {
      validateField(name, value);
      updateStepStatus(currentStep);
    }, 0);
  };

  const handleAssureChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;

    // Marquer le champ comme touché
    setTouchedFields(prev => new Set([...prev, name]));

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

    // Valider seulement le champ modifié
    setTimeout(() => {
      validateField(name, value);
      updateStepStatus(currentStep);
    }, 0);
  };

  const handleProduitChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const produitSelectionne = produits.find(p => p.id === e.target.value);

    // Marquer le champ comme touché
    setTouchedFields(prev => new Set([...prev, 'produit']));

    if (produitSelectionne) {
      setCategoriesDisponibles(produitSelectionne.categoriesVehicules || []);
      const categorieExiste = produitSelectionne.categoriesVehicules?.some(
        cat => cat.code === formData.vehicule.categorieCode
      );
      if (!categorieExiste) {
        setFormData(prev => ({
          ...prev,
          produit: produitSelectionne.id,
          vehicule: {
            ...prev.vehicule,
            categorieCode: ''
          }
        }));
      } else {
        setFormData(prev => ({
          ...prev,
          produit: produitSelectionne.id
        }));
      }
    } else {
      setCategoriesDisponibles([]);
      setFormData(prev => ({
        ...prev,
        produit: '',
        vehicule: {
          ...prev.vehicule,
          categorieCode: ''
        }
      }));
    }

    if (validationErrors['produit']) {
      setValidationErrors(prev => ({
        ...prev,
        produit: ''
      }));
    }

    // Valider seulement le champ modifié
    setTimeout(() => {
      validateField('produit', e.target.value);
      updateStepStatus(currentStep);
    }, 0);
  };

  const handleVehiculeValeurVenaleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { value } = e.target;

    // Marquer le champ comme touché
    setTouchedFields(prev => new Set([...prev, 'vehiculeValeurVenale']));

    setFormData(prev => ({
      ...prev,
      vehiculeValeurVenale: Number(value)
    }));

    if (validationErrors['vehiculeValeurVenale']) {
      setValidationErrors(prev => ({
        ...prev,
        vehiculeValeurVenale: ''
      }));
    }

    // Valider seulement le champ modifié
    setTimeout(() => {
      validateField('vehiculeValeurVenale', value);
      updateStepStatus(currentStep);
    }, 0);
  };

  const validateField = (fieldName: string, value: string) => {
    const errors: Record<string, string> = {};

    if (!value || value.trim() === '') {
      errors[fieldName] = 'Ce champ est requis';
    } else {
      // Validations spécifiques
      switch (fieldName) {
        case 'telephone':
          if (!/^\+?[0-9]{8,}$/.test(value)) {
            errors[fieldName] = 'Numéro de téléphone invalide';
          }
          break;
        case 'immatriculation':
          if (!/^[A-Z0-9\s]+$/i.test(value)) {
            errors[fieldName] = 'Le numéro d\'immatriculation ne doit contenir que des lettres et des chiffres';
          }
          break;
        case 'email':
          if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) {
            errors[fieldName] = 'Adresse email invalide';
          }
          break;
        case 'dateMiseEnCirculation':
        case 'dateNaissance': {
          const date = new Date(value);
          const now = new Date();
          if (date > now) {
            errors[fieldName] = 'La date ne peut pas être dans le futur';
          }
          break;
        }
        case 'nombreDeSieges': {
          const sieges = parseInt(value);
          if (isNaN(sieges) || sieges < 1 || sieges > 9) {
            errors[fieldName] = 'Le nombre de sièges doit être entre 1 et 9';
          }
          break;
        }
        case 'nombreDePortes': {
          const portes = parseInt(value);
          if (isNaN(portes) || portes < 2 || portes > 5) {
            errors[fieldName] = 'Le nombre de portes doit être entre 2 et 5';
          }
          break;
        }
        case 'puissanceFiscale': {
          const puissance = parseInt(value);
          if (isNaN(puissance) || puissance < 1) {
            errors[fieldName] = 'La puissance fiscale doit être supérieure à 0';
          }
          break;
        }
        case 'valeurNeuf':
        case 'vehiculeValeurVenale': {
          const valeur = parseFloat(value);
          if (isNaN(valeur) || valeur <= 0) {
            errors[fieldName] = 'La valeur doit être supérieure à 0';
          }
          break;
        }
        case 'numeroCarteIdentite':
          if (!/^[A-Z0-9]{5,}$/.test(value.toUpperCase())) {
            errors[fieldName] = 'Numéro de carte d\'identité invalide';
          }
          break;
      }
    }

    setValidationErrors(prev => {
      const newErrors = { ...prev };
      if (errors[fieldName]) {
        // Ajouter l'erreur
        newErrors[fieldName] = errors[fieldName];
      } else {
        // Supprimer l'erreur si le champ est maintenant valide
        delete newErrors[fieldName];
      }
      return newErrors;
    });
  };

  const validateStep = (step: number): boolean => {
    const stepFields = {
      0: ['produit', 'dateMiseEnCirculation', 'immatriculation', 'couleur', 'categorieCode', 'nombreDeSieges', 'nombreDePortes', 'puissanceFiscale', 'valeurNeuf'],
      1: ['nom', 'prenoms', 'numeroCarteIdentite', 'email', 'sexe', 'dateNaissance', 'lieuNaissance', 'adresse', 'telephone'],
      2: []
    };
    const currentStepFields = stepFields[step as keyof typeof stepFields];
    const errors: Record<string, string> = {};

    currentStepFields.forEach((field) => {
      let value = '';
      if (step === 0) {
        if (field === 'produit') {
          value = String(formData.produit || '');
        } else {
          value = String(formData.vehicule[field as keyof Vehicule] || '');
        }
      } else if (step === 1) {
        value = String(formData.assure[field as keyof Assure] || '');
      }

      // Ne valider que si le champ a été touché ou a une valeur
      if (touchedFields.has(field) || (value && value.trim() !== '')) {
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
            case 'immatriculation':
              if (!/^[A-Z0-9\s]+$/i.test(value)) {
                errors[field] = 'Le numéro d\'immatriculation ne doit contenir que des lettres et des chiffres';
              }
              break;
            case 'email':
              if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) {
                errors[field] = 'Adresse email invalide';
              }
              break;
            case 'dateMiseEnCirculation':
            case 'dateNaissance': {
              const date = new Date(value);
              const now = new Date();
              if (date > now) {
                errors[field] = 'La date ne peut pas être dans le futur';
              }
              break;
            }
            case 'nombreDeSieges': {
              const sieges = parseInt(value);
              if (isNaN(sieges) || sieges < 1 || sieges > 9) {
                errors[field] = 'Le nombre de sièges doit être entre 1 et 9';
              }
              break;
            }
            case 'nombreDePortes': {
              const portes = parseInt(value);
              if (isNaN(portes) || portes < 2 || portes > 5) {
                errors[field] = 'Le nombre de portes doit être entre 2 et 5';
              }
              break;
            }
            case 'puissanceFiscale': {
              const puissance = parseInt(value);
              if (isNaN(puissance) || puissance < 1) {
                errors[field] = 'La puissance fiscale doit être supérieure à 0';
              }
              break;
            }
            case 'valeurNeuf': {
              const valeur = parseFloat(value);
              if (isNaN(valeur) || valeur <= 0) {
                errors[field] = 'La valeur doit être supérieure à 0';
              }
              break;
            }
            case 'numeroCarteIdentite':
              if (!/^[A-Z0-9]{5,}$/.test(value.toUpperCase())) {
                errors[field] = 'Numéro de carte d\'identité invalide';
              }
              break;
          }
        }
      }
    });

    setValidationErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleNext = () => {
    if (validateStep(currentStep)) {
      updateStepStatus(currentStep);
      if (currentStep === 2) {
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
      // Récupérer le nom du produit à partir de l'ID
      const produitSelectionne = produits.find(p => p.id === formData.produit);
      const nomProduit = produitSelectionne?.nom || '';

      const submitData: SouscriptionData = {
        vehicule: {
          ...formData.vehicule,
          nombreDeSieges: Number(formData.vehicule.nombreDeSieges),
          nombreDePortes: Number(formData.vehicule.nombreDePortes),
          puissanceFiscale: Number(formData.vehicule.puissanceFiscale),
          valeurNeuf: Number(formData.vehicule.valeurNeuf)
        },
        assure: {
          ...formData.assure
        },
        vehiculeValeurVenale: Number(formData.vehiculeValeurVenale),
        produit: nomProduit
      };

      const response = await httpClient.post<{ status: string, data: any }>(
        API_ENDPOINTS.souscription.creer,
        submitData
      );

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
            newValidationErrors[field] = messages[0];
          });
          setValidationErrors(newValidationErrors);
          errorMessage = 'Veuillez corriger les erreurs dans le formulaire';
        } else if (apiError.message) {
          errorMessage = apiError.message;
        }
      }

      setApiError(errorMessage);
      showError(errorMessage);
    } finally {
      setIsLoading(false);
    }
  };

  const renderField = (
    name: string,
    label: string,
    type: 'text' | 'number' | 'date' | 'tel' | 'select' | 'email',
    options?: { value: string; label: string }[],
    isVehiculeField: boolean = true
  ) => {
    const error = validationErrors[name];
    const value = isVehiculeField
      ? name === 'produit'
        ? formData.produit
        : name === 'vehiculeValeurVenale'
          ? formData.vehiculeValeurVenale
          : formData.vehicule[name as keyof Vehicule]
      : formData.assure[name as keyof Assure];

    const getChangeHandler = () => {
      if (type === 'select') {
        return isVehiculeField
          ? name === 'produit'
            ? handleProduitChange
            : handleVehiculeChange
          : handleAssureChange;
      } else {
        return isVehiculeField
          ? name === 'vehiculeValeurVenale'
            ? handleVehiculeValeurVenaleChange
            : handleVehiculeChange
          : handleAssureChange;
      }
    };

    return (
      <div className="mb-3 sm:mb-4">
        <label htmlFor={name}
          className="block text-xs sm:text-sm font-semibold text-gray-700 dark:text-gray-300 mb-2 bg-gradient-to-r from-gray-600 to-gray-800 dark:from-gray-300 dark:to-gray-100 bg-clip-text text-transparent">
          {label}
        </label>
        <div>
          {type === 'select' ? (
            <select
              id={name}
              name={name}
              value={value?.toString() || ''}
              onChange={getChangeHandler() as React.ChangeEventHandler<HTMLSelectElement>}
              className="appearance-none block w-full px-3 sm:px-4 py-2 sm:py-2.5 border border-gray-200/50 dark:border-gray-600/50 placeholder-gray-400 dark:placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-indigo-500/50 focus:border-indigo-500 bg-white/80 dark:bg-gray-800/80 text-gray-900 text-xs sm:text-sm rounded-xl shadow-sm hover:shadow-md transition-all duration-200 backdrop-blur-sm"
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
              value={value?.toString() || ''}
              onChange={getChangeHandler() as React.ChangeEventHandler<HTMLInputElement>}
              className="appearance-none block w-full px-3 sm:px-4 py-2 sm:py-2.5 border border-gray-200/50 dark:border-gray-600/50 placeholder-gray-400 dark:placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-indigo-500/50 focus:border-indigo-500 bg-white/80 dark:bg-gray-800/80 text-gray-900 text-xs sm:text-sm rounded-xl shadow-sm hover:shadow-md transition-all duration-200 backdrop-blur-sm"
              placeholder={`Entrez ${label.toLowerCase()}`}
              min={type === 'number' ? '0' : undefined}
              disabled={isLoading}
            />
          )}
          {error && (
            <p className="mt-2 text-xs text-red-600 dark:text-red-400 bg-red-50/50 dark:bg-red-900/20 px-2 py-1 rounded-lg backdrop-blur-sm">{error}</p>
          )}
        </div>
      </div>
    );
  };

  const renderStepContent = (step: number) => {
    // Déterminer si on est sur un écran très petit (≤ 500px)
    const isVerySmallScreen = screenWidth <= 500;
    const gridClass = isVerySmallScreen ? 'grid-cols-1' : 'grid-cols-1 custom-grid-cols-2';
    const mobileClass = isVerySmallScreen ? 'mobile-single-column' : '';

    switch (step) {
      case 0:
        return (
          <div className="space-y-4 sm:space-y-6">
            {/* Informations du produit */}
            <fieldset className="border border-gray-200/50 dark:border-gray-600/50 rounded-2xl p-4 sm:p-6 bg-white/60 dark:bg-gray-800/60 backdrop-blur-sm shadow-lg hover:shadow-xl transition-all duration-300">
              <legend className="text-base sm:text-lg font-semibold text-gray-900 dark:text-white px-3 py-1 bg-gradient-to-r from-indigo-50 to-purple-50 dark:from-indigo-900/20 dark:to-purple-900/20 rounded-full">
                Informations du produit
              </legend>
              <div className={`grid ${gridClass} gap-3 sm:gap-4 ${mobileClass}`}>
                {renderField('produit', 'Produit', 'select',
                  produits.length > 0 ? produits.map(prod => ({
                    value: prod.id,
                    label: prod.nom
                  })) : []
                )}
                {renderField('categorieCode', 'Catégorie', 'select',
                  categoriesDisponibles.map(cat => ({
                    value: cat.code,
                    label: `${cat.code} - ${cat.libelle}`
                  }))
                )}
              </div>
            </fieldset>

            {/* Informations du véhicule */}
            <fieldset className="border border-gray-200/50 dark:border-gray-600/50 rounded-2xl p-4 sm:p-6 bg-white/60 dark:bg-gray-800/60 backdrop-blur-sm shadow-lg hover:shadow-xl transition-all duration-300">
              <legend className="text-base sm:text-lg font-semibold text-gray-900 dark:text-white px-3 py-1 bg-gradient-to-r from-indigo-50 to-purple-50 dark:from-indigo-900/20 dark:to-purple-900/20 rounded-full">
                Informations du véhicule
              </legend>
              <div className={`grid ${gridClass} gap-3 sm:gap-4 ${mobileClass}`}>
                {renderField('dateMiseEnCirculation', 'Date de mise en circulation', 'date')}
                {renderField('immatriculation', 'Numéro d\'immatriculation', 'text')}
                {renderField('couleur', 'Couleur', 'text')}
                {renderField('nombreDeSieges', 'Nombre de sièges', 'number')}
                {renderField('nombreDePortes', 'Nombre de portes', 'number')}
                {renderField('puissanceFiscale', 'Puissance fiscale', 'number')}
                {renderField('valeurNeuf', 'Valeur à neuf', 'number')}
                {renderField('vehiculeValeurVenale', 'Valeur vénale', 'number')}
              </div>
            </fieldset>
          </div>
        );
      case 1:
        return (
          <fieldset className="border border-gray-200/50 dark:border-gray-600/50 rounded-2xl p-4 sm:p-6 bg-white/60 dark:bg-gray-800/60 backdrop-blur-sm shadow-lg hover:shadow-xl transition-all duration-300">
            <legend className="text-base sm:text-lg font-semibold text-gray-900 dark:text-white px-3 py-1 bg-gradient-to-r from-indigo-50 to-purple-50 dark:from-indigo-900/20 dark:to-purple-900/20 rounded-full">
              Informations de l'assuré
            </legend>
            <div className={`grid ${gridClass} gap-3 sm:gap-4 ${mobileClass}`}>
              {renderField('nom', 'Nom', 'text', undefined, false)}
              {renderField('prenoms', 'Prénoms', 'text', undefined, false)}
              {renderField('numeroCarteIdentite', 'Numéro de carte d\'identité', 'text', undefined, false)}
              {renderField('email', 'Email', 'email', undefined, false)}
              {renderField('sexe', 'Sexe', 'select', [
                { value: 'M', label: 'Masculin' },
                { value: 'F', label: 'Féminin' }
              ], false)}
              {renderField('dateNaissance', 'Date de naissance', 'date', undefined, false)}
              {renderField('lieuNaissance', 'Lieu de naissance', 'text', undefined, false)}
              {renderField('adresse', 'Adresse', 'text', undefined, false)}
              {renderField('telephone', 'Téléphone', 'tel', undefined, false)}
            </div>
          </fieldset>
        );
      case 2:
        return (
          <div className="bg-gradient-to-br from-gray-50/80 to-indigo-50/30 dark:from-gray-700/50 dark:to-indigo-900/20 p-4 sm:p-6 rounded-2xl backdrop-blur-sm shadow-lg">
            <h2 className="text-lg sm:text-xl font-semibold text-gray-900 dark:text-white mb-4 sm:mb-6 bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent">
              Récapitulatif de la souscription
            </h2>
            <div className={`grid ${gridClass} gap-4 sm:gap-6 ${mobileClass}`}>
              {/* Informations du véhicule */}
              <div className="bg-white/80 dark:bg-gray-800/80 p-4 sm:p-6 rounded-2xl shadow-lg hover:shadow-xl transition-all duration-300 backdrop-blur-sm border border-gray-200/50 dark:border-gray-700/50">
                <h3 className="text-base sm:text-lg font-semibold text-gray-900 dark:text-white mb-3 sm:mb-4 bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent">
                  Informations du véhicule
                </h3>
                <div className="space-y-2 sm:space-y-3">
                  <div className={`grid ${gridClass} gap-x-3 sm:gap-x-4 gap-y-2 sm:gap-y-3 text-xs sm:text-sm ${mobileClass}`}>
                    <div className="p-2 rounded-lg bg-gray-50/50 dark:bg-gray-700/50">
                      <p className="text-gray-500 dark:text-gray-400">Produit</p>
                      <p className="font-medium text-gray-900 dark:text-white">
                        {(() => {
                          const produitSelectionne = produits.find(p => p.id === formData.produit);
                          return produitSelectionne?.nom || formData.produit;
                        })()}
                      </p>
                    </div>
                    <div className="p-2 rounded-lg bg-gray-50/50 dark:bg-gray-700/50">
                      <p className="text-gray-500 dark:text-gray-400">Date de mise en
                        circulation</p>
                      <p className="font-medium text-gray-900 dark:text-white">
                        {formaterDate(formData.vehicule.dateMiseEnCirculation)}
                      </p>
                    </div>
                    <div className="p-2 rounded-lg bg-gray-50/50 dark:bg-gray-700/50">
                      <p className="text-gray-500 dark:text-gray-400">Valeur vénale</p>
                      <p className="font-medium text-gray-900 dark:text-white">
                        {formData.vehiculeValeurVenale}
                      </p>
                    </div>
                    <div className="p-2 rounded-lg bg-gray-50/50 dark:bg-gray-700/50">
                      <p className="text-gray-500 dark:text-gray-400">Numéro d'immatriculation</p>
                      <p className="font-medium text-gray-900 dark:text-white">{formData.vehicule.immatriculation}</p>
                    </div>
                    <div className="p-2 rounded-lg bg-gray-50/50 dark:bg-gray-700/50">
                      <p className="text-gray-500 dark:text-gray-400">Couleur</p>
                      <p className="font-medium text-gray-900 dark:text-white">{formData.vehicule.couleur}</p>
                    </div>
                    <div className="p-2 rounded-lg bg-gray-50/50 dark:bg-gray-700/50">
                      <p className="text-gray-500 dark:text-gray-400">Catégorie</p>
                      <p className="font-medium text-gray-900 dark:text-white">{formData.vehicule.categorieCode}</p>
                    </div>
                    <div className="p-2 rounded-lg bg-gray-50/50 dark:bg-gray-700/50">
                      <p className="text-gray-500 dark:text-gray-400">Nombre de sièges</p>
                      <p className="font-medium text-gray-900 dark:text-white">{formData.vehicule.nombreDeSieges}</p>
                    </div>
                    <div className="p-2 rounded-lg bg-gray-50/50 dark:bg-gray-700/50">
                      <p className="text-gray-500 dark:text-gray-400">Nombre de portes</p>
                      <p className="font-medium text-gray-900 dark:text-white">{formData.vehicule.nombreDePortes}</p>
                    </div>
                  </div>
                </div>
              </div>

              {/* Informations de l'assuré */}
              <div className="bg-white/80 dark:bg-gray-800/80 p-4 sm:p-6 rounded-2xl shadow-lg hover:shadow-xl transition-all duration-300 backdrop-blur-sm border border-gray-200/50 dark:border-gray-700/50">
                <h3 className="text-base sm:text-lg font-semibold text-gray-900 dark:text-white mb-3 sm:mb-4 bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent">
                  Informations de l'assuré
                </h3>
                <div className="space-y-2 sm:space-y-3">
                  <div className={`grid ${gridClass} gap-x-3 sm:gap-x-4 gap-y-2 sm:gap-y-3 text-xs sm:text-sm ${mobileClass}`}>
                    <div className="p-2 rounded-lg bg-gray-50/50 dark:bg-gray-700/50">
                      <p className="text-gray-500 dark:text-gray-400">Nom</p>
                      <p className="font-medium text-gray-900 dark:text-white">{formData.assure.nom}</p>
                    </div>
                    <div className="p-2 rounded-lg bg-gray-50/50 dark:bg-gray-700/50">
                      <p className="text-gray-500 dark:text-gray-400">Prénoms</p>
                      <p className="font-medium text-gray-900 dark:text-white">{formData.assure.prenoms}</p>
                    </div>
                    <div className="p-2 rounded-lg bg-gray-50/50 dark:bg-gray-700/50">
                      <p className="text-gray-500 dark:text-gray-400">Numéro de carte
                        d'identité</p>
                      <p className="font-medium text-gray-900 dark:text-white">{formData.assure.numeroCarteIdentite}</p>
                    </div>
                    <div className="p-2 rounded-lg bg-gray-50/50 dark:bg-gray-700/50">
                      <p className="text-gray-500 dark:text-gray-400">Email</p>
                      <p className="font-medium text-gray-900 dark:text-white">{formData.assure.email}</p>
                    </div>
                    <div className="p-2 rounded-lg bg-gray-50/50 dark:bg-gray-700/50">
                      <p className="text-gray-500 dark:text-gray-400">Sexe</p>
                      <p className="font-medium text-gray-900 dark:text-white">{formData.assure.sexe}</p>
                    </div>
                    <div className="p-2 rounded-lg bg-gray-50/50 dark:bg-gray-700/50">
                      <p className="text-gray-500 dark:text-gray-400">Date de naissance</p>
                      <p className="font-medium text-gray-900 dark:text-white">{formaterDate(formData.assure.dateNaissance)}</p>
                    </div>
                    <div className="p-2 rounded-lg bg-gray-50/50 dark:bg-gray-700/50">
                      <p className="text-gray-500 dark:text-gray-400">Lieu de naissance</p>
                      <p className="font-medium text-gray-900 dark:text-white">{formData.assure.lieuNaissance}</p>
                    </div>
                    <div className="p-2 rounded-lg bg-gray-50/50 dark:bg-gray-700/50">
                      <p className="text-gray-500 dark:text-gray-400">Téléphone</p>
                      <p className="font-medium text-gray-900 dark:text-white">{formData.assure.telephone}</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div className="mt-4 sm:mt-6">
              <div className="bg-gradient-to-r from-yellow-50/80 to-orange-50/60 dark:from-yellow-900/20 dark:to-orange-900/20 p-3 sm:p-4 rounded-2xl backdrop-blur-sm border border-yellow-200/50 dark:border-yellow-800/50 shadow-lg">
                <p className="text-xs sm:text-sm text-yellow-800 dark:text-yellow-200">
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
      <h1 className="text-lg sm:text-xl md:text-2xl font-extrabold bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent pb-2 sm:pb-3 pt-2 sm:pt-3 gap-4">
        Nouvelle Souscription
      </h1>
      <div className="bg-white/80 dark:bg-gray-800/80 backdrop-blur-sm shadow-2xl border border-gray-200/50 dark:border-gray-700/50 w-full max-w-7xl mx-auto rounded-2xl overflow-hidden">
        <div className="p-4 sm:p-6">
          {apiError && (
            <div
              className="mb-4 sm:mb-6 bg-red-50/80 dark:bg-red-900/20 backdrop-blur-sm border border-red-200/50 dark:border-red-800/50 p-3 sm:p-4 rounded-xl shadow-lg">
              <p className="text-xs sm:text-sm text-red-600 dark:text-red-400">
                {apiError}
              </p>
            </div>
          )}

          {/* Stepper */}
          <div className="mb-4">
            <Steps
              model={steps}
              activeIndex={currentStep}
              readOnly={false}
              className={classNames('custom-steps', { 'steps-mobile': !isDesktop })}
            />
          </div>

          <form onSubmit={(e) => e.preventDefault()}>
            {/* Contenu de l'étape courante */}
            <div className="mb-6 sm:mb-8">
              {renderStepContent(currentStep)}
            </div>

            {/* Boutons de navigation */}
            <div className="w-full mt-4 sm:mt-6">
              <div className="flex gap-3 sm:gap-4 justify-end">
                {currentStep > 0 && (
                  <button
                    type="button"
                    onClick={handleBack}
                    className="px-3 sm:px-4 py-2 text-xs sm:text-sm min-w-[120px] sm:min-w-[150px] border border-gray-300/50 text-gray-700 bg-white/80 hover:bg-gray-50/90 backdrop-blur-sm focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 rounded-xl shadow-lg hover:shadow-xl transition-all duration-200"
                  >
                    Précédent
                  </button>
                )}
                <button
                  type="button"
                  onClick={currentStep === 2 ? handleSubmit : handleNext}
                  disabled={isLoading || Object.keys(validationErrors).length > 0}
                  className="px-3 sm:px-4 py-2 text-xs sm:text-sm min-w-[120px] sm:min-w-[150px] border border-transparent text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed rounded-xl shadow-lg hover:shadow-xl transition-all duration-200 transform hover:scale-105"
                >
                  {isLoading ? (
                    <span className="flex items-center">
                      <svg className="animate-spin -ml-1 mr-2 h-3 w-3 sm:h-4 sm:w-4 text-white"
                        xmlns="http://www.w3.org/2000/svg"
                        fill="none" viewBox="0 0 24 24">
                        <circle className="opacity-25" cx="12" cy="12" r="10"
                          stroke="currentColor"
                          strokeWidth="4"></circle>
                        <path className="opacity-75" fill="currentColor"
                          d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                      </svg>
                      <span className="text-xs sm:text-sm">
                        {currentStep === 2 ? 'Création en cours...' : 'Chargement...'}
                      </span>
                    </span>
                  ) : currentStep === 2 ? (
                    'Créer la souscription'
                  ) : (
                    'Suivant'
                  )}
                </button>
              </div>
            </div>
          </form>
        </div>
      </div>
    </>
  );
};

export default CreerSouscription; 