import { API_ENDPOINTS } from '@/config/api';
import { useMediaQuery } from '@hooks/useMediaQuery';
import { http } from '@services/http';
import { MenuItem } from 'primereact/menuitem';
import { Steps } from 'primereact/steps';
import { classNames } from 'primereact/utils';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useToast } from '@contexts/ToastContext.tsx';
import { Button } from 'primereact/button';
import { produitHttpService } from "@services/produit.http-service.ts";

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
  const [currentStep, setCurrentStep] = useState<number>(0);
  const [isLoading, setIsLoading] = useState(false);
  const [validationErrors, setValidationErrors] = useState<Partial<Record<string, string>>>({});
  const [apiError, setApiError] = useState<string | null>(null);
  const [completedSteps, setCompletedSteps] = useState<number[]>([]);
  const [produits, setProduits] = useState<Produit[]>([]);
  const [categoriesDisponibles, setCategoriesDisponibles] = useState<Categorie[]>([]);

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

  const handleProduitChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const produitSelectionne = produits.find(p => p.id === e.target.value);
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
  };

  const handleVehiculeValeurVenaleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { value } = e.target;
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

      const response = await http.post<{ status: string, data: any }>(
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
      <div className="mb-4">
        <label htmlFor={name}
          className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
          {label}
        </label>
        <div>
          {type === 'select' ? (
            <select
              id={name}
              name={name}
              value={value?.toString() || ''}
              onChange={getChangeHandler() as React.ChangeEventHandler<HTMLSelectElement>}
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
              value={value?.toString() || ''}
              onChange={getChangeHandler() as React.ChangeEventHandler<HTMLInputElement>}
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
              {renderField('produit', 'Produit', 'select',
                produits.length > 0 ? produits.map(prod => ({
                  value: prod.id,
                  label: prod.nom
                })) : []
              )}
              {renderField('dateMiseEnCirculation', 'Date de mise en circulation', 'date')}
              {renderField('immatriculation', 'Numéro d\'immatriculation', 'text')}
              {renderField('couleur', 'Couleur', 'text')}
              {renderField('categorieCode', 'Catégorie', 'select',
                categoriesDisponibles.map(cat => ({
                  value: cat.code,
                  label: `${cat.code} - ${cat.libelle}`
                }))
              )}
              {renderField('nombreDeSieges', 'Nombre de sièges', 'number')}
              {renderField('nombreDePortes', 'Nombre de portes', 'number')}
              {renderField('puissanceFiscale', 'Puissance fiscale', 'number')}
              {renderField('valeurNeuf', 'Valeur à neuf', 'number')}
              {renderField('vehiculeValeurVenale', 'Valeur vénale', 'number')}
            </div>
          </div>
        );
      case 1:
        return (
          <div className="bg-gray-50 dark:bg-gray-700/50 p-6 rounded-lg space-y-8">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
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
                      <p className="text-gray-500 dark:text-gray-400">Produit</p>
                      <p className="font-medium text-gray-900 dark:text-white">
                        {(() => {
                          const produitSelectionne = produits.find(p => p.id === formData.produit);
                          return produitSelectionne?.nom || formData.produit;
                        })()}
                      </p>
                    </div>
                    <div>
                      <p className="text-gray-500 dark:text-gray-400">Date de mise en
                        circulation</p>
                      <p className="font-medium text-gray-900 dark:text-white">
                        {formatDate(formData.vehicule.dateMiseEnCirculation)}
                      </p>
                    </div>
                    <div>
                      <p className="text-gray-500 dark:text-gray-400">Valeur vénale</p>
                      <p className="font-medium text-gray-900 dark:text-white">
                        {formData.vehiculeValeurVenale}
                      </p>
                    </div>
                    <div>
                      <p className="text-gray-500 dark:text-gray-400">Numéro d'immatriculation</p>
                      <p className="font-medium text-gray-900 dark:text-white">{formData.vehicule.immatriculation}</p>
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
                      <p className="text-gray-500 dark:text-gray-400">Prénoms</p>
                      <p className="font-medium text-gray-900 dark:text-white">{formData.assure.prenoms}</p>
                    </div>
                    <div>
                      <p className="text-gray-500 dark:text-gray-400">Numéro de carte
                        d'identité</p>
                      <p className="font-medium text-gray-900 dark:text-white">{formData.assure.numeroCarteIdentite}</p>
                    </div>
                    <div>
                      <p className="text-gray-500 dark:text-gray-400">Email</p>
                      <p className="font-medium text-gray-900 dark:text-white">{formData.assure.email}</p>
                    </div>
                    <div>
                      <p className="text-gray-500 dark:text-gray-400">Sexe</p>
                      <p className="font-medium text-gray-900 dark:text-white">{formData.assure.sexe}</p>
                    </div>
                    <div>
                      <p className="text-gray-500 dark:text-gray-400">Date de naissance</p>
                      <p className="font-medium text-gray-900 dark:text-white">{formatDate(formData.assure.dateNaissance)}</p>
                    </div>
                    <div>
                      <p className="text-gray-500 dark:text-gray-400">Lieu de naissance</p>
                      <p className="font-medium text-gray-900 dark:text-white">{formData.assure.lieuNaissance}</p>
                    </div>
                    <div>
                      <p className="text-gray-500 dark:text-gray-400">Téléphone</p>
                      <p className="font-medium text-gray-900 dark:text-white">{formData.assure.telephone}</p>
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
      <div
        className="bg-white dark:bg-gray-800 shadow-lg w-full max-w-7xl mx-auto min-h-[calc(100vh-7rem)]">
        <div className="p-6">
          {apiError && (
            <div
              className="mb-6 bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 p-4 rounded-lg">
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