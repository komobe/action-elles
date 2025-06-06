import { faSpinner } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import clsx from "clsx";
import { Button } from "primereact/button";
import React from "react";

type Variant = 'success' | 'info' | 'secondary' | 'contrast' | 'warning' | 'danger' | 'help';

type ActionButtonProps = {
  isLoading?: boolean;
  validationErrors?: Record<string, object>;
  variant?: Variant;
  children: React.ReactNode;
} & React.ButtonHTMLAttributes<HTMLButtonElement>;

const variantClasses: Record<Variant, string> = {
  success: 'bg-green-600 hover:bg-green-700 text-white border border-green-600',
  info: 'bg-blue-600 hover:bg-blue-700 text-white border border-blue-600',
  secondary: 'bg-white text-gray-700 border border-gray-300 hover:bg-gray-50',
  contrast: 'bg-gray-800 hover:bg-gray-900 text-white border border-gray-800',
  warning: 'bg-yellow-600 hover:bg-yellow-700 text-white border border-yellow-600',
  danger: 'bg-red-600 hover:bg-red-700 text-white border border-red-600',
  help: 'bg-purple-600 hover:bg-purple-700 text-white border border-purple-600',
};

export default function ActionButton(
  {
    isLoading = false,
    validationErrors = {},
    variant = 'success',
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
