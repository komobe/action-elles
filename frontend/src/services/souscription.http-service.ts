import {API_ENDPOINTS} from "@/config/api";
import {httpClient} from "./http/http-client";

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
  assure: Assure;
  vehicule: Vehicule;
}

export const souscriptionHttpService = {
  lister: async () => {
    return await httpClient.get<Souscription[]>(API_ENDPOINTS.souscription.list);
  }
}