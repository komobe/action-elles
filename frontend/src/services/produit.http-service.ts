import { httpClient } from './http/http-client';
import { Produit } from "@/services/devis.http-service";
import { buildUrl } from "@/utils/apiUtils";

export const produitHttpService = {
  lister: async () => {
    return httpClient.get<Produit[]>(buildUrl('api/v1/produits'));
  }
}