import { http, type ApiResponse } from './http';
import { API_ENDPOINTS } from "@/config/api.ts";
import { Produit } from "@services/devis.ts";

export const produitHttpService = {
  lister: async (): Promise<ApiResponse<Produit[]>> => {
    return http.get(API_ENDPOINTS.produit.list);
  }
}