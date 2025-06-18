export class HttpError<T = unknown> extends Error {
  status?: number;
  data?: T;
  isAuthError?: boolean;

  constructor(
      message: string,
      options?: {
        status?: number;
        data?: T;
        isAuthError?: boolean;
      }
  ) {
    super(message);
    this.name = 'HttpError';
    this.status = options?.status;
    this.data = options?.data;
    this.isAuthError = options?.isAuthError ?? false;

    Object.setPrototypeOf(this, new.target.prototype);
  }
}