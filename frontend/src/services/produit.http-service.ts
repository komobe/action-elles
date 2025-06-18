import { httpClient } from './http/http-client';
import { buildUrl } from "@/utils/apiUtils";

interface PuissanceFiscale {
  debut: number;
  fin: number;
  exactMatch: boolean;
}

interface Prime {
  type: 'MONTANT' | 'POURCENTAGE';
  valeur: number;
}

interface Garantie {
  id: string;
  libelle: string;
  description: string;
  code: string;
  puissanceFiscale: PuissanceFiscale | null;
  baseDeCalcul: string;
  prime: Prime;
  primeMinimum: number | null;
  maxAge: number;
  plafonne: boolean;
}

interface Categorie {
  id: string;
  code: string;
  libelle: string;
  description: string;
}

export interface Produit {
  id: string;
  code: string | null;
  nom: string;
  description: string;
  garanties: Garantie[];
  categoriesVehicules: Categorie[];
}

export const produitHttpService = {
  lister: async () => {
    return httpClient.get<Produit[]>(buildUrl('api/v1/produits'));
  }
}