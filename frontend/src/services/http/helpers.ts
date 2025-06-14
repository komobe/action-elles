import {HttpResponse, PaginatedResponse} from "./response.type";

export function isPaginatedResponse<T>(response: HttpResponse<T>): response is PaginatedResponse<T> {
  return 'metadata' in response && response.metadata !== undefined;
}