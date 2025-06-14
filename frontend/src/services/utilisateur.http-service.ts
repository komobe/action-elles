import {User} from '@/services/utilisateur.http-service';
import {API_ENDPOINTS} from "@/config/api";
import {httpClient} from "./http/http-client";

export interface User {
  id: string;
  username: string;
  email: string;
  role: string;
  createdAt: string;
  isActive: boolean;
}

interface Role {
  name: string;
  label: string;
}

export const utilisateurHttpService = {
  lister: async (page: number, size: number) => {
    return await httpClient.get<User[]>(`${API_ENDPOINTS.users.list}?page=${page}&size=${size}`);
  },
  modifier: async (utilisateurId: string, utilisateur: Partial<User>) => {
    await httpClient.put<{ status: string }>(API_ENDPOINTS.users.update, {
      id: utilisateurId,
      ...utilisateur
    });
  },
  supprimer: async (utilisateurId: string) => {
    await httpClient.delete(API_ENDPOINTS.users.delete(utilisateurId));
  },
  changerMot2Passe: async (utilisateurId: string, nouveauMot2Passe: string) => {
    await httpClient.put<{ status: string }>(API_ENDPOINTS.users.resetPassword, {
      id: utilisateurId,
      newPassword: nouveauMot2Passe.trim()
    });
  }
}