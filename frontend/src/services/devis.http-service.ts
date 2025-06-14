import {httpClient} from './http/http-client';
import {API_ENDPOINTS} from '../config/api';


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

export interface SimulationDevisRequest extends VehiculeInfo { }

export interface EnregistrerDevisRequest extends DevisData, VehiculeInfo { }

export interface Produit {
  id: string;
  code: string | null;
  nom: string;
  description: string;
  garanties: Array<any>;
  categoriesVehicules: Array<Categorie>;
}

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
    return httpClient.post<SimulationResponse['data']>(API_ENDPOINTS.devis.simuler, data);
  },
  enregistrer: async (data: EnregistrerDevisRequest) => {
    return httpClient.post<{ status: string, message: string }>(API_ENDPOINTS.devis.enregistrer, data);
  },
}; 