import React, { useMemo } from 'react';

interface PaginationProps {
  currentPage: number;
  defautPageSize?: number;
  currentPageSize?: number;
  totalRecords: number;
  onPageChange: (page: number) => void;
  onPageSizeChange: (size: number) => void;
  pageSizeOptions?: number[];
  displayedItemsCount: number;
}

const DEFAULT_PAGE_SIZE = 5;
const DEFAULT_PAGINATION_OPTIONS = [DEFAULT_PAGE_SIZE, 10, 20, 50];

const Pagination: React.FC<PaginationProps> = ({
  currentPage,
  defautPageSize = DEFAULT_PAGE_SIZE,
  currentPageSize,
  totalRecords,
  onPageChange,
  onPageSizeChange,
  pageSizeOptions = DEFAULT_PAGINATION_OPTIONS,
  displayedItemsCount,
}) => {

  const effectivePageSize = useMemo(() => {
    return currentPageSize ?? defautPageSize ?? DEFAULT_PAGE_SIZE;
  }, [currentPageSize, defautPageSize]);

  // Garantir que le defautPageSize est toujours présent dans les options
  const normalizedPageSizeOptions = useMemo(() => {
    const optionsSet = new Set(pageSizeOptions);
    optionsSet.add(defautPageSize);
    return Array.from(optionsSet).sort((a, b) => a - b);
  }, [defautPageSize, pageSizeOptions]);

  const totalPages = Math.ceil(totalRecords / effectivePageSize);

  const { startIndex, endIndex } = useMemo(() => {
    if (displayedItemsCount === 0) return { startIndex: 0, endIndex: 0 };
    const start = (currentPage - 1) * effectivePageSize + 1;
    const end = Math.min(currentPage * effectivePageSize, totalRecords);
    return { startIndex: start, endIndex: end };
  }, [currentPage, effectivePageSize, totalRecords, displayedItemsCount]);

  const renderPageButton = (page: number) => (
    <button
      key={page}
      onClick={() => onPageChange(page)}
      className={`w-8 h-8 flex items-center justify-center text-sm ${page === currentPage
        ? 'border-2 border-primary-600 rounded-full text-primary-600'
        : 'text-gray-700 hover:text-primary-600'
        }`}
    >
      {page}
    </button>
  );

  const renderPageNumbers = () => {
    const pages = [];
    const maxPagesToShow = 5;

    let startPage = Math.max(1, currentPage - Math.floor(maxPagesToShow / 2));
    let endPage = Math.min(totalPages, startPage + maxPagesToShow - 1);

    if (endPage - startPage < maxPagesToShow - 1) {
      startPage = Math.max(1, endPage - maxPagesToShow + 1);
    }

    if (startPage > 1) {
      pages.push(renderPageButton(1));
      if (startPage > 2) {
        pages.push(
          <span key="start-ellipsis" className="px-2 text-gray-500">
            ...
          </span>
        );
      }
    }

    for (let i = startPage; i <= endPage; i++) {
      pages.push(renderPageButton(i));
    }

    if (endPage < totalPages) {
      if (endPage < totalPages - 1) {
        pages.push(
          <span key="end-ellipsis" className="px-2 text-gray-500">
            ...
          </span>
        );
      }
      pages.push(renderPageButton(totalPages));
    }

    return pages;
  };

  return (
    <div className="bg-white dark:bg-gray-800 px-4 py-3 flex flex-col sm:flex-row sm:items-center sm:justify-between border-t border-gray-200 dark:border-gray-700 space-y-2 sm:space-y-0">
      {/* Résumé */}
      <div>
        <p className="text-sm text-gray-700 dark:text-gray-300">
          Affichage de <span className="font-medium">{startIndex}</span> à{' '}
          <span className="font-medium">{endIndex}</span> sur{' '}
          <span className="font-medium">{totalRecords}</span> résultats
        </p>
      </div>

      {/* Contrôles de pagination */}
      <div className="flex items-center space-x-2 sm:space-x-3 overflow-x-auto">
        {/* Première / Précédente */}
        {totalPages > 1 && (
          <>
            <button
              onClick={() => onPageChange(1)}
              disabled={currentPage === 1}
              aria-label="Première page"
              className="w-8 h-8 flex items-center justify-center text-gray-700 hover:text-primary-600 disabled:opacity-50"
            >
              <i className="pi pi-angle-double-left" />
            </button>
            <button
              onClick={() => onPageChange(currentPage - 1)}
              disabled={currentPage === 1}
              aria-label="Page précédente"
              className="w-8 h-8 flex items-center justify-center text-gray-700 hover:text-primary-600 disabled:opacity-50"
            >
              <i className="pi pi-chevron-left" />
            </button>
          </>
        )}

        {/* Numéros de pages */}
        <div className="flex items-center space-x-1">{renderPageNumbers()}</div>

        {/* Suivante / Dernière */}
        {totalPages > 1 && (
          <>
            <button
              onClick={() => onPageChange(currentPage + 1)}
              disabled={currentPage === totalPages}
              aria-label="Page suivante"
              className="w-8 h-8 flex items-center justify-center text-gray-700 hover:text-primary-600 disabled:opacity-50"
            >
              <i className="pi pi-chevron-right" />
            </button>
            <button
              onClick={() => onPageChange(totalPages)}
              disabled={currentPage === totalPages}
              aria-label="Dernière page"
              className="w-8 h-8 flex items-center justify-center text-gray-700 hover:text-primary-600 disabled:opacity-50"
            >
              <i className="pi pi-angle-double-right" />
            </button>
          </>
        )}

        {/* Sélecteur taille de page */}
        <div className="ml-4 shrink-0">
          <select
            value={effectivePageSize}
            onChange={(e) => onPageSizeChange(Number(e.target.value))}
            className="py-2 px-3 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500 sm:text-sm min-w-24"
            aria-label="Nombre d'éléments par page"
          >
            {normalizedPageSizeOptions.map((size) => (
              <option key={size} value={size}>
                {size} par page
              </option>
            ))}
          </select>
        </div>
      </div>
    </div>
  );
};

export default Pagination;