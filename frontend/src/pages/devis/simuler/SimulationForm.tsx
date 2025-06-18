import React from 'react';
import { Button } from 'primereact/button';
import { DropdownField, InputField, InputNumberField } from '@/components/form';
import InputDateField from '@/components/form/InputDateField';
import { LoadingSpinner } from '@/components/common';
import { type Produit, type Categorie, type SimulationDevisRequest } from '@/services/devis.http-service';

interface SimulationFormProps {
  currentStep: number;
  formData: SimulationDevisRequest;
  selectedProduitId: string;
  categoriesDisponibles: Categorie[];
  produits: Produit[];
  validationErrors: Partial<Record<keyof SimulationDevisRequest, string>>;
  isLoading: boolean;
  isLoadingData: boolean;
  isDesktop: boolean;
  steps: Array<{ number: number; title: string; fields: string[] }>;
  onHandleChange: (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement> | { target: { name: string; value: any } }) => void;
  onHandleNext: () => void;
  onHandleBack: () => void;
  onHandleSubmit: () => void;
  onHandleSave: () => void;
  result: any;
}

const SimulationForm: React.FC<SimulationFormProps> = ({
  currentStep,
  formData,
  selectedProduitId,
  categoriesDisponibles,
  produits,
  validationErrors,
  isLoading,
  isLoadingData,
  isDesktop,
  steps,
  onHandleChange,
  onHandleNext,
  onHandleBack,
  onHandleSubmit,
  onHandleSave,
  result
}) => {
  return (
    <form onSubmit={(e) => e.preventDefault()}>
      <div className="app-form-fieldset">
        <div className="space-y-6">
          <div className={`${!isDesktop && currentStep !== 1 ? 'hidden' : 'block'}`}>
            <fieldset className="app-form-fieldset">
              <legend className="app-form-legend">
                Informations du produit
              </legend>
              <div className="grid lg:grid-cols-2 gap-4">
                <DropdownField
                  id="produit"
                  name="produit"
                  label="Produit"
                  options={produits.map(prod => ({
                    label: prod.nom,
                    value: prod.id
                  }))}
                  value={selectedProduitId}
                  onChange={onHandleChange}
                  required
                  error={validationErrors.produit}
                  placeholder="Sélectionnez un produit"
                />

                <DropdownField
                  id="categorie"
                  name="categorie"
                  label="Catégorie"
                  options={categoriesDisponibles.map(cat => ({
                    label: `${cat.code} - ${cat.libelle}`,
                    value: cat.code
                  }))}
                  value={formData.categorie}
                  onChange={onHandleChange}
                  required
                  placeholder="Sélectionnez une catégorie"
                  disabled={categoriesDisponibles.length === 0}
                  error={validationErrors.categorie}
                />
              </div>
            </fieldset>
          </div>

          <div className={`${!isDesktop && currentStep !== 2 ? 'hidden' : 'block'}`}>
            <fieldset className="app-form-fieldset">
              <legend className="app-form-legend">
                Informations du véhicule
              </legend>
              <div className="grid lg:grid-cols-2 gap-4">
                <InputField
                  id="vehiculeImmatriculation"
                  name="vehiculeImmatriculation"
                  label="Immatriculation"
                  value={formData.vehiculeImmatriculation}
                  onChange={onHandleChange}
                  error={validationErrors.vehiculeImmatriculation}
                  required
                />

                <InputNumberField
                  id="puissanceFiscale"
                  name="puissanceFiscale"
                  label="Puissance fiscale"
                  value={formData.puissanceFiscale}
                  onChange={onHandleChange}
                  error={validationErrors.puissanceFiscale}
                  placeholder="Entrez la puissance fiscale"
                  required
                  min={0}
                  max={100}
                  suffix=" CV"
                />

                <InputDateField
                  id="dateDeMiseEnCirculation"
                  name="dateDeMiseEnCirculation"
                  label="Date de mise en circulation"
                  value={formData.dateDeMiseEnCirculation}
                  onChange={onHandleChange}
                  required
                  error={validationErrors.dateDeMiseEnCirculation}
                />

                <InputNumberField
                  id="valeurNeuf"
                  name="valeurNeuf"
                  label="Valeur à neuf"
                  value={formData.valeurNeuf}
                  onChange={onHandleChange}
                  mode="currency"
                  currency="XOF"
                  required
                  error={validationErrors.valeurNeuf}
                />

                <InputNumberField
                  id="valeurVenale"
                  name="valeurVenale"
                  label="Valeur vénale"
                  value={formData.valeurVenale}
                  onChange={onHandleChange}
                  mode="currency"
                  currency="XOF"
                  required
                  error={validationErrors.valeurVenale}
                />
              </div>
            </fieldset>
          </div>

          <div className="flex gap-3 sm:gap-4 justify-end">
            {!isDesktop && currentStep > 1 && (
              <Button
                label='Précédent'
                iconPos="left"
                icon="pi pi-chevron-left"
                onClick={onHandleBack}
                severity='secondary'
              />
            )}
            {!isDesktop && currentStep < steps.length ? (
              <Button
                label="Suivant"
                iconPos="right"
                icon="pi pi-chevron-right"
                onClick={onHandleNext}
                disabled={isLoading || isLoadingData}
                className="ml-auto"
              />
            ) : (
              <div className="flex flex-col sm:flex-row gap-4 ml-auto">
                <Button
                  onClick={onHandleSubmit}
                  disabled={isLoading || Object.keys(validationErrors).length > 0}
                  severity='info'
                >
                  {isLoading ? (<LoadingSpinner text="Simulation en cours..." />) : (
                    'Simuler le devis'
                  )}
                </Button>
              </div>
            )}
          </div>
        </div>
      </div>
    </form>
  );
};

export default SimulationForm; 