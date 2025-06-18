import React, { useCallback, useEffect, useState } from 'react';
import { useMediaQuery } from '@hooks/useMediaQuery';
import {
  type Categorie,
  devisHttpService,
  SimulationDevisRequest,
  type SimulationResponse
} from '@/services/devis.http-service';
import { Produit, produitHttpService } from "@services/produit.http-service";
import SimulationForm from '@/pages/devis/simuler/SimulationForm';
import SimulationResult from '@/pages/devis/simuler/SimulationResult';
import { HttpError } from "@services/http/ http-error.ts";

const initialFormData: SimulationDevisRequest = {
  produit: '',
  categorie: '',
  puissanceFiscale: 0,
  vehiculeImmatriculation: '',
  dateDeMiseEnCirculation: '',
  valeurNeuf: 0,
  valeurVenale: 0
} as SimulationDevisRequest;

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
  }
];

const SimulerDevis = () => {
  const isDesktop = useMediaQuery('(min-width: 1024px)');
  const [currentStep, setCurrentStep] = useState<number>(1);
  const [selectedProduitId, setSelectedProduitId] = useState<string>('');
  const [formData, setFormData] = useState<SimulationDevisRequest>(initialFormData);
  const [produits, setProduits] = useState<Produit[]>([]);
  const [categoriesDisponibles, setCategoriesDisponibles] = useState<Categorie[]>([]);
  const [isLoadingData, setIsLoadingData] = useState(true);
  const [result, setResult] = useState<SimulationResponse['data'] | null>(null);
  const [error, setError] = useState<string>('');
  const [isLoading, setIsLoading] = useState(false);
  const [validationErrors, setValidationErrors] = useState<Partial<Record<keyof SimulationDevisRequest, string>>>({});

  useEffect(() => {
    const fetchData = async () => {
      setIsLoadingData(true);
      try {
        const produitsResponse = await produitHttpService.lister();

        if (produitsResponse.status === 'success' && produitsResponse.data) {
          setProduits(produitsResponse.data);
        }
      } catch (error) {
        if (error instanceof HttpError) {
          setError(error.message);
        } else {
          setError('Erreur lors du chargement des données');
        }
      } finally {
        setIsLoadingData(false);
      }
    };

    fetchData().then()
  }, []);

  const validateStep = useCallback((step: number): boolean => {
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
  }, [formData]);

  const validateAllSteps = useCallback((): boolean => {
    return steps.every((_, index) => validateStep(index + 1));
  }, [validateStep]);

  const handleChange = useCallback((e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement> | {
    target: { name: string; value: unknown }
  }) => {
    const { name, value } = e.target;

    if (name === 'produit') {
      const produitSelectionne = produits.find(p => p.id === value);
      if (produitSelectionne) {
        setSelectedProduitId(value as string);
        setCategoriesDisponibles(produitSelectionne.categoriesVehicules || []);
        setFormData(prev => ({
          ...prev,
          produit: produitSelectionne.nom,
          categorie: ''
        }));
      } else {
        setSelectedProduitId('');
        setCategoriesDisponibles([]);
        setFormData(prev => ({
          ...prev,
          produit: '',
          categorie: ''
        }));
      }
    } else {
      setFormData(prev => ({
        ...prev,
        [name]: name === 'puissanceFiscale' || name === 'valeurNeuf' || name === 'valeurVenale'
          ? Number(value) || 0
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
  }, [produits, validationErrors]);

  const handleSubmit = useCallback(async () => {
    if (!validateAllSteps()) {
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
        setError('');
      } else {
        setError('Une erreur est survenue lors de la simulation');
      }
    } catch (error: unknown) {
      if (error instanceof HttpError) {
        setError(error.message);
      } else {
        setError('Une erreur est survenue lors de la simulation');
      }
    } finally {
      setIsLoading(false);
    }
  }, [validateAllSteps, isLoading, formData]);

  const handleNext = useCallback(() => {
    if (validateStep(currentStep)) {
      if (currentStep === 2) {
        handleSubmit().then();
      } else if (currentStep < steps.length) {
        setCurrentStep(currentStep + 1);
      }
    }
  }, [currentStep, validateStep, handleSubmit]);

  const handleBack = useCallback(() => {
    if (currentStep > 1) {
      setError('');
      setValidationErrors({});
      setResult(null);
      setCurrentStep(currentStep - 1);
    }
  }, [currentStep]);

  const handleReset = useCallback(() => {
    setFormData(initialFormData);
    setSelectedProduitId('');
    setResult(null);
    setError('');
    setValidationErrors({});
    setCurrentStep(1);
  }, []);

  const handleSave = useCallback(async () => {
    if (!result) {
      setError('Aucune simulation disponible');
      return;
    }

    const { quoteReference, price, endDate } = result;
    setIsLoading(true);
    setError('');

    try {
      const enregistrerDevisRequest = { quoteReference, price, endDate, ...formData };
      const response = await devisHttpService.enregistrer(enregistrerDevisRequest);
      if (response.status === 'error') {
        setError(response.message ?? 'Erreur lors de l\'enregistrement');
      } else {
        handleReset();
      }
    } catch (error) {
      setError(error instanceof Error ? error.message : 'Erreur inattendue');
    } finally {
      setIsLoading(false);
    }
  }, [result, formData, handleReset]);

  useEffect(() => {
    setError('');
  }, [currentStep]);

  return (
    <div className="flex flex-col gap-3">
      <div className="flex justify-between items-center app-form-fieldset py-2">
        <h1 className="text-2xl font-bold text-gray-900 dark:text-white mb-0">
          Simuler un devis
        </h1>
      </div>

      <div className="grid lg:grid-cols-12 gap-6">
        <div className="lg:col-span-8 space-y-6">
          {error && (
            <div className="error-message" role="alert">
              <p>{error}</p>
            </div>
          )}

          <SimulationForm
            currentStep={currentStep}
            formData={formData}
            selectedProduitId={selectedProduitId}
            categoriesDisponibles={categoriesDisponibles}
            produits={produits}
            validationErrors={validationErrors}
            isLoading={isLoading}
            isLoadingData={isLoadingData}
            isDesktop={isDesktop}
            steps={steps}
            onHandleChange={handleChange}
            onHandleNext={handleNext}
            onHandleBack={handleBack}
            onHandleSubmit={handleSubmit}
          />
        </div>

        <div className={`lg:col-span-4 ${!result ? 'hidden lg:block' : ''}`}>
          <SimulationResult
            result={result}
            formData={formData}
            onSave={handleSave}
            isLoading={isLoading}
          />
        </div>
      </div>
    </div>
  );
};

export default SimulerDevis;