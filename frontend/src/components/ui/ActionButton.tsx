import { faSpinner } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import clsx from "clsx";
import { Button } from "primereact/button";
import React from "react";

type Variant = 'primary' | 'danger' | 'secondary';

type ActionButtonProps = {
  isLoading?: boolean;
  validationErrors?: Record<string, object>;
  variant?: Variant;
  children: React.ReactNode;
} & React.ButtonHTMLAttributes<HTMLButtonElement>;

const variantClasses: Record<Variant, string> = {
  primary: 'bg-indigo-600 hover:bg-indigo-700 text-white border border-indigo-600',
  danger: 'bg-red-600 hover:bg-red-700 text-white border border-red-600',
  secondary: 'bg-white text-gray-700 border border-gray-300 hover:bg-gray-50',
};

export default function ActionButton(
  {
    isLoading = false,
    validationErrors = {},
    variant = 'primary',
    children,
    className,
    ...props
  }: ActionButtonProps
) {
  const disabled = isLoading || Object.keys(validationErrors).length > 0 || props.disabled;

  return (
    <Button
      outlined
      rounded
      severity={variant}
      {...props}
      type={props.type ?? 'button'}
      disabled={disabled}
      aria-busy={isLoading}
      className={clsx(
        'w-full lg:w-auto px-6 py-2.5 text-sm font-medium focus:outline-none disabled:opacity-50 transition-all duration-150',
        variantClasses[variant],
        className
      )}
    >
      {isLoading ? (
        <span className="flex items-center justify-center">
          <FontAwesomeIcon icon={faSpinner} spin className="mr-2 h-4 w-4" />
          {children}
        </span>
      ) : (
        children
      )}
    </Button>
  );
}
