import { API_ENDPOINTS } from "@/config/api";
import { httpClient } from "./http/http-client";

export interface Assure {
  nom: string;
  prenoms: string;
  sexe: string;
  dateNaissance: string;
  email: string;
  numeroCarteIdentite: string;
  telephone: string;
  adresse: string;
  ville: string;
}

export interface Vehicule {
  immatriculation: string;
  dateMiseEnCirculation: string;
  couleur: string;
  nombreDeSieges: number;
  nombreDePortes: number;
  puissanceFiscale: number;
  categorie: {
    code: string,
    libelle: string
  };
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

  const statutParsed = statut
    .toLowerCase()
    .replace(/_/g, ' ');

  return statutParsed.charAt(0).toUpperCase() + statutParsed.slice(1);
};

export const souscriptionHttpService = {
  lister: async () => {
    return await httpClient.get<Souscription[]>(API_ENDPOINTS.souscription.list);
  },
  delete: async (id: string) => {
    return await httpClient.delete(`${API_ENDPOINTS.souscription.list}/${id}`);
  }
}