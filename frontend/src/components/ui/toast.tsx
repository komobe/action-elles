import React, { createContext, useContext, useState, useCallback, ReactNode } from 'react';

interface Toast {
  id: number;
  type: 'success' | 'error' | 'warning' | 'info';
  message: string;
}

interface ToastContextType {
  addToast: (type: Toast['type'], message: string) => void;
  removeToast: (id: number) => void;
}

const ToastContext = createContext<ToastContextType | undefined>(undefined);

export function useToast() {
  const context = useContext(ToastContext);
  if (!context) {
    throw new Error('useToast must be used within a ToastProvider');
  }
  return {
    ...context,
    success: (message: string) => context.addToast('success', message),
    error: (message: string) => context.addToast('error', message),
    warning: (message: string) => context.addToast('warning', message),
    info: (message: string) => context.addToast('info', message),
  };
}

interface ToastProviderProps {
  children: ReactNode;
}

export function ToastProvider({ children }: ToastProviderProps) {
  const [toasts, setToasts] = useState<Toast[]>([]);

  const addToast = useCallback((type: Toast['type'], message: string) => {
    const id = Date.now();
    setToasts(prev => [...prev, { id, type, message }]);
    setTimeout(() => removeToast(id), 3000);
  }, []);

  const removeToast = useCallback((id: number) => {
    setToasts(prev => prev.filter(toast => toast.id !== id));
  }, []);

  return (
    <ToastContext.Provider value={{ addToast, removeToast }}>
      {children}
      <div className="fixed top-4 right-4 z-50 space-y-2">
        {toasts.map(toast => (
          <div
            key={toast.id}
            className={`max-w-md w-full p-4 rounded-lg shadow-lg transform transition-all duration-300 ease-in-out
              ${toast.type === 'success' ? 'bg-green-50 text-green-800 dark:bg-green-900 dark:text-green-200' :
                toast.type === 'error' ? 'bg-red-50 text-red-800 dark:bg-red-900 dark:text-red-200' :
                  toast.type === 'warning' ? 'bg-yellow-50 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200' :
                    'bg-blue-50 text-blue-800 dark:bg-blue-900 dark:text-blue-200'
              }`}
          >
            <div className="flex justify-between items-center">
              <p className="text-sm font-medium">{toast.message}</p>
              <button
                onClick={() => removeToast(toast.id)}
                className="ml-4 text-sm font-medium opacity-60 hover:opacity-100 focus:outline-none"
              >
                ×
              </button>
            </div>
          </div>
        ))}
      </div>
    </ToastContext.Provider>
  );
}

// Hooks d'utilité
export const toast = {
  success: (message: string) => {
    const { addToast } = useToast();
    addToast('success', message);
  },
  error: (message: string) => {
    const { addToast } = useToast();
    addToast('error', message);
  },
  warning: (message: string) => {
    const { addToast } = useToast();
    addToast('warning', message);
  },
  info: (message: string) => {
    const { addToast } = useToast();
    addToast('info', message);
  },
}; 