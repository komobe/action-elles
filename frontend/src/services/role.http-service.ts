import { API_ENDPOINTS } from "@/config/api";
import { httpClient } from "./http/http-client";

export interface Role {
  label: string;
  value: string;
}

export const roleHttpService = {
  lister: async (): Promise<Role[]> => {
    const response = await httpClient.get<string[]>(API_ENDPOINTS.roles.list);
    return (response.data || []).map((role) => ({
      label: role,
      value: role
    }));
  }
};