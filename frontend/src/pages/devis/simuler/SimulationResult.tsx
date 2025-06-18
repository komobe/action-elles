import React from 'react';
import { type SimulationResponse } from '@/services/devis.http-service';
import { formaterDate, formaterMontant, calculerJoursRestants } from '@/utils/dateUtils';
import { Button } from 'primereact/button';
import LoadingSpinner from '@/components/common/LoadingSpinner';

interface SimulationResultProps {
  result: SimulationResponse['data'] | null;
  formData: any;
  onSave: () => void;
  isLoading: boolean;
}

const SimulationResult: React.FC<SimulationResultProps> = ({
  result,
  formData,
  onSave,
  isLoading,
}) => {
  return (
    result ? (
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

            <div className="flex justify-center pt-4">
              <Button
                onClick={onSave}
                disabled={isLoading}
                severity='info'
                icon="pi pi-save"
              >
                {isLoading ? <LoadingSpinner text="Enregistrement..." /> : (
                  <span className='ml-2'>Enregistrer le devis</span>
                )}
              </Button>
            </div>

          </div>
        </div>
      </div>
    ) : (
      <div className="app-form-fieldset h-full flex items-center justify-center">
        <div className="text-center">
          <div className="bg-gray-100 dark:bg-gray-600 p-3 mx-auto mb-2 w-12 h-12 flex items-center justify-center rounded-lg">
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
    )
  );
};

export default SimulationResult; 