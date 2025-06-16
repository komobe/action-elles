import { Button } from 'primereact/button';
import React, { useEffect, useState } from 'react';
import { useMediaQuery } from '@hooks/useMediaQuery.ts';
import {
  type Categorie,
  devisHttpService,
  type Produit,
  type SimulationDevisRequest,
  type SimulationResponse
} from '@/services/devis.http-service';
import { produitHttpService } from "@services/produit.http-service.ts";
import { formaterDate, formaterMontant, calculerJoursRestants } from '@/utils/dateUtils';

const SimulerDevis = () => {
  const isDesktop = useMediaQuery('(min-width: 1024px)');
  const [currentStep, setCurrentStep] = useState<number>(1);
  const [selectedProduitId, setSelectedProduitId] = useState<string>('');
  const [formData, setFormData] = useState<SimulationDevisRequest>({
    produit: '',
    categorie: '',
    puissanceFiscale: 0,
    vehiculeImmatriculation: '',
    dateDeMiseEnCirculation: '',
    valeurNeuf: 0,
    valeurVenale: 0
  } as SimulationDevisRequest);

  const [produits, setProduits] = useState<Produit[]>([]);
  const [categoriesDisponibles, setCategoriesDisponibles] = useState<Categorie[]>([]);
  const [isLoadingData, setIsLoadingData] = useState(true);

  type SimulationResponseData = SimulationResponse['data'];

  const [result, setResult] = useState<SimulationResponseData | null>(null);
  const [error, setError] = useState<string>('');
  const [isLoading, setIsLoading] = useState(false);
  const [validationErrors, setValidationErrors] = useState<Partial<Record<keyof SimulationDevisRequest, string>>>({});

  const steps = [
    {
      number: 1,
      title: 'Informations du produit',
      fields: ['produit', 'categorie']
    },
    {
      number: 2,
      title: 'Informations du véhicule',
      fields: ['puissanceFiscale', 'vehiculeImmatriculation', 'dateDeMiseEnCirculation', 'valeurNeuf', 'valeurVenale']
    },
    {
      number: 3,
      title: 'Résultat',
      fields: []
    }
  ];

  useEffect(() => {
    const fetchData = async () => {
      setIsLoadingData(true);
      try {
        const produitsResponse = await produitHttpService.lister();

        if (produitsResponse.status === 'success' && produitsResponse.data) {
          setProduits(produitsResponse.data);
        }
      } catch (error) {
        console.error('Erreur lors du chargement des données:', error);
        setError('Erreur lors du chargement des données');
      } finally {
        setIsLoadingData(false);
      }
    };

    fetchData();
  }, []);

  const validateStep = (step: number): boolean => {
    const currentStepFields = steps[step - 1].fields;
    const errors: Partial<Record<keyof SimulationDevisRequest, string>> = {};

    if (currentStepFields.length === 0) return true;

    currentStepFields.forEach((field) => {
      const value = formData[field as keyof SimulationDevisRequest];

      if (value === undefined || value === null || value === '') {
        errors[field as keyof SimulationDevisRequest] = 'Ce champ est requis';
      }

      if (field === 'puissanceFiscale' && (value as number) < 1) {
        errors.puissanceFiscale = 'La puissance fiscale doit être supérieure à 0';
      }

      if (field === 'dateDeMiseEnCirculation') {
        const date = new Date(value as string);
        const now = new Date();
        if (date > now) {
          errors.dateDeMiseEnCirculation = 'La date ne peut pas être dans le futur';
        }
      }

      if (field === 'valeurNeuf' && (value as number) <= 0) {
        errors.valeurNeuf = 'La valeur à neuf doit être supérieure à 0';
      }

      if (field === 'valeurVenale') {
        if ((value as number) <= 0) {
          errors.valeurVenale = 'La valeur vénale doit être supérieure à 0';
        }
        if ((value as number) > formData.valeurNeuf) {
          errors.valeurVenale = 'La valeur vénale ne peut pas être supérieure à la valeur à neuf';
        }
      }

      if (field === 'vehiculeImmatriculation' && (value as string).trim() === '') {
        errors.vehiculeImmatriculation = 'L\'immatriculation est requise';
      }
    });

    setValidationErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleNext = () => {
    if (validateStep(currentStep)) {
      if (currentStep === 2) {
        handleSubmit();
      }
      if (currentStep < steps.length) {
        setCurrentStep(currentStep + 1);
      }
    }
  };

  const handleBack = () => {
    if (currentStep > 1) {
      setCurrentStep(currentStep - 1);
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;

    if (name === 'produit') {
      const produitSelectionne = produits.find(p => p.id === value);
      if (produitSelectionne) {
        setSelectedProduitId(value);
        setCategoriesDisponibles(produitSelectionne.categoriesVehicules || []);
        setFormData(prev => ({
          ...prev,
          [name]: produitSelectionne.nom,
          categorie: ''
        }));
      } else {
        setSelectedProduitId('');
        setCategoriesDisponibles([]);
        setFormData(prev => ({
          ...prev,
          [name]: '',
          categorie: ''
        }));
      }
    } else {
      setFormData(prev => ({
        ...prev,
        [name]: name === 'puissanceFiscale' || name === 'valeurNeuf' || name === 'valeurVenale'
          ? Number(value)
          : value
      }));
    }

    if (validationErrors[name as keyof SimulationDevisRequest]) {
      setValidationErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
    setError('');
  };

  const handleSubmit = async (e?: React.FormEvent) => {
    if (e) {
      e.preventDefault();
    }

    if (!validateStep(1) || !validateStep(2)) {
      return;
    }

    if (isLoading) {
      return;
    }

    setIsLoading(true);
    setResult(null);
    setError('');

    try {
      const response = await devisHttpService.simuler(formData);
      if (response.status === 'success' && response.data) {
        setResult(response.data);
        setValidationErrors({});
        setCurrentStep(3);
        setError('');
      } else {
        setError('Une erreur est survenue lors de la simulation');
      }
    } catch (error) {
      setError('Une erreur est survenue lors de la simulation');
      console.error('Erreur de simulation:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleReset = () => {
    setFormData({
      produit: '',
      categorie: '',
      puissanceFiscale: 0,
      vehiculeImmatriculation: '',
      dateDeMiseEnCirculation: '',
      valeurNeuf: 0,
      valeurVenale: 0
    });
    setSelectedProduitId('');
    setResult(null);
    setError('');
    setValidationErrors({});
    setCurrentStep(1);
  };

  const handleSave = async (e?: React.FormEvent): Promise<void> => {
    e?.preventDefault();

    if (!result) {
      setError('Aucune simulation disponible');
      return;
    }

    const { quoteReference, price, endDate }: SimulationResponseData = result;
    setIsLoading(true);
    setError('');

    try {
      const enregistrerDevisRequest = { quoteReference, price, endDate, ...formData };
      const response = await devisHttpService.enregistrer(enregistrerDevisRequest);
      if (response.status === 'error') {
        setError(response.message ?? 'Erreur lors de l\'enregistrement');
      }
      handleReset();
    } catch (error) {
      setError(error instanceof Error ? error.message : 'Erreur inattendue');
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    setError('');
  }, [currentStep]);

  const renderField = (
    name: keyof SimulationDevisRequest,
    label: string,
    type: 'text' | 'number' | 'date' | 'select',
    options?: { value: string; label: string }[]
  ) => {
    const error = validationErrors[name];

    return (
      <div className="app-form-group">
        <label htmlFor={name} className="app-form-label">
          {label}
        </label>
        <div>
          {type === 'select' ? (
            <select
              id={name}
              name={name}
              value={name === 'produit' ? selectedProduitId : formData[name]?.toString() || ''}
              onChange={handleChange}
              className="app-form-select"
            >
              <option value="">Sélectionnez {label.toLowerCase()}</option>
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
              value={formData[name]?.toString() || ''}
              onChange={handleChange}
              min={type === 'number' ? '0' : undefined}
              step={type === 'number' ? (name === 'puissanceFiscale' ? '1' : '100000') : undefined}
              className="app-form-input"
              placeholder={`Entrez ${label.toLowerCase()}`}
            />
          )}
          {error && (
            <p className="mt-1 text-xs text-red-600 dark:text-red-400">{error}</p>
          )}
        </div>
      </div>
    );
  };

  return (
      <div className="flex flex-col gap-1">
        <div className="flex justify-between items-center">
          <h1 className="text-2xl font-bold text-gray-900 dark:text-white">
            Simuler un devis
          </h1>
        </div>

        <div className="grid lg:grid-cols-12 gap-6">
          {/* Formulaire */}
          <div className="lg:col-span-8 space-y-6">
            <form onSubmit={(e) => e.preventDefault()}>
              {error && (
                <div className="error-message" role="alert">
                  <p>{error}</p>
                </div>
              )}

              <div className="app-form-fieldset">
                <div className="space-y-6">
                  {/* Informations du produit - visible en desktop ou à l'étape 1 en mobile */}
                  <div className={`${!isDesktop && currentStep !== 1 ? 'hidden' : 'block'}`}>
                    <fieldset className="app-form-fieldset">
                      <legend className="app-form-legend">
                        Informations du produit
                      </legend>
                      <div className="grid lg:grid-cols-2 gap-4">
                        {renderField('produit', 'Produit', 'select',
                          produits.map(prod => ({
                            value: prod.id,
                            label: prod.nom
                          }))
                        )}
                        {renderField('categorie', 'Catégorie', 'select',
                          categoriesDisponibles.map(cat => ({
                            value: cat.code,
                            label: `${cat.code} - ${cat.libelle}`
                          }))
                        )}
                      </div>
                    </fieldset>
                  </div>

                  {/* Informations du véhicule - visible en desktop ou à l'étape 2 en mobile */}
                  <div className={`${!isDesktop && currentStep !== 2 ? 'hidden' : 'block'}`}>
                    <fieldset className="app-form-fieldset">
                      <legend className="app-form-legend">
                        Informations du véhicule
                      </legend>
                      <div className="grid lg:grid-cols-2 gap-4">
                        {renderField('puissanceFiscale', 'Puissance fiscale', 'number')}
                        {renderField('vehiculeImmatriculation', 'Immatriculation', 'text')}
                        {renderField('dateDeMiseEnCirculation', 'Date de mise en circulation', 'date')}
                        {renderField('valeurNeuf', 'Valeur à neuf', 'number')}
                        {renderField('valeurVenale', 'Valeur vénale', 'number')}
                      </div>
                    </fieldset>
                  </div>

                  {/* Boutons de navigation */}
                  <div className="flex gap-3 sm:gap-4 justify-end">
                    {!isDesktop && currentStep > 1 && (
                      <button
                        type="button"
                        onClick={handleBack}
                        className="app-form-button"
                      >
                        Précédent
                      </button>
                    )}
                    {!isDesktop && currentStep < steps.length ? (
                      <Button
                        label="Suivant"
                        iconPos="right"
                        icon="pi pi-chevron-right"
                        onClick={handleNext}
                        disabled={isLoading || isLoadingData}
                        className="ml-auto"
                      />
                    ) : (
                      <div className="flex flex-col sm:flex-row gap-4 ml-auto">
                        <Button
                          onClick={handleSubmit}
                          disabled={isLoading || Object.keys(validationErrors).length > 0}
                          className="app-form-button-primary"
                        >
                          {isLoading ? (
                            <span className="flex items-center">
                              <svg className="animate-spin -ml-1 mr-2 h-4 w-4 text-white"
                                xmlns="http://www.w3.org/2000/svg"
                                fill="none" viewBox="0 0 24 24">
                                <circle className="opacity-25" cx="12" cy="12" r="10"
                                  stroke="currentColor"
                                  strokeWidth="4"></circle>
                                <path className="opacity-75" fill="currentColor"
                                  d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                              </svg>
                              Simulation en cours...
                            </span>
                          ) : 'Simuler le devis'}
                        </Button>

                        {result && currentStep === 3 &&
                          <Button
                            onClick={handleSave}
                            disabled={isLoading || (!result && Object.keys(validationErrors).length > 0)}
                            className="app-form-button-primary"
                          >
                            {isLoading ? (
                              <span className="flex items-center">
                                <svg className="animate-spin -ml-1 mr-2 h-4 w-4 text-white"
                                  xmlns="http://www.w3.org/2000/svg"
                                  fill="none" viewBox="0 0 24 24">
                                  <circle className="opacity-25" cx="12" cy="12" r="10"
                                    stroke="currentColor"
                                    strokeWidth="4"></circle>
                                  <path className="opacity-75" fill="currentColor"
                                    d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                                </svg>
                                Simulation en cours...
                              </span>
                            ) : result ? ('Enregistrer le devis') : ('Simuler le devis')}
                          </Button>
                        }
                      </div>
                    )}
                  </div>
                </div>
              </div>
            </form>
          </div>

          {/* Résultat */}
          <div className={`lg:col-span-4 ${!result || !isDesktop ? 'hidden lg:block' : ''}`}>
            {result ? (
              <div className="app-form-fieldset">
                <div>
                  <div className="flex items-center justify-between mb-4">
                    <h2 className="text-lg font-semibold text-gray-900 dark:text-white">
                      Résultat
                    </h2>
                    <span className="text-xs px-2 py-1 bg-green-100 text-green-800 rounded-full">
                      Ref: {result.quoteReference}
                    </span>
                  </div>

                  <div className="space-y-3">
                    {/* Prix */}
                    <div className="bg-white/80 dark:bg-gray-800/80 p-3 rounded-xl border border-indigo-100 dark:border-indigo-900 shadow-sm hover:shadow-md transition-all duration-200">
                      <div className="flex items-center justify-between">
                        <p className="text-sm text-gray-500 dark:text-gray-400">Prime d'assurance</p>
                        <p className="text-xl font-bold text-indigo-600 dark:text-indigo-400">
                          {formaterMontant(result.price)}
                        </p>
                      </div>
                      <div className="mt-1 flex items-center justify-between text-xs">
                        <span className="text-gray-500 dark:text-gray-400">Valable jusqu'au</span>
                        <span className="font-medium text-gray-900 dark:text-white">{formaterDate(result.endDate)}</span>
                      </div>
                    </div>

                    {/* Récapitulatif du véhicule */}
                    <div className="bg-white/80 dark:bg-gray-800/80 p-3 rounded-xl shadow-sm hover:shadow-md transition-all duration-200">
                      <h3 className="text-sm font-medium text-gray-900 dark:text-white mb-2 flex items-center">
                        <svg className="w-4 h-4 mr-1" fill="none" stroke="currentColor"
                          viewBox="0 0 24 24">
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2"
                            d="M19 4H5a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2V6a2 2 0 00-2-2z" />
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2"
                            d="M16 2v4M8 2v4M3 10h18" />
                        </svg>
                        Récapitulatif
                      </h3>

                      {/* Informations du produit */}
                      <div className="mb-3">
                        <h4 className="text-xs font-medium text-gray-700 dark:text-gray-300 mb-1.5">
                          Informations du produit
                        </h4>
                        <div className="space-y-1.5">
                          <div className="flex items-center justify-between text-xs">
                            <span className="text-gray-500 dark:text-gray-400">Produit</span>
                            <span className="font-medium text-gray-900 dark:text-white">{formData.produit}</span>
                          </div>
                          <div className="flex items-center justify-between text-xs">
                            <span className="text-gray-500 dark:text-gray-400">Catégorie</span>
                            <span className="font-medium text-gray-900 dark:text-white">{formData.categorie}</span>
                          </div>
                        </div>
                      </div>

                      {/* Informations du véhicule */}
                      <div>
                        <h4 className="text-xs font-medium text-gray-700 dark:text-gray-300 mb-1.5">
                          Informations du véhicule
                        </h4>
                        <div className="space-y-1.5">
                          <div className="flex items-center justify-between text-xs">
                            <span className="text-gray-500 dark:text-gray-400">Puissance Fiscale</span>
                            <span className="font-medium text-gray-900 dark:text-white">{formData.puissanceFiscale} CV</span>
                          </div>
                          <div className="flex items-center justify-between text-xs">
                            <span className="text-gray-500 dark:text-gray-400">Immatriculation</span>
                            <span className="font-medium text-gray-900 dark:text-white">{formData.vehiculeImmatriculation}</span>
                          </div>
                          <div className="flex items-center justify-between text-xs">
                            <span className="text-gray-500 dark:text-gray-400">Mise en circulation</span>
                            <span className="font-medium text-gray-900 dark:text-white">{formaterDate(formData.dateDeMiseEnCirculation)}</span>
                          </div>
                          <div className="flex items-center justify-between text-xs">
                            <span className="text-gray-500 dark:text-gray-400">Valeur à neuf</span>
                            <span className="font-medium text-gray-900 dark:text-white">{formaterMontant(formData.valeurNeuf)}</span>
                          </div>
                          <div className="flex items-center justify-between text-xs">
                            <span className="text-gray-500 dark:text-gray-400">Valeur vénale</span>
                            <span className="font-medium text-gray-900 dark:text-white">{formaterMontant(formData.valeurVenale)}</span>
                          </div>
                        </div>
                      </div>
                    </div>

                    <div className="bg-indigo-50 dark:bg-indigo-900/20 p-2 rounded-xl">
                      <div className="flex items-start">
                        <svg className="w-3 h-3 text-indigo-500 mt-0.5 mr-1 flex-shrink-0"
                          fill="none"
                          stroke="currentColor" viewBox="0 0 24 24">
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2"
                            d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                        </svg>
                        <p className="text-xs text-indigo-700 dark:text-indigo-300">
                          Cette simulation est valable
                          pendant {calculerJoursRestants(result.endDate)} jours
                          (jusqu'au {formaterDate(result.endDate)}). Pour souscrire à cette offre,
                          veuillez contacter
                          votre agent.
                        </p>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            ) : (
              <div
                className="app-form-fieldset h-full flex items-center justify-center">
                <div className="text-center">
                  <div
                    className="bg-gray-100 dark:bg-gray-600 p-3 mx-auto mb-2 w-12 h-12 flex items-center justify-center rounded-lg">
                    <svg className="w-6 h-6 text-gray-400 dark:text-gray-300" fill="none"
                      stroke="currentColor"
                      viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2"
                        d="M9 7h6m0 10v-3m-3 3h.01M9 17h.01M9 14h.01M12 14h.01M15 11h.01M12 11h.01M9 11h.01M7 21h10a2 2 0 002-2V5a2 2 0 00-2-2H7a2 2 0 00-2 2v14a2 2 0 002 2z"></path>
                    </svg>
                  </div>
                  <h3 className="text-sm font-medium text-gray-900 dark:text-white mb-1">
                    En attente
                  </h3>
                  <p className="text-xs text-gray-500 dark:text-gray-400">
                    Complétez le formulaire
                  </p>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
  );
};

export default SimulerDevis; 