import { useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faDownload } from '@fortawesome/free-solid-svg-icons';
import { useToast } from '@/contexts/ToastContext';

interface DownloadButtonProps<T> {
  onClick: () => Promise<T>;
  fileName: string;
  title?: string;
  className?: string;
  disabled?: boolean;
}

const DownloadButton = <T,>({ onClick: onDownload, fileName, title = 'Télécharger', className = '', disabled = false }: DownloadButtonProps<T>) => {
  const [isLoading, setIsLoading] = useState(false);
  const { error: showError } = useToast();

  const handleClick = async () => {
    if (disabled || isLoading) return;

    setIsLoading(true);

    try {
      const response = await onDownload();

      if (response instanceof Blob) {
        const url = URL.createObjectURL(response);
        const link = document.createElement('a');
        link.href = url;
        link.download = `${fileName}.pdf`;
        link.click();
        URL.revokeObjectURL(url);
      }
    } catch (error) {
      showError((error as any).message ?? 'Erreur lors du téléchargement:');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <button
      onClick={handleClick}
      disabled={disabled || isLoading}
      title={title}
      className={`${className} inline-flex items-center gap-2 px-3 py-2 text-sm font-medium text-blue-600 hover:text-blue-900 dark:text-blue-400 dark:hover:text-blue-300 transition-colors duration-200 disabled:opacity-50 disabled:cursor-not-allowed`}
    >
      <FontAwesomeIcon
        icon={faDownload}
        className={`w-4 h-4 ${isLoading ? 'animate-spin' : ''}`}
      />
    </button>
  );
};

export default DownloadButton; 