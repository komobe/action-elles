import {Button} from "primereact/button";

interface SubmitButtonProps {
  isDisabled: boolean;
  isLoading: boolean;
  label?: string;
}

export default function SubmitButton({ isDisabled = false, isLoading, label = "Envoyer" }: SubmitButtonProps) {
  return (
    <div className="pt-4">
      <Button
        type="submit"
        label={label}
        className="w-full"
        disabled={isDisabled}
        loading={isLoading}
      />
    </div>
  );
}
