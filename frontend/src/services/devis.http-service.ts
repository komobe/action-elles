import { httpClient } from './http/http-client';
import { buildUrl } from '@/utils/apiUtils';

export interface VehiculeInfo {
  produit: string;
  categorie: string;
  puissanceFiscale: number;
  vehiculeImmatriculation: string;
  dateDeMiseEnCirculation: string;
  valeurNeuf: number;
  valeurVenale: number;
}

export interface DevisData {
  quoteReference: string;
  price: number;
  endDate: string;
}

export type SimulationDevisRequest = VehiculeInfo

export interface EnregistrerDevisRequest extends DevisData, VehiculeInfo { }


export interface Categorie {
  id: string;
  code: string;
  libelle: string;
  description: string;
}

// Interface pour la réponse de simulation
export interface SimulationResponse {
  status: 'success' | 'error';
  data: DevisData & {
    metadata: VehiculeInfo;
  };
  message?: string;
}

export const devisHttpService = {
  simuler: async (data: SimulationDevisRequest) => {
    return httpClient.post<SimulationResponse['data']>(buildUrl('api/v1/devis', 'simuler'), data);
  },
  enregistrer: async (data: EnregistrerDevisRequest) => {
    return httpClient.post<{ status: string, message: string }>(buildUrl('api/v1/devis', 'enregistrer'), data);
  },
}; 