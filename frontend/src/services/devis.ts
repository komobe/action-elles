import { http } from './http';
import { API_ENDPOINTS } from '../config/api';

export interface SimulationDevis {
  produit: string;
  categorie: string;
  puissanceFiscale: number;
  dateDeMiseEnCirculation: string;
  valeurNeuf: number;
  valeurVenale: number;
}

export interface SimulationResponse {
  status: string;
  data: {
    quoteReference: string;
    price: number;
    endDate: string;
    metadata: SimulationDevis;
  };
}

export const devisService = {
  simuler: async (data: SimulationDevis): Promise<SimulationResponse> => {
    return http.post(API_ENDPOINTS.devis.simuler, data);
  },
}; 