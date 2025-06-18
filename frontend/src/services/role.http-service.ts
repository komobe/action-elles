import { httpClient } from "./http/http-client";
import { buildUrl } from "@/utils/apiUtils";

export interface Role {
  label: string;
  value: string;
}

export const roleHttpService = {
  lister: async (): Promise<Role[]> => {
    const response = await httpClient.get<string[]>(buildUrl('api/v1/roles')) ;
    return (response.data || []).map((role) => ({
      label: role,
      value: role
    }));
  }
};