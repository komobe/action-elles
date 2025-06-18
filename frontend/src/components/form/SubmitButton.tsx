import React from 'react';
import { Button } from "primereact/button";

interface SubmitButtonProps {
  label: string;
  isLoading: boolean;
  isDisabled: boolean;
  isPrimary?: boolean;
  className?: string;
}

export const SubmitButton: React.FC<SubmitButtonProps> = ({
  label,
  isLoading,
  isDisabled,
  isPrimary = true,
  className
}) => {
  return (
    <Button
      type="submit"
      label={label}
      className={`${isPrimary ? "app-form-button-primary" : "app-form-button"} ${className ?? ''}`}
      disabled={isDisabled}
      loading={isLoading}
    />
  );
};
