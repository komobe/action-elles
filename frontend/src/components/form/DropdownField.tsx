import React, { forwardRef } from 'react';
import { Dropdown } from 'primereact/dropdown';

interface DropdownFieldProps {
  id: string;
  name: string;
  label: string;
  options: { label: string; value: string | number }[];
  value?: string | number;
  onChange?: (e: { target: { name: string; value: any } }) => void;
  className?: string;
  error?: string;
  placeholder?: string;
  required?: boolean;
  disabled?: boolean;
}

const DropdownField = forwardRef<any, DropdownFieldProps>(
  ({ id, name, label, options, value, onChange, className, error, placeholder, required, disabled, ...props }, ref) => (
    <div className={`app-form-group ${className || ''}`}>
      <label htmlFor={id} className="app-form-label">
        {label} {required && <span className='text-red-500'>*</span>}
      </label>
      <Dropdown
        ref={ref}
        id={id}
        name={name}
        options={options}
        value={value}
        onChange={(e) => onChange && onChange({ target: { name, value: e.value } })}
        placeholder={placeholder}
        disabled={disabled}
        invalid={!!error}
        className={`app-form-select ${error ? 'p-invalid' : ''}`}
        {...props}
      />
      {error && <small className="p-error block mt-1">{error}</small>}
    </div>
  )
);

DropdownField.displayName = 'DropdownField';

export default DropdownField;