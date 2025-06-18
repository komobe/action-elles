import { forwardRef } from 'react';
import InputField from './InputField';
import { InputFieldProps } from './form.types';

const InputTextField = forwardRef<HTMLInputElement, InputFieldProps>(
  (props, ref) => (
    <InputField
      ref={ref}
      type="text"
      {...props}
    />
  )
);

InputTextField.displayName = 'InputTextField';

export default InputTextField;