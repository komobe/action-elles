import { forwardRef } from 'react';
import { InputText } from 'primereact/inputtext';
import { InputFieldProps } from './form.types';

const InputField = forwardRef<HTMLInputElement, InputFieldProps>(
  ({ label, className = '', error, value, required = false, type = 'text', placeholder, ...props }, ref) => (
    <div className={`app-form-group ${className}`.trim()}>
      <label htmlFor={props.id} className="app-form-label">
        {label} {required && <span className="text-red-500">*</span>}
      </label>
      <InputText
        ref={ref}
        type={type}
        value={value != null ? String(value) : ''}
        placeholder={placeholder ?? `Entrez ${label.toLowerCase()}`}
        invalid={!!error}
        className={`w-full ${error ? 'p-invalid' : ''}`.trim()}
        {...props}
      />
      {error && <small className="p-error block mt-1">{error}</small>}
    </div>
  )
);

InputField.displayName = 'InputField';

export default InputField;