import { useEffect } from 'react';
import { FaCheckCircle, FaExclamationCircle, FaInfoCircle } from 'react-icons/fa';

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
        return <FaCheckCircle className="h-5 w-5 text-green-400" />;
      case 'error':
        return <FaExclamationCircle className="h-5 w-5 text-red-400" />;
      case 'info':
        return <FaInfoCircle className="h-5 w-5 text-blue-400" />;
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
      className={`flex items-center p-4 rounded-lg shadow-lg border-l-4 ${getBackgroundColor()} ${getBorderColor()} transition-all duration-500 transform translate-y-0 w-full max-w-sm`}
      role="alert"
    >
      <div className="flex-shrink-0">{getIcon()}</div>
      <div className="ml-3 flex-grow">
        <p className="text-sm font-medium text-gray-900 dark:text-gray-100">{message}</p>
      </div>
      <button
        className="ml-4 flex-shrink-0 text-gray-400 hover:text-gray-500 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
        onClick={onClose}
      >
        <span className="sr-only">Fermer</span>
        <svg className="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
          <path
            fillRule="evenodd"
            d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
            clipRule="evenodd"
          />
        </svg>
      </button>
    </div>
  );
};

export default Toast; 