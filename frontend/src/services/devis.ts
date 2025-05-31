import { http } from './http';
import { API_ENDPOINTS } from '../config/api';


export interface VehiculeInfo {
  produit: string;
  categorie: string;
  puissanceFiscale: number;
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


// Interface pour la réponse de simulation
export interface SimulationResponse {
  status: 'success' | 'error';
  data: DevisData & {
    metadata: VehiculeInfo;
  };
  message?: string; // Pour les messages d'erreur ou d'information
}

export const devisService = {
  simuler: async (data: SimulationDevisRequest): Promise<SimulationResponse> => {
    return http.post(API_ENDPOINTS.devis.simuler, data);
  },
  enregistrer: async (data: EnregistrerDevisRequest): Promise<{ status: string, message: string }> => {
    return http.post<{ status: string, message: string }>(API_ENDPOINTS.devis.enregistrer, data);
  }
}; 