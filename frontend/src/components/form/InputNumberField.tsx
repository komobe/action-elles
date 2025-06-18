import { forwardRef } from 'react';
import { InputNumber } from 'primereact/inputnumber';
import { InputNumberFieldProps } from './form.types';

// Valeurs par défaut
const DEFAULT_CONFIG = {
  locale: 'fr-FR',
  currency: 'XOF',
  mode: 'decimal' as 'decimal' | 'currency',
  step: 1,
  minFractionDigits: 0,
  maxFractionDigits: 2,
} as const;

const InputNumberField = forwardRef<InputNumber, InputNumberFieldProps & Partial<typeof DEFAULT_CONFIG>>(
  ({
    label,
    onChange,
    className = '',
    error,
    required = false,
    placeholder,
    ...props
  }, ref) => {
    const propsOverride = { ...DEFAULT_CONFIG, ...props };

    return (
      <div className={`app-form-group ${className}`.trim()}>
        <label htmlFor={props.id} className="app-form-label">
          {label} {required && <span className="text-red-500" aria-label="champ requis">*</span>}
        </label>
        <InputNumber
          ref={ref}
          placeholder={placeholder ?? `Entrez ${label?.toLowerCase() ?? 'une valeur'}`}
          onChange={(e) => onChange?.({ target: { name: props.name, value: e.value } })}
          invalid={!!error}
          className={`w-full ${error ? 'p-invalid' : ''}`.trim()}
          required={required}
          aria-invalid={!!error}
          aria-describedby={error ? `${props.id}-error` : undefined}
          {...propsOverride}
        />
        {error && (
          <small
            id={`${props.id}-error`}
            className="p-error block mt-1"
            role="alert"
          >
            {error}
          </small>
        )}
      </div>
    );
  }
);

InputNumberField.displayName = 'InputNumberField';

export default InputNumberField;