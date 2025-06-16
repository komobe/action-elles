import React, { forwardRef } from 'react';
import { Password } from 'primereact/password';

interface PasswordFieldProps {
  id: string;
  name: string;
  label: string;
  value?: string;
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
  className?: string;
  error?: string;
  required?: boolean;
  placeholder?: string;
  disabled?: boolean;
}
const PasswordField = forwardRef<HTMLInputElement, PasswordFieldProps>(
  ({ id, name, label, className, error, value, required, onChange, placeholder, disabled, ...props }, ref) => {
    return (
      <div className={`app-form-group ${className ?? ''}`}>
        <label htmlFor={id} className="app-form-label">
          {label} {required && <span className='text-red-500'>*</span>}
        </label>
        <Password
          id={id}
          name={name}
          inputRef={ref}
          value={value ?? ''}
          onChange={onChange}
          placeholder={placeholder}
          disabled={disabled}
          toggleMask
          feedback={false}
          invalid={!!error}
          {...props}
        />
        {error && <small className="p-error block mt-1">{error}</small>}
      </div>
    );
  }
);

PasswordField.displayName = 'PasswordField';

export default PasswordField;