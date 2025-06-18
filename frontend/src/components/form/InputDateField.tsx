import { forwardRef } from 'react';
import InputField from './InputField';
import { InputDateFieldProps } from './form.types';

const InputDateField = forwardRef<HTMLInputElement, InputDateFieldProps>(
  (props, ref) => (
    <InputField
      ref={ref}
      type="date"
      {...props}
    />
  )
);

InputDateField.displayName = 'InputDateField';

export default InputDateField;