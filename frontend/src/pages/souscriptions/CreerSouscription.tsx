import { formaterDate } from '@/utils/dateUtils';
import { useToast } from '@contexts/ToastContext.tsx';
import { useMediaQuery } from '@hooks/useMediaQuery';
import { produitHttpService } from "@services/produit.http-service.ts";
import { Button } from 'primereact/button';
import { MenuItem } from 'primereact/menuitem';
import { Steps } from 'primereact/steps';
import { classNames } from 'primereact/utils';
import { useEffect, useMemo, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { DropdownField, InputNumberField } from '@/components/form';
import InputDateField from '@/components/form/InputDateField';
import InputTextField from '@/components/form/InputTextField';
import { Categorie, Produit } from '@/services/devis.http-service';
import { SouscriptionData, souscriptionHttpService, Vehicule, Assure } from '@/services/souscription.http-service';

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
      nombreDeSieges: 0,
      nombreDePortes: 0,
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
          value = String(formData.vehicule[field as keyof Vehicule] ?? '');
        }
      } else if (stepIndex === 1) {
        value = String(formData.assure[field as keyof Assure] ?? '');
      }

      if (!value || (typeof value === 'string' && value.trim() === '')) {
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

  const handleFieldChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement> | { target: { name: string; value: any } }) => {
    const { name, value } = e.target;
    setTouchedFields(prev => new Set([...prev, name]));

    if (name === 'produit') {
      handleProduitChange(value);
    } else if (name === 'vehiculeValeurVenale') {
      setFormData(prev => ({
        ...prev,
        vehiculeValeurVenale: Number(value)
      }));
    } else if (['nom', 'prenoms', 'sexe', 'dateNaissance', 'lieuNaissance', 'numeroCarteIdentite', 'telephone', 'adresse', 'email'].includes(name)) {
      setFormData(prev => ({
        ...prev,
        assure: {
          ...prev.assure,
          [name]: value
        }
      }));
    } else {
      setFormData(prev => ({
        ...prev,
        vehicule: {
          ...prev.vehicule,
          [name]: ['nombreDeSieges', 'nombreDePortes', 'puissanceFiscale', 'valeurNeuf'].includes(name)
            ? Number(value)
            : value
        }
      }));
    }

    if (validationErrors[name]) {
      setValidationErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }

    validateField(name, value);
  };

  const handleProduitChange = (produitId: string) => {
    const produitSelectionne = produits.find(p => p.id === produitId);

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

  const validateField = (fieldName: string, value: any) => {
    const errors: Record<string, string> = {};

    const stringValue = value != null ? String(value) : '';

    if (!value || stringValue.trim() === '') {
      errors[fieldName] = 'Ce champ est requis';
    } else {
      switch (fieldName) {
        case 'telephone':
          if (!/^\+?[\\d]{8,}$/.test(stringValue)) {
            errors[fieldName] = 'Numéro de téléphone invalide';
          }
          break;
        case 'immatriculation':
          if (!/^[A-Z0-9\s]+$/i.test(stringValue)) {
            errors[fieldName] = 'Le numéro d\'immatriculation ne doit contenir que des lettres et des chiffres';
          }
          break;
        case 'email':
          if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(stringValue)) {
            errors[fieldName] = 'Adresse email invalide';
          }
          break;
        case 'dateMiseEnCirculation':
        case 'dateNaissance': {
          const date = new Date(stringValue);
          const now = new Date();
          if (date > now) {
            errors[fieldName] = 'La date ne peut pas être dans le futur';
          }
          break;
        }
        case 'nombreDeSieges': {
          const sieges = parseInt(stringValue);
          if (isNaN(sieges) || sieges < 1 || sieges > 9) {
            errors[fieldName] = 'Le nombre de sièges doit être entre 1 et 9';
          }
          break;
        }
        case 'nombreDePortes': {
          const portes = parseInt(stringValue);
          if (isNaN(portes) || portes < 2 || portes > 5) {
            errors[fieldName] = 'Le nombre de portes doit être entre 2 et 5';
          }
          break;
        }
        case 'puissanceFiscale': {
          const puissance = parseInt(stringValue);
          if (isNaN(puissance) || puissance < 1) {
            errors[fieldName] = 'La puissance fiscale doit être supérieure à 0';
          }
          break;
        }
        case 'valeurNeuf':
        case 'vehiculeValeurVenale': {
          const valeur = parseFloat(stringValue);
          if (isNaN(valeur) || valeur <= 0) {
            errors[fieldName] = 'La valeur doit être supérieure à 0';
          }
          break;
        }
        case 'numeroCarteIdentite':
          if (!/^[A-Z0-9]{5,}$/.test(stringValue.toUpperCase())) {
            errors[fieldName] = 'Numéro de carte d\'identité invalide';
          }
          break;
      }
    }

    setValidationErrors(prev => {
      const newErrors = { ...prev };
      if (errors[fieldName]) {
        newErrors[fieldName] = errors[fieldName];
      } else {
        delete newErrors[fieldName];
      }
      return newErrors;
    });
  };

  useEffect(() => {
    updateStepStatus(currentStep);
  }, [formData, currentStep]);

  const isCurrentStepValid = useMemo(() => {
    const stepFields = {
      0: ['produit', 'dateMiseEnCirculation', 'immatriculation', 'couleur', 'categorieCode', 'nombreDeSieges', 'nombreDePortes', 'puissanceFiscale', 'valeurNeuf'],
      1: ['nom', 'prenoms', 'numeroCarteIdentite', 'email', 'sexe', 'dateNaissance', 'lieuNaissance', 'adresse', 'telephone'],
      2: []
    };
    const currentStepFields = stepFields[currentStep as keyof typeof stepFields];

    return currentStepFields.every((field) => {
      let value = '';
      if (currentStep === 0) {
        if (field === 'produit') {
          value = String(formData.produit || '');
        } else {
          value = String(formData.vehicule[field as keyof Vehicule] ?? '');
        }
      } else if (currentStep === 1) {
        value = String(formData.assure[field as keyof Assure] ?? '');
      }

      return value &&  value.trim() !== '' && !validationErrors[field];
    });
  }, [formData, currentStep, validationErrors]);

  const handleNext = () => {
    if (isCurrentStepValid) {
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
      const produitSelectionne = produits.find(p => p.id === formData.produit);
      const nomProduit = produitSelectionne?.nom ?? '';

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

      const response = await souscriptionHttpService.creer(submitData);

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

  const renderStepContent = (step: number) => {
    const isVerySmallScreen = screenWidth <= 500;
    const gridClass = isVerySmallScreen ? 'grid-cols-1' : 'grid-cols-1 custom-grid-cols-2';

    switch (step) {
      case 0:
        return (
          <div className="space-y-4 sm:space-y-6">
            <fieldset className="border border-gray-200/50 dark:border-gray-600/50 rounded-2xl p-4 sm:p-6 bg-white/60 dark:bg-gray-800/60 backdrop-blur-sm shadow-lg hover:shadow-xl transition-all duration-300">
              <legend className="text-base sm:text-lg font-semibold text-gray-900 dark:text-white px-3 py-1 bg-gradient-to-r from-indigo-50 to-purple-50 dark:from-indigo-900/20 dark:to-purple-900/20 rounded-full">
                Informations du produit
              </legend>
              <div className={`grid ${gridClass} gap-3 sm:gap-4`}>
                <DropdownField
                  id="produit"
                  name="produit"
                  label="Produit"
                  options={produits.map(prod => ({
                    label: prod.nom,
                    value: prod.id
                  }))}
                  value={formData.produit}
                  onChange={handleFieldChange}
                  error={validationErrors.produit}
                  placeholder="Sélectionnez un produit"
                  required
                />
                <DropdownField
                  id="categorieCode"
                  name="categorieCode"
                  label="Catégorie"
                  options={categoriesDisponibles.map(cat => ({
                    label: `${cat.code} - ${cat.libelle}`,
                    value: cat.code
                  }))}
                  value={formData.vehicule.categorieCode}
                  onChange={handleFieldChange}
                  error={validationErrors.categorieCode}
                  placeholder="Sélectionnez une catégorie"
                  required
                  disabled={categoriesDisponibles.length === 0}
                />
              </div>
            </fieldset>

            <fieldset className="border border-gray-200/50 dark:border-gray-600/50 rounded-2xl p-4 sm:p-6 bg-white/60 dark:bg-gray-800/60 backdrop-blur-sm shadow-lg hover:shadow-xl transition-all duration-300">
              <legend className="text-base sm:text-lg font-semibold text-gray-900 dark:text-white px-3 py-1 bg-gradient-to-r from-indigo-50 to-purple-50 dark:from-indigo-900/20 dark:to-purple-900/20 rounded-full">
                Informations du véhicule
              </legend>
              <div className={`grid ${gridClass} gap-3 sm:gap-4`}>
                <InputDateField
                  id="dateMiseEnCirculation"
                  name="dateMiseEnCirculation"
                  label="Date de mise en circulation"
                  value={formData.vehicule.dateMiseEnCirculation}
                  onChange={handleFieldChange}
                  error={validationErrors.dateMiseEnCirculation}
                  required
                />
                <InputTextField
                  id="immatriculation"
                  name="immatriculation"
                  label="Numéro d'immatriculation"
                  value={formData.vehicule.immatriculation}
                  onChange={handleFieldChange}
                  error={validationErrors.immatriculation}
                  required
                />
                <InputTextField
                  id="couleur"
                  name="couleur"
                  label="Couleur"
                  value={formData.vehicule.couleur}
                  onChange={handleFieldChange}
                  error={validationErrors.couleur}
                  required
                />
                <InputNumberField
                  id="nombreDeSieges"
                  name="nombreDeSieges"
                  label="Nombre de sièges"
                  value={formData.vehicule.nombreDeSieges}
                  onChange={handleFieldChange}
                  error={validationErrors.nombreDeSieges}
                  min={1}
                  max={9}
                  required
                />
                <InputNumberField
                  id="nombreDePortes"
                  name="nombreDePortes"
                  label="Nombre de portes"
                  value={formData.vehicule.nombreDePortes}
                  onChange={handleFieldChange}
                  error={validationErrors.nombreDePortes}
                  min={2}
                  max={10}
                  required
                />
                <InputNumberField
                  id="puissanceFiscale"
                  name="puissanceFiscale"
                  label="Puissance fiscale"
                  value={formData.vehicule.puissanceFiscale}
                  onChange={handleFieldChange}
                  error={validationErrors.puissanceFiscale}
                  min={0}
                  suffix=" CV"
                  required
                />
                <InputNumberField
                  id="valeurNeuf"
                  name="valeurNeuf"
                  label="Valeur à neuf"
                  value={formData.vehicule.valeurNeuf}
                  onChange={handleFieldChange}
                  error={validationErrors.valeurNeuf}
                  mode="currency"
                  currency="XOF"
                  required
                />
                <InputNumberField
                  id="vehiculeValeurVenale"
                  name="vehiculeValeurVenale"
                  label="Valeur vénale"
                  value={formData.vehiculeValeurVenale}
                  onChange={handleFieldChange}
                  error={validationErrors.vehiculeValeurVenale}
                  mode="currency"
                  currency="XOF"
                  required
                />
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
            <div className={`grid ${gridClass} gap-3 sm:gap-4`}>
              <InputTextField
                id="nom"
                name="nom"
                label="Nom"
                value={formData.assure.nom}
                onChange={handleFieldChange}
                error={validationErrors.nom}
                required
              />
              <InputTextField
                id="prenoms"
                name="prenoms"
                label="Prénoms"
                value={formData.assure.prenoms}
                onChange={handleFieldChange}
                error={validationErrors.prenoms}
                required
              />
              <InputTextField
                id="numeroCarteIdentite"
                name="numeroCarteIdentite"
                label="Numéro de carte d'identité"
                value={formData.assure.numeroCarteIdentite}
                onChange={handleFieldChange}
                error={validationErrors.numeroCarteIdentite}
                required
              />
              <InputTextField
                id="email"
                name="email"
                label="Email"
                type="email"
                value={formData.assure.email}
                onChange={handleFieldChange}
                error={validationErrors.email}
                required
              />
              <DropdownField
                id="sexe"
                name="sexe"
                label="Sexe"
                options={[
                  { label: 'Masculin', value: 'M' },
                  { label: 'Féminin', value: 'F' }
                ]}
                value={formData.assure.sexe}
                onChange={handleFieldChange}
                error={validationErrors.sexe}
                required
              />
              <InputDateField
                id="dateNaissance"
                name="dateNaissance"
                label="Date de naissance"
                value={formData.assure.dateNaissance}
                onChange={handleFieldChange}
                error={validationErrors.dateNaissance}
                required
              />
              <InputTextField
                id="lieuNaissance"
                name="lieuNaissance"
                label="Lieu de naissance"
                value={formData.assure.lieuNaissance}
                onChange={handleFieldChange}
                error={validationErrors.lieuNaissance}
                required
              />
              <InputTextField
                id="adresse"
                name="adresse"
                label="Adresse"
                value={formData.assure.adresse}
                onChange={handleFieldChange}
                error={validationErrors.adresse}
                required
              />
              <InputTextField
                id="telephone"
                name="telephone"
                label="Téléphone"
                type="tel"
                value={formData.assure.telephone}
                onChange={handleFieldChange}
                error={validationErrors.telephone}
                required
              />
            </div>
          </fieldset>
        );
      case 2:
        return (
          <div className="bg-gradient-to-br from-gray-50/80 to-indigo-50/30 dark:from-gray-700/50 dark:to-indigo-900/20 p-4 sm:p-6 rounded-2xl backdrop-blur-sm shadow-lg">
            <h2 className="text-lg sm:text-xl font-semibold text-gray-900 dark:text-white mb-4 sm:mb-6 bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent">
              Récapitulatif de la souscription
            </h2>
            <div className={`grid ${gridClass} gap-4 sm:gap-6`}>
              <div className="bg-white/80 dark:bg-gray-800/80 p-4 sm:p-6 rounded-2xl shadow-lg hover:shadow-xl transition-all duration-300 backdrop-blur-sm border border-gray-200/50 dark:border-gray-700/50">
                <h3 className="text-base sm:text-lg font-semibold text-gray-900 dark:text-white mb-3 sm:mb-4 bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent">
                  Informations du véhicule
                </h3>
                <div className="space-y-2 sm:space-y-3">
                  <div className={`grid ${gridClass} gap-x-3 sm:gap-x-4 gap-y-2 sm:gap-y-3 text-xs sm:text-sm`}>
                    <div className="p-2 rounded-lg bg-gray-50/50 dark:bg-gray-700/50">
                      <p className="text-gray-500 dark:text-gray-400">Produit</p>
                      <p className="font-medium text-gray-900 dark:text-white">
                        {(() => {
                          const produitSelectionne = produits.find(p => p.id === formData.produit);
                          return produitSelectionne?.nom ?? formData.produit;
                        })()}
                      </p>
                    </div>
                    <div className="p-2 rounded-lg bg-gray-50/50 dark:bg-gray-700/50">
                      <p className="text-gray-500 dark:text-gray-400">Date de mise en circulation</p>
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

              <div className="bg-white/80 dark:bg-gray-800/80 p-4 sm:p-6 rounded-2xl shadow-lg hover:shadow-xl transition-all duration-300 backdrop-blur-sm border border-gray-200/50 dark:border-gray-700/50">
                <h3 className="text-base sm:text-lg font-semibold text-gray-900 dark:text-white mb-3 sm:mb-4 bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent">
                  Informations de l'assuré
                </h3>
                <div className="space-y-2 sm:space-y-3">
                  <div className={`grid ${gridClass} gap-x-3 sm:gap-x-4 gap-y-2 sm:gap-y-3 text-xs sm:text-sm`}>
                    <div className="p-2 rounded-lg bg-gray-50/50 dark:bg-gray-700/50">
                      <p className="text-gray-500 dark:text-gray-400">Nom</p>
                      <p className="font-medium text-gray-900 dark:text-white">{formData.assure.nom}</p>
                    </div>
                    <div className="p-2 rounded-lg bg-gray-50/50 dark:bg-gray-700/50">
                      <p className="text-gray-500 dark:text-gray-400">Prénoms</p>
                      <p className="font-medium text-gray-900 dark:text-white">{formData.assure.prenoms}</p>
                    </div>
                    <div className="p-2 rounded-lg bg-gray-50/50 dark:bg-gray-700/50">
                      <p className="text-gray-500 dark:text-gray-400">Numéro de carte d'identité</p>
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
            <div className="mb-4 sm:mb-6 bg-red-50/80 dark:bg-red-900/20 backdrop-blur-sm border border-red-200/50 dark:border-red-800/50 p-3 sm:p-4 rounded-xl shadow-lg">
              <p className="text-xs sm:text-sm text-red-600 dark:text-red-400">
                {apiError}
              </p>
            </div>
          )}

          <div className="mb-4">
            <Steps
              model={steps}
              activeIndex={currentStep}
              readOnly={false}
              className={classNames('custom-steps', { 'steps-mobile': !isDesktop })}
            />
          </div>

          <form onSubmit={(e) => e.preventDefault()}>
            <div className="mb-6 sm:mb-8">
              {renderStepContent(currentStep)}
            </div>

            <div className="w-full mt-4 sm:mt-6">
              <div className="flex gap-3 sm:gap-4 justify-end">
                {currentStep > 0 && (
                  <Button
                    label="Précédent"
                    onClick={handleBack}
                    severity='secondary'
                    className="px-3 sm:px-4 py-2 text-xs sm:text-sm min-w-[120px] sm:min-w-[150px] border border-transparent text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed rounded-xl shadow-lg hover:shadow-xl transition-all duration-200 transform hover:scale-105"
                  />
                )}
                <Button
                  label={currentStep === 2 ? (isLoading ? 'Création en cours...' : 'Créer la souscription') : 'Suivant'}
                  onClick={currentStep === 2 ? handleSubmit : handleNext}
                  disabled={isLoading || !isCurrentStepValid}
                  className="px-3 sm:px-4 py-2 text-xs sm:text-sm min-w-[120px] sm:min-w-[150px] border border-transparent text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed rounded-xl shadow-lg hover:shadow-xl transition-all duration-200 transform hover:scale-105"
                >
                  {isLoading && currentStep === 2 && (
                    <span className="flex items-center">
                      <svg className="animate-spin -ml-1 mr-2 h-3 w-3 sm:h-4 sm:w-4 text-white"
                        xmlns="http://www.w3.org/2000/svg"
                        fill="none" viewBox="0 0 24 24">
                        <circle className="opacity-25" cx="12" cy="12" r="10"
                          stroke="currentColor" strokeWidth="4"></circle>
                        <path className="opacity-75" fill="currentColor"
                          d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 714 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                      </svg>
                    </span>
                  )}
                </Button>
              </div>
            </div>
          </form>
        </div>
      </div>
    </>
  );
};

export default CreerSouscription;