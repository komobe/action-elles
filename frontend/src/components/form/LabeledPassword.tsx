import React from 'react';
import { Password } from 'primereact/password';

interface LabeledPasswordProps {
  id: string;
  name: string;
  label: string;
  value: string;
  placeholder?: string;
  disabled?: boolean;
  required?: boolean;
  className?: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  error?: string;
}

const LabeledPassword: React.FC<LabeledPasswordProps> = ({
  id,
  name,
  label,
  value,
  placeholder,
  disabled = false,
  required = false,
  className = '',
  onChange,
  error
}) => {
  return (
    <div className={className}>
      <label htmlFor={id} className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
        {label}
      </label>
      <Password
        id={id}
        name={name}
        value={value}
        onChange={onChange}
        placeholder={placeholder}
        disabled={disabled}
        required={required}
        feedback={false}
        toggleMask
        className={`w-full ${error ? 'p-invalid' : ''}`}
      />
      {error && <small className="p-error">{error}</small>}
    </div>
  );
};

export default LabeledPassword;
