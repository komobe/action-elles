import { API_BASE_URL } from "@/config/api";
import { httpClient } from "./http/http-client";

export interface Assure {
  nom: string;
  prenoms: string;
  sexe: string;
  dateNaissance: string;
  lieuNaissance?: string;
  email: string;
  numeroCarteIdentite: string;
  telephone: string;
  adresse: string;
  ville?: string;
}

export interface Vehicule {
  immatriculation: string;
  dateMiseEnCirculation: string;
  couleur: string;
  nombreDeSieges: number;
  nombreDePortes: number;
  puissanceFiscale: number;
  categorieCode?: string;
  valeurNeuf?: number;
  categorie?: {
    code: string,
    libelle: string
  };
}

export interface SouscriptionData {
  vehicule: Vehicule;
  vehiculeValeurVenale: number;
  produit: string;
  assure: Assure;
}

export interface Souscription {
  id: string;
  numero: string;
  statut: string;
  dateSouscription: Date | string
  assure: Assure;
  vehicule: Vehicule;
}

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

interface Produit {
  id: string;
  code: string | null;
  nom: string;
  description: string;
  garanties: Garantie[];
  categoriesVehicules: Categorie[];
}

export const parseStatutToDisplay = (statut: string): string | undefined => {
  if (!statut) return;

  const statutParsed = statut
    .toLowerCase()
    .replace(/_/g, ' ');

  return statutParsed.charAt(0).toUpperCase() + statutParsed.slice(1);
};

const subscriptionBaseUrl = `${API_BASE_URL}/api/v1/subscriptions`;

export const souscriptionHttpService = {
  lister: async () => {
    return await httpClient.get<Souscription[]>(subscriptionBaseUrl);
  },
  creer: async (data: SouscriptionData) => {
    return await httpClient.post<Souscription>(subscriptionBaseUrl, data);
  },
  delete: async (id: string) => {
    return await httpClient.delete(`${subscriptionBaseUrl}/${id}`);
  },
  gerererAttestation: async (id: string) => {
    return await httpClient.getBlob(`${subscriptionBaseUrl}/${id}/attestation`);
  }
}