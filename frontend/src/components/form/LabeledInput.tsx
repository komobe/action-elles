import React from 'react';
import { InputText } from 'primereact/inputtext';

interface LabeledInputProps {
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

const LabeledInput: React.FC<LabeledInputProps> = ({
  id,
  name,
  label,
  value,
  placeholder,
  disabled,
  required,
  className,
  onChange,
  error
}) => {
  return (
    <div className={className}>
      <label htmlFor={id} className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
        {label}
      </label>
      <InputText
        id={id}
        name={name}
        value={value}
        onChange={onChange}
        placeholder={placeholder}
        disabled={disabled}
        className={`w-full ${error ? 'p-invalid' : ''}`}
        required={required}
      />
      {error && <small className="p-error">{error}</small>}
    </div>
  );
};

export default LabeledInput;
