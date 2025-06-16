// Configuration par défaut de PrimeReact
export const PRIMEREACT_CONFIG = {
  ripple: false,
  inputStyle: 'outlined',
  zIndex: {
    modal: 1100,        // Dialog, Sidebar
    overlay: 1000,      // Dropdown, Overlay Panel
    menu: 1000,         // Menu
    tooltip: 1100,      // Tooltip
    toast: 1200         // Toast
  }
} as const;

// Configuration des styles de formulaire
export const FORM_LAYOUT = {
  labelClass: 'modern-form-label',
  inputClass: 'modern-form-input',
  fieldClass: 'modern-form-group',
  buttonClass: 'modern-form-button-primary'
} as const; 