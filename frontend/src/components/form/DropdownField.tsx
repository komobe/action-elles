import { forwardRef } from 'react';
import { Dropdown } from 'primereact/dropdown';
import { DropdownFieldProps } from './form.types';

const DropdownField = forwardRef<Dropdown, DropdownFieldProps>(
  ({ label, onChange, className = '', error, required = false, ...props }, ref) => (
    <div className={`app-form-group ${className}`.trim()}>
      <label htmlFor={props.id} className="app-form-label">
        {label} {required && <span className="text-red-500">*</span>}
      </label>
      <Dropdown
        ref={ref}
        onChange={(e) => onChange?.({ target: { name: props.name, value: e.value } })}
        invalid={!!error}
        className={`app-form-select ${error ? 'p-invalid' : ''}`.trim()}
        {...props}
      />
      {error && <small className="p-error block mt-1">{error}</small>}
    </div>
  )
);

DropdownField.displayName = 'DropdownField';

export default DropdownField;