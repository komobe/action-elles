import { buildUrl } from "@/utils/apiUtils";
import { httpClient } from "./http/http-client";

export interface User {
  id: string;
  username: string;
  email: string;
  role: string;
  createdAt: string;
}

export const utilisateurHttpService = {
  lister: async (page: number, size: number) => {
    return await httpClient.get<User[]>(buildUrl('api/v1/utilisateurs') + `?page=${page}&size=${size}`);
  },
  modifier: async (utilisateurId: string, utilisateur: Partial<User>) => {
    await httpClient.put<{ status: string }>(buildUrl('api/v1/utilisateurs'), {
      id: utilisateurId,
      ...utilisateur
    });
  },
  supprimer: async (utilisateurId: string) => {
    await httpClient.delete(buildUrl('api/v1/utilisateurs', utilisateurId));
  },
  changerMot2Passe: async (utilisateurId: string, nouveauMot2Passe: string) => {
    await httpClient.put<{ status: string }>(buildUrl('api/v1/utilisateurs', 'reset-password'), {
      id: utilisateurId,
      newPassword: nouveauMot2Passe.trim()
    });
  }
}