import {createContext, ReactNode, useContext, useRef} from 'react';
import {Toast} from 'primereact/toast';

type ToastType = 'success' | 'info' | 'warn' | 'error';

interface ToastContextType {
  success: (message: string) => void;
  info: (message: string) => void;
  warn: (message: string) => void;
  error: (message: string) => void;
}

const ToastContext = createContext<ToastContextType | undefined>(undefined);

export const ToastProvider = ({ children }: { children: ReactNode }) => {
  const toast = useRef<Toast>(null);

  const showToast = (severity: ToastType, message: string) => {
    toast.current?.show({
      severity,
      summary: severity.charAt(0).toUpperCase() + severity.slice(1),
      detail: message,
      life: 3000
    });
  };

  const success = (message: string) => showToast('success', message);
  const info = (message: string) => showToast('info', message);
  const warn = (message: string) => showToast('warn', message);
  const error = (message: string) => showToast('error', message);

  return (
    <ToastContext.Provider value={{ success, info, warn, error }}>
      {children}
      <Toast ref={toast} position="top-right" />
    </ToastContext.Provider>
  );
};

export const useToast = () => {
  const context = useContext(ToastContext);
  if (context === undefined) {
    throw new Error('useToast must be used within a ToastProvider');
  }
  return context;
}; 