import { buildUrl } from "@/utils/apiUtils";
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

export const parseStatutToDisplay = (statut: string): string | undefined => {
  if (!statut) return;

  const statutParsed = statut.toLowerCase().replace(/_/g, ' ');

  return statutParsed.charAt(0).toUpperCase() + statutParsed.slice(1);
};

export const souscriptionHttpService = {
  lister: async () => {
    return await httpClient.get<Souscription[]>(buildUrl('api/v1/subscriptions'));
  },
  creer: async (data: SouscriptionData) => {
    return await httpClient.post<Souscription>(buildUrl('api/v1/subscriptions'), data);
  },
  delete: async (id: string) => {
    return await httpClient.delete(buildUrl('api/v1/subscriptions', id));
  },
  gerererAttestation: async (id: string) => {
    return await httpClient.download(buildUrl('api/v1/subscriptions', id, 'attestation'));
  }
}