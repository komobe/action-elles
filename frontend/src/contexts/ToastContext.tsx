import {ReactNode, useCallback, useMemo, useRef} from 'react';
import {Toast} from 'primereact/toast';
import {ToastContext} from './toast-context';

type ToastType = 'success' | 'info' | 'warn' | 'error';

export const ToastProvider = ({children}: { children: ReactNode }) => {
  const toast = useRef<Toast>(null);

  const showToast = useCallback((severity: ToastType, message: string) => {
    toast.current?.show({
      severity,
      summary: severity.charAt(0).toUpperCase() + severity.slice(1),
      detail: message,
      life: 3000
    });
  }, []);

  const success = useCallback((message: string) => showToast('success', message), [showToast]);
  const info = useCallback((message: string) => showToast('info', message), [showToast]);
  const warn = useCallback((message: string) => showToast('warn', message), [showToast]);
  const error = useCallback((message: string) => showToast('error', message), [showToast]);

  const props = useMemo(() =>
      ({success, info, warn, error}), [success, info, warn, error]
  );

  return (
      <ToastContext.Provider value={props}>
        {children}
        <Toast ref={toast} position="top-right"/>
      </ToastContext.Provider>
  );
};