import { Button } from 'primereact/button';
import React, { useEffect, useState } from 'react';
import { useMediaQuery } from '@hooks/useMediaQuery.ts';
import {
  type Categorie,
  devisService,
  type Produit,
  type SimulationDevisRequest,
  type SimulationResponse
} from '@services/devis.ts';
import { produitHttpService } from "@services/produit.http-service.ts";

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
      const response = await devisService.simuler(formData);
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
      const response = await devisService.enregistrer(enregistrerDevisRequest);
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

  const formatMontant = (montant: number) => {
    return new Intl.NumberFormat('fr-FR', {
      style: 'currency',
      currency: 'XOF',
      maximumFractionDigits: 0
    }).format(montant);
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('fr-FR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  };

  const calculateDaysRemaining = (endDate: string): number => {
    const end = new Date(endDate);
    const today = new Date();
    const diffTime = end.getTime() - today.getTime();
    return Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  };

  const renderField = (
    name: keyof SimulationDevisRequest,
    label: string,
    type: 'text' | 'number' | 'date' | 'select',
    options?: { value: string; label: string }[]
  ) => {
    const error = validationErrors[name];
    const inputClassName = "appearance-none block w-full px-3 py-2 text-sm border border-gray-300 dark:border-gray-600 placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 bg-white dark:bg-gray-700 text-gray-900 dark:text-gray-100";

    return (
      <div className="mb-4 last:mb-0">
        <label htmlFor={name}
          className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
          {label}
        </label>
        <div>
          {type === 'select' ? (
            <select
              id={name}
              name={name}
              value={name === 'produit' ? selectedProduitId : formData[name]?.toString() || ''}
              onChange={handleChange}
              className={inputClassName}
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
              className={inputClassName}
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
    <div className="bg-white dark:bg-gray-800 w-full max-w-7xl mx-auto min-h-[calc(100vh-7rem)]">
      <div className="p-6">
        <h2 className="text-center text-3xl font-extrabold text-gray-900 dark:text-white mb-2">
          Simulation de Devis
        </h2>
        <p className="text-center text-base text-gray-600 dark:text-gray-400 mb-8">
          Remplissez le formulaire pour obtenir une estimation de votre prime d'assurance
        </p>

        {/* Stepper Mobile */}
        <div className="lg:hidden mb-8">
          <div className="flex items-center justify-between">
            {steps.map((step, index) => (
              <div key={step.number} className="flex flex-col items-center flex-1">
                <div
                  className={`flex items-center justify-center w-8 h-8 border-2 rounded-full mb-2 ${currentStep >= step.number
                    ? 'border-indigo-600 bg-indigo-600 text-white'
                    : 'border-gray-300 text-gray-500'
                    }`}>
                  {step.number}
                </div>
                <div className={`text-xs text-center ${currentStep >= step.number
                  ? 'text-indigo-600 font-medium'
                  : 'text-gray-500'
                  }`}>
                  {step.title}
                </div>
                {index < steps.length - 1 && (
                  <div
                    className={`absolute w-1/4 h-0.5 left-${index === 0 ? '1/4' : '1/2'} ${currentStep > step.number
                      ? 'bg-indigo-600'
                      : 'bg-gray-300'
                      }`} />
                )}
              </div>
            ))}
          </div>
        </div>

        <div className="grid lg:grid-cols-12 gap-6">
          {/* Formulaire */}
          <div className="lg:col-span-8 space-y-6">
            <form onSubmit={(e) => e.preventDefault()}>
              {error && (
                <div className="bg-red-50 dark:bg-red-900/50 border-l-4 border-red-400 p-4 mb-6"
                  role="alert">
                  <p className="text-sm text-red-700 dark:text-red-200">{error}</p>
                </div>
              )}

              <div className="bg-gray-50 dark:bg-gray-700/50 p-6 rounded-lg">
                <div className="space-y-6">
                  {/* Informations du produit - visible en desktop ou à l'étape 1 en mobile */}
                  <div className={`${!isDesktop && currentStep !== 1 ? 'hidden' : 'block'}`}>
                    <fieldset className="border border-gray-300 dark:border-gray-600 rounded-lg p-4">
                      <legend className="text-lg font-medium text-gray-900 dark:text-white px-2">
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
                    <fieldset className="border border-gray-300 dark:border-gray-600 rounded-lg p-4">
                      <legend className="text-lg font-medium text-gray-900 dark:text-white px-2">
                        Informations du véhicule
                      </legend>
                      <div className="grid lg:grid-cols-2 gap-4">
                        {renderField('vehiculeImmatriculation', 'Immatriculation', 'text')}
                        {renderField('dateDeMiseEnCirculation', 'Date de mise en circulation', 'date')}
                        {renderField('puissanceFiscale', 'Puissance Fiscale', 'number')}
                        {renderField('valeurNeuf', 'Valeur à neuf', 'number')}
                        {renderField('valeurVenale', 'Valeur vénale', 'number')}
                      </div>
                    </fieldset>
                  </div>
                </div>

                {/* Résultat - visible à l'étape 3 sur mobile */}
                {currentStep === 3 && (
                  <div className="block lg:hidden mt-6">
                    {result ? (
                      <div className="space-y-4">
                        {/* Prix */}
                        <div
                          className="bg-white dark:bg-gray-800 p-4 rounded-lg border border-indigo-100 dark:border-indigo-900">
                          <div className="flex items-center justify-between">
                            <p className="text-sm text-gray-500 dark:text-gray-400">Prime
                              d'assurance</p>
                            <p className="text-xl font-bold text-indigo-600 dark:text-indigo-400">
                              {formatMontant(result.price)}
                            </p>
                          </div>
                          <div className="mt-2 flex items-center justify-between text-xs">
                            <span
                              className="text-gray-500 dark:text-gray-400">Référence</span>
                            <span
                              className="font-medium text-gray-900 dark:text-white">{result.quoteReference}</span>
                          </div>
                          <div className="mt-1 flex items-center justify-between text-xs">
                            <span className="text-gray-500 dark:text-gray-400">Valable jusqu'au</span>
                            <span
                              className="font-medium text-gray-900 dark:text-white">{formatDate(result.endDate)}</span>
                          </div>
                        </div>

                        {/* Récapitulatif */}
                        <div className="bg-white dark:bg-gray-800 p-4 rounded-lg">
                          <h3 className="text-sm font-medium text-gray-900 dark:text-white mb-3">
                            Récapitulatif
                          </h3>

                          {/* Informations du produit */}
                          <div className="mb-4">
                            <h4 className="text-xs font-medium text-gray-700 dark:text-gray-300 mb-2">
                              Informations du produit
                            </h4>
                            <div className="space-y-2 text-sm">
                              <div className="flex justify-between">
                                <span className="text-gray-500">Produit</span>
                                <span className="font-medium">{formData.produit}</span>
                              </div>
                              <div className="flex justify-between">
                                <span className="text-gray-500">Catégorie</span>
                                <span className="font-medium">{formData.categorie}</span>
                              </div>
                            </div>
                          </div>

                          {/* Informations du véhicule */}
                          <div>
                            <h4 className="text-xs font-medium text-gray-700 dark:text-gray-300 mb-2">
                              Informations du véhicule
                            </h4>
                            <div className="space-y-2 text-sm">
                              <div className="flex justify-between">
                                <span className="text-gray-500">Puissance</span>
                                <span className="font-medium">{formData.puissanceFiscale} CV</span>
                              </div>
                              <div className="flex justify-between">
                                <span className="text-gray-500">Immatriculation</span>
                                <span className="font-medium">{formData.vehiculeImmatriculation}</span>
                              </div>
                              <div className="flex justify-between">
                                <span className="text-gray-500">Valeur à neuf</span>
                                <span className="font-medium">{formatMontant(formData.valeurNeuf)}</span>
                              </div>
                              <div className="flex justify-between">
                                <span className="text-gray-500">Valeur vénale</span>
                                <span className="font-medium">{formatMontant(formData.valeurVenale)}</span>
                              </div>
                            </div>
                          </div>
                        </div>

                        <div className="bg-indigo-50 dark:bg-indigo-900/20 p-3 rounded-lg">
                          <p className="text-xs text-indigo-700 dark:text-indigo-300">
                            Cette simulation est valable
                            pendant {calculateDaysRemaining(result.endDate)} jours. Pour
                            souscrire, contactez votre agent.
                          </p>
                        </div>
                      </div>
                    ) : (
                      <div className="text-center py-8">
                        <p className="text-sm text-gray-500">Aucun résultat disponible</p>
                      </div>
                    )}
                  </div>
                )}


                {/* Boutons de navigation mobile */}
                <div className="flex justify-between mt-6 lg:hidden">
                  <button
                    type="button"
                    onClick={handleBack}
                    className={`px-4 py-2 text-sm border border-gray-300 text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 ${currentStep === 1 ? 'invisible' : ''
                      }`}
                  >
                    Précédent
                  </button>
                  <button
                    type="button"
                    onClick={currentStep === 2 ? handleSubmit : handleNext}
                    disabled={isLoading || Object.keys(validationErrors).length > 0}
                    className="px-4 py-2 text-sm border border-transparent text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed"
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
                    ) : currentStep === 2 ? (
                      'Simuler le devis'
                    ) : currentStep === 3 ? (
                      'Nouvelle simulation'
                    ) : (
                      'Suivant'
                    )}
                  </button>
                </div>

                {/* Bouton Simuler en desktop */}
                <div className="mt-6 flex lg:justify-end justify-center">
                  <Button
                    className="w-full lg:w-auto"
                    onClick={result ? handleSave : handleSubmit}
                    disabled={isLoading || (!result && Object.keys(validationErrors).length > 0)}
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
                    ) : result ? ('Enregistrer le devis') : ('Simuler le devis')
                    }
                  </Button>
                </div>


              </div>
            </form>
          </div>

          {/* Résultat */}
          <div
            className={`lg:col-span-4 ${!result && currentStep !== 3 ? 'hidden lg:block' : ''}`}>
            {result ? (
              <div className="bg-gray-50 dark:bg-gray-700/50 p-4 rounded-lg h-full">
                <div>
                  <div className="flex items-center justify-between mb-4">
                    <h2 className="text-lg font-semibold text-gray-900 dark:text-white">
                      Résultat
                    </h2>
                    <span
                      className="text-xs px-2 py-1 bg-green-100 text-green-800 rounded-full">
                      Ref: {result.quoteReference}
                    </span>
                  </div>

                  <div className="space-y-3">
                    {/* Prix */}
                    <div
                      className="bg-white dark:bg-gray-800 p-3 rounded-lg border border-indigo-100 dark:border-indigo-900">
                      <div className="flex items-center justify-between">
                        <p className="text-sm text-gray-500 dark:text-gray-400">Prime
                          d'assurance</p>
                        <p className="text-xl font-bold text-indigo-600 dark:text-indigo-400">
                          {formatMontant(result.price)}
                        </p>
                      </div>
                      <div className="mt-1 flex items-center justify-between text-xs">
                        <span
                          className="text-gray-500 dark:text-gray-400">Valable jusqu'au</span>
                        <span
                          className="font-medium text-gray-900 dark:text-white">{formatDate(result.endDate)}</span>
                      </div>
                    </div>

                    {/* Récapitulatif du véhicule */}
                    <div className="bg-white dark:bg-gray-800 p-3 rounded-lg">
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
                            <span className="font-medium text-gray-900 dark:text-white">{formatDate(formData.dateDeMiseEnCirculation)}</span>
                          </div>
                          <div className="flex items-center justify-between text-xs">
                            <span className="text-gray-500 dark:text-gray-400">Valeur à neuf</span>
                            <span className="font-medium text-gray-900 dark:text-white">{formatMontant(formData.valeurNeuf)}</span>
                          </div>
                          <div className="flex items-center justify-between text-xs">
                            <span className="text-gray-500 dark:text-gray-400">Valeur vénale</span>
                            <span className="font-medium text-gray-900 dark:text-white">{formatMontant(formData.valeurVenale)}</span>
                          </div>
                        </div>
                      </div>
                    </div>

                    <div className="bg-indigo-50 dark:bg-indigo-900/20 p-2 rounded-lg">
                      <div className="flex items-start">
                        <svg className="w-3 h-3 text-indigo-500 mt-0.5 mr-1 flex-shrink-0"
                          fill="none"
                          stroke="currentColor" viewBox="0 0 24 24">
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2"
                            d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                        </svg>
                        <p className="text-xs text-indigo-700 dark:text-indigo-300">
                          Cette simulation est valable
                          pendant {calculateDaysRemaining(result.endDate)} jours
                          (jusqu'au {formatDate(result.endDate)}). Pour souscrire à cette offre,
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
                className="bg-gray-50 dark:bg-gray-700/50 p-4 h-full flex items-center justify-center rounded-lg">
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
    </div>
  );
};

export default SimulerDevis; 