export interface PaginationMetadata {
  number: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
  offset: number;
  remainingElements: number;
  remainingPages: number;
}

export interface PaginationLinks {
  current: string;
  first: string;
  last: string;
  next: string | null;
  prev: string | null;
}

export interface ApiResponse<T> {
  status: 'success' | 'error';
  data?: T;
  message?: string;
}

export interface PaginatedResponse<T> extends ApiResponse<T> {
  metadata?: PaginationMetadata;
  links?: PaginationLinks;
  size?: number;
}

export type HttpResponse<T> = PaginatedResponse<T> | ApiResponse<T>;