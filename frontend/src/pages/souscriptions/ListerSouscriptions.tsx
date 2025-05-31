import React, { useState, useEffect } from 'react';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { http } from '@services/http';
import { API_ENDPOINTS } from '@/config/api';
import { useToast } from '@contexts/ToastContext';
import 'primeicons/primeicons.css';
import { Button } from 'primereact/button';
import { useNavigate } from 'react-router-dom';
import ActionButton from '@/components/ui/ActionButton';

interface Assure {
  nom: string;
  prenom: string;
  sexe: string;
  dateNaissance: string;
  email: string;
  numeroCarteIdentite: string;
  telephone: string;
  adresse: string;
  ville: string;
}

interface Vehicule {
  numeroImmatriculation: string;
  dateMiseEnCirculation: string;
  couleur: string;
  nombreDeSieges: number;
  nombreDePortes: number;
  categorieCode: string;
  puissanceFiscale: number;
  carburant: string;
  numeroChassis: string;
}

interface Souscription {
  id: string;
  assure: Assure;
  vehicule: Vehicule;
}

interface ApiResponse {
  data: Souscription[];
}

export default function ListerSouscriptions() {
  const navigate = useNavigate();
  const [souscriptions, setSouscriptions] = useState<Souscription[]>([]);
  const [expandedRows, setExpandedRows] = useState<any>(null);
  const { error: showError } = useToast();

  useEffect(() => {
    fetchSouscriptions();
  }, []);

  const fetchSouscriptions = async () => {
    try {
      const response = await http.get<ApiResponse>(API_ENDPOINTS.souscription.list);
      if (response?.data) {
        const souscriptionsWithId = (Array.isArray(response.data) ? response.data : [])
          .map((s, index) => ({
            ...s,
            id: s.assure.numeroCarteIdentite || `souscription-${index}`
          }));
        setSouscriptions(souscriptionsWithId);
      }
    } catch (error) {
      showError('Erreur lors du chargement des souscriptions');
    }
  };

  const formatDate = (date: string) => {
    return new Date(date).toLocaleDateString('fr-FR');
  };

  const rowExpansionTemplate = (data: Souscription) => {
    return (
      <div className="p-3">
        <div className="grid grid-cols-2 gap-8">
          <div>
            <h3 className="text-lg font-semibold mb-4">Informations de l'assuré</h3>
            <div className="space-y-3">
              <div>
                <label className="text-sm text-gray-500">Nom complet</label>
                <p className="font-medium">{data.assure.nom} {data.assure.prenom}</p>
              </div>
              <div>
                <label className="text-sm text-gray-500">Date de naissance</label>
                <p className="font-medium">{formatDate(data.assure.dateNaissance)}</p>
              </div>
              <div>
                <label className="text-sm text-gray-500">Sexe</label>
                <p className="font-medium">{data.assure.sexe}</p>
              </div>
              <div>
                <label className="text-sm text-gray-500">N° Carte d'identité</label>
                <p className="font-medium">{data.assure.numeroCarteIdentite}</p>
              </div>
              <div>
                <label className="text-sm text-gray-500">Contact</label>
                <p className="font-medium">{data.assure.telephone}</p>
                <p className="text-sm text-gray-500">{data.assure.email}</p>
              </div>
              <div>
                <label className="text-sm text-gray-500">Adresse</label>
                <p className="font-medium">{data.assure.adresse}</p>
                <p className="text-sm">{data.assure.ville}</p>
              </div>
            </div>
          </div>

          <div>
            <h3 className="text-lg font-semibold mb-4">Informations du véhicule</h3>
            <div className="space-y-3">
              <div>
                <label className="text-sm text-gray-500">Immatriculation</label>
                <p className="font-medium">{data.vehicule.numeroImmatriculation}</p>
              </div>
              <div>
                <label className="text-sm text-gray-500">N° Chassis</label>
                <p className="font-medium">{data.vehicule.numeroChassis}</p>
              </div>
              <div>
                <label className="text-sm text-gray-500">Mise en circulation</label>
                <p className="font-medium">{formatDate(data.vehicule.dateMiseEnCirculation)}</p>
              </div>
              <div>
                <label className="text-sm text-gray-500">Caractéristiques</label>
                <p className="font-medium">{data.vehicule.couleur} - {data.vehicule.carburant}</p>
                <p className="text-sm text-gray-500">
                  {data.vehicule.nombreDePortes} portes - {data.vehicule.nombreDeSieges} places
                </p>
              </div>
              <div>
                <label className="text-sm text-gray-500">Puissance fiscale</label>
                <p className="font-medium">{data.vehicule.puissanceFiscale} CV</p>
              </div>
              <div>
                <label className="text-sm text-gray-500">Catégorie</label>
                <p className="font-medium">Code {data.vehicule.categorieCode}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  };

  const expandedRowIcon = () => {
    return <i className="pi pi-chevron-down text-gray-900" />;
  };

  const collapsedRowIcon = () => {
    return <i className="pi pi-chevron-right text-gray-900" />;
  };

  return (
    <div className="flex flex-col gap-4">
      <div className="px-6 pt-10 pb-2 flex justify-between items-center">
        <h1 className="text-2xl font-bold text-gray-900 dark:text-white">
          Souscriptions
        </h1>
        <div className="flex justify-end gap-2">
          <Button
            label="Nouvelle"
            icon="pi pi-plus"
            onClick={() => navigate('/souscription/creer')}
            severity="success"
            raised
          />
        </div>
      </div>
      <div className="bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-700">
        <DataTable
          value={souscriptions}
          expandedRows={expandedRows}
          onRowToggle={(e) => setExpandedRows(e.data)}
          rowExpansionTemplate={rowExpansionTemplate}
          dataKey="id"
          tableStyle={{ minWidth: '100%' }}
          collapsedRowIcon={collapsedRowIcon}
          expandedRowIcon={expandedRowIcon}
          className="[&_.p-datatable-wrapper]:border-none [&_.p-datatable-header]:border-none [&_.p-datatable-header]:bg-transparent [&_.p-paginator]:border-none [&_.p-datatable-thead]:bg-gray-50 dark:[&_.p-datatable-thead]:bg-gray-700/50 [&_.p-datatable-tbody]:bg-transparent [&_.p-datatable-row]:border-b [&_.p-datatable-row]:border-gray-200 dark:[&_.p-datatable-row]:border-gray-700"
        >
          <Column expander style={{ width: '1.5rem' }} />
          <Column field="id" header="N° Souscription" headerClassName="!text-gray-500 dark:!text-gray-300 !font-medium !uppercase !text-sm" />
          <Column
            header="Nom et Prénom"
            body={(rowData) => `${rowData.assure.nom} ${rowData.assure.prenom}`}
            headerClassName="!text-gray-500 dark:!text-gray-300 !font-medium !uppercase !text-sm"
          />
          <Column
            field="assure.numeroCarteIdentite"
            header="N° Carte d'identité"
            headerClassName="!text-gray-500 dark:!text-gray-300 !font-medium !uppercase !text-sm"
          />
          <Column
            field="vehicule.numeroImmatriculation"
            header="Immatriculation"
            headerClassName="!text-gray-500 dark:!text-gray-300 !font-medium !uppercase !text-sm"
          />
          <Column
            field="vehicule.puissanceFiscale"
            header="Puissance fiscale"
            headerClassName="!text-gray-500 dark:!text-gray-300 !font-medium !uppercase !text-sm"
            body={(rowData) => {
              const puissanceFiscale = rowData.vehicule.puissanceFiscale;
              return (
                puissanceFiscale ? `${puissanceFiscale} CV` : 'N/A'
              )
            }}
          />
          <Column
            header="Actions"
            body={() => (
              <div className="flex gap-2 justify-center">
                <Button
                  icon="pi pi-eye"
                  severity="secondary"
                  text
                  rounded
                  tooltip="Voir"
                  tooltipOptions={{ position: 'top' }}
                />
                <Button
                  icon="pi pi-pencil"
                  severity="secondary"
                  text
                  rounded
                  tooltip="Modifier"
                  tooltipOptions={{ position: 'top' }}
                />
                <Button
                  icon="pi pi-trash"
                  severity="danger"
                  text
                  rounded
                  tooltip="Supprimer"
                  tooltipOptions={{ position: 'top' }}
                />
              </div>
            )}
            style={{ width: '10rem' }}
            headerClassName="!text-gray-500 dark:!text-gray-300 !font-medium !uppercase !text-sm"
          />
        </DataTable>
      </div>
    </div>
  );
} 