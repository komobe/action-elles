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
  labelClass: 'block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1',
  inputClass: 'w-full',
  fieldClass: 'mb-4',
  buttonClass: 'p-button-primary'
} as const; 