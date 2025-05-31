import { useEffect } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCircleCheck, faCircleExclamation, faCircleInfo, faXmark } from '@fortawesome/free-solid-svg-icons';

export type ToastType = 'success' | 'error' | 'info';

interface ToastProps {
  message: string;
  type: ToastType;
  onClose: () => void;
  duration?: number;
}

const Toast = ({ message, type, onClose, duration = 3000 }: ToastProps) => {
  useEffect(() => {
    const timer = setTimeout(() => {
      onClose();
    }, duration);

    return () => clearTimeout(timer);
  }, [duration, onClose]);

  const getIcon = () => {
    switch (type) {
      case 'success':
        return <FontAwesomeIcon icon={faCircleCheck} className="h-5 w-5 text-green-400" />;
      case 'error':
        return <FontAwesomeIcon icon={faCircleExclamation} className="h-5 w-5 text-red-400" />;
      case 'info':
        return <FontAwesomeIcon icon={faCircleInfo} className="h-5 w-5 text-blue-400" />;
    }
  };

  const getBackgroundColor = () => {
    switch (type) {
      case 'success':
        return 'bg-green-50 dark:bg-green-900/50';
      case 'error':
        return 'bg-red-50 dark:bg-red-900/50';
      case 'info':
        return 'bg-blue-50 dark:bg-blue-900/50';
    }
  };

  const getBorderColor = () => {
    switch (type) {
      case 'success':
        return 'border-green-400';
      case 'error':
        return 'border-red-400';
      case 'info':
        return 'border-blue-400';
    }
  };

  return (
    <div
      className={`flex items-center p-4 border-l-4 ${getBackgroundColor()} ${getBorderColor()} w-full max-w-sm`}
      role="alert"
    >
      <div className="flex-shrink-0">{getIcon()}</div>
      <div className="ml-3 flex-grow">
        <p className="text-sm font-medium text-gray-900 dark:text-gray-100">{message}</p>
      </div>
      <button
        className="ml-4 flex-shrink-0 text-gray-400 hover:text-gray-500 focus:outline-none"
        onClick={onClose}
      >
        <span className="sr-only">Fermer</span>
        <FontAwesomeIcon icon={faXmark} className="h-5 w-5" />
      </button>
    </div>
  );
};

export default Toast; 