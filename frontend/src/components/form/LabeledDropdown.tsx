import React from 'react';
import { Dropdown, DropdownChangeEvent } from 'primereact/dropdown';

interface Option {
  [key: string]: any;
}

interface LabeledDropdownProps {
  id?: string;
  label: string;
  value: any;
  options: Option[];
  onChange: (value: any) => void;
  optionLabel?: string;
  optionValue?: string;
  placeholder?: string;
  required?: boolean;
  className?: string;
  disabled?: boolean;
}

export const LabeledDropdown: React.FC<LabeledDropdownProps> = ({
  id = 'dropdown',
  label,
  value,
  options,
  onChange,
  optionLabel = 'label',
  optionValue = 'value',
  placeholder = 'Sélectionner une option',
  required = false,
  className = '',
  disabled = false,
}) => {
  return (
    <div>
      <label
        htmlFor={id}
        className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2"
      >
        {label}{required && <span className='text-red-700'> *</span>}
      </label>
      <Dropdown
        id={id}
        value={value}
        options={options}
        onChange={(e: DropdownChangeEvent) => onChange(e.value)}
        optionLabel={optionLabel}
        optionValue={optionValue}
        placeholder={placeholder}
        className={`w-full ${className}`}
        disabled={disabled}
        required={required}
      />
    </div>
  );
};