import {httpClient} from './http/http-client';
import {API_ENDPOINTS} from "@/config/api.ts";
import {Produit} from "@/services/devis.http-service";

export const produitHttpService = {
  lister: async () => {
    return httpClient.get<Produit[]>(API_ENDPOINTS.produit.list);
  }
}