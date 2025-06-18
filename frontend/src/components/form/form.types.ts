import React from 'react';

export interface BaseFormFieldProps {
  id: string;
  name: string;
  label: string;
  className?: string;
  error?: string;
  required?: boolean;
  placeholder?: string;
  disabled?: boolean;
}

export interface BaseInputProps extends BaseFormFieldProps {
  value?: string | number;
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

export interface BaseCustomInputProps extends BaseFormFieldProps {
  onChange?: (e: { target: { name: string; value: unknown } }) => void;
}

export interface InputFieldProps extends BaseInputProps {
  type?: string;
  max?: string;
  min?: string;
}

export type InputDateFieldProps = Omit<InputFieldProps, 'type'> & {
  max?: string;
  min?: string;
};

export type PasswordFieldProps = Omit<InputFieldProps, 'type'>;

export interface DropdownFieldProps extends BaseCustomInputProps {
  options: { label: string; value: string | number }[];
  value?: string | number;
}

export interface InputNumberFieldProps extends BaseFormFieldProps {
  value?: number | null;
  onChange?: (e: { target: { name: string; value: number | null } }) => void;
  min?: number;
  max?: number;
  step?: number;
  currency?: string;
  mode?: 'decimal' | 'currency';
  minFractionDigits?: number;
  maxFractionDigits?: number;
  suffix?: string;
  prefix?: string;
}

export interface SubmitButtonProps {
  label: string;
  isLoading: boolean;
  isDisabled: boolean;
  isPrimary?: boolean;
  className?: string;
  onClick?: () => void;
}

export type SelectOption = {
  label: string;
  value: string | number;
};

export type CustomChangeEvent = {
  target: {
    name: string;
    value: unknown;
  };
};

export type FormChangeEvent =
  | React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  | CustomChangeEvent;

export interface ValidationProps {
  error?: string;
  touched?: boolean;
  valid?: boolean;
}

export interface LoadingProps {
  loading?: boolean;
  loadingText?: string;
} 