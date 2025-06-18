import { forwardRef } from 'react';
import { Password } from 'primereact/password';
import { PasswordFieldProps } from './form.types';

const PasswordField = forwardRef<HTMLInputElement, PasswordFieldProps>(
  ({ label, className = '', error, required = false, onChange, ...props }, ref) => (
    <div className={`app-form-group ${className}`.trim()}>
      <label htmlFor={props.id} className="app-form-label">
        {label} {required && <span className="text-red-500">*</span>}
      </label>
      <Password
        inputRef={ref}
        value={props.value ?? ''}
        onChange={onChange}
        toggleMask
        feedback={false}
        invalid={!!error}
        required={required}
        {...props}
      />
      {error && <small className="p-error block mt-1">{error}</small>}
    </div>
  )
);

PasswordField.displayName = 'PasswordField';

export default PasswordField;