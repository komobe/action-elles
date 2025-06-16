import React, { forwardRef } from 'react';
import { InputText } from 'primereact/inputtext';

interface InputFieldProps {
  id: string;
  name: string;
  label: string;
  value?: string | number;
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
  className?: string;
  error?: string;
  required?: boolean;
  placeholder?: string;
  disabled?: boolean;
  type?: string;
}

const InputField = forwardRef<HTMLInputElement, InputFieldProps>(
  ({ id, name, label, className, error, value, required, onChange, placeholder, disabled, type = 'text', ...props }, ref) => (
    <div className={`app-form-group ${className || ''}`}>
      <label htmlFor={id} className="app-form-label">
        {label} {required && <span className='text-red-500'>*</span>}
      </label>
      <InputText
        ref={ref}
        id={id}
        name={name}
        type={type}
        value={value !== undefined && value !== null ? String(value) : ''}
        onChange={onChange}
        placeholder={placeholder}
        disabled={disabled}
        invalid={!!error}
        className={`w-full ${error ? 'p-invalid' : ''}`}
        {...props}
      />
      {error && <small className="p-error block mt-1">{error}</small>}
    </div>
  )
);

InputField.displayName = 'InputField';

export default InputField;