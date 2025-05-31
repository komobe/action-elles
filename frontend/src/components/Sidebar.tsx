import React, { ReactElement } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { FaHome, FaCalculator, FaFileSignature, FaInfoCircle, FaSignOutAlt, FaBars, FaTimes } from 'react-icons/fa';
import { useAuth } from '../contexts/AuthContext';

interface MenuItem {
  path: string;
  label: string;
  icon: ReactElement;
}

interface SidebarProps {
  isCollapsed: boolean;
  isMobileOpen: boolean;
  toggleSidebar: () => void;
  toggleMobile: () => void;
}

const mainMenuItems: MenuItem[] = [
  { path: '/home', label: 'Tableau de bord', icon: <FaHome className="w-5 h-5" /> },
  { path: '/simuler-devis', label: 'Simuler un devis', icon: <FaCalculator className="w-5 h-5" /> },
  { path: '/souscription/creer', label: 'Nouvelle souscription', icon: <FaFileSignature className="w-5 h-5" /> },
];

const bottomMenuItem: MenuItem = { path: '/about', label: 'À propos', icon: <FaInfoCircle className="w-5 h-5" /> };

const Sidebar = ({ isCollapsed, isMobileOpen, toggleSidebar, toggleMobile }: SidebarProps) => {
  const location = useLocation();
  const { user, logout } = useAuth();

  const handleLogout = () => {
    logout();
  };

  const isActiveRoute = (path: string) => {
    if (path === '/home' && (location.pathname === '/' || location.pathname === '/home')) {
      return true;
    }
    return location.pathname === path;
  };

  const renderMenuItem = (item: MenuItem) => (
    <Link
      to={item.path}
      className={`flex items-center ${!isCollapsed ? 'space-x-3' : ''} px-3 py-3 transition-all duration-200
        ${isActiveRoute(item.path)
          ? 'bg-indigo-50 text-indigo-600 dark:bg-indigo-900/50 dark:text-indigo-400'
          : 'text-gray-600 hover:bg-gray-100 dark:text-gray-300 dark:hover:bg-gray-700/50'
        }
        ${isCollapsed ? 'justify-center' : ''}`}
      onClick={() => {
        if (window.innerWidth < 1024) {
          toggleMobile();
        }
      }}
    >
      <div className="flex-shrink-0">{item.icon}</div>
      {!isCollapsed && (
        <span className="text-sm font-medium whitespace-nowrap">{item.label}</span>
      )}
      {isCollapsed && (
        <span className="sr-only">{item.label}</span>
      )}
    </Link>
  );

  return (
    <>
      {/* Overlay pour mobile */}
      {isMobileOpen && (
        <div
          className="fixed inset-0 bg-black bg-opacity-50 z-20 lg:hidden transition-opacity duration-300"
          onClick={toggleMobile}
        />
      )}

      {/* Bouton toggle mobile */}
      <button
        id="sidebar-toggle"
        onClick={toggleMobile}
        className="fixed top-4 left-4 z-30 lg:hidden bg-white dark:bg-gray-800 p-2 rounded-lg shadow-lg hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors duration-200"
        aria-label="Toggle Menu"
      >
        {isMobileOpen ? (
          <FaTimes className="w-5 h-5 text-gray-600 dark:text-gray-300" />
        ) : (
          <FaBars className="w-5 h-5 text-gray-600 dark:text-gray-300" />
        )}
      </button>

      {/* Sidebar */}
      <aside
        id="sidebar"
        className={`fixed top-0 left-0 h-full bg-white dark:bg-gray-800 shadow-xl z-30 transition-all duration-300 ease-in-out flex flex-col
          ${isCollapsed ? 'w-20' : 'w-64'}
          ${isMobileOpen ? 'translate-x-0' : '-translate-x-full lg:translate-x-0'}`}
      >
        {/* En-tête */}
        <div className="flex items-center justify-between h-16 px-4 border-b border-gray-200 dark:border-gray-700 flex-shrink-0">
          <div className={`flex items-center ${isCollapsed ? 'justify-center w-full' : ''}`}>
            {!isCollapsed && (
              <span className="text-lg font-bold text-indigo-600 dark:text-indigo-400 truncate">
                Action'Elles
              </span>
            )}
          </div>
          <button
            onClick={toggleSidebar}
            className="hidden lg:block text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200 p-1 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-700/50 transition-colors duration-200"
            aria-label="Toggle Sidebar"
          >
            <FaBars className="w-4 h-4" />
          </button>
        </div>

        {/* Menu principal */}
        <nav className="flex-1 overflow-y-auto">
          <ul className="flex flex-col gap-0 mt-2">
            {mainMenuItems.map((item) => (
              <li key={item.path}>
                {renderMenuItem(item)}
              </li>
            ))}
          </ul>
        </nav>

        {/* Menu du bas */}
        <div className="px-2 border-t border-gray-200 dark:border-gray-700">
          {renderMenuItem(bottomMenuItem)}
        </div>

        {/* Pied de page */}
        <div className="border-t border-gray-200 dark:border-gray-700 p-4 flex-shrink-0">
          <div className={`flex items-center ${isCollapsed ? 'justify-center' : 'space-x-3'}`}>
            {!isCollapsed && (
              <div className="flex-1 min-w-0">
                <p className="text-sm font-medium text-gray-900 dark:text-white truncate">
                  {user?.username}
                </p>
              </div>
            )}
            <button
              onClick={handleLogout}
              className={`p-2 text-gray-500 hover:text-red-600 dark:text-gray-400 dark:hover:text-red-400 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-700/50 transition-colors duration-200
                ${isCollapsed ? 'w-full flex justify-center' : ''}`}
              aria-label="Se déconnecter"
            >
              <FaSignOutAlt className="w-5 h-5" />
              {isCollapsed && <span className="sr-only">Se déconnecter</span>}
            </button>
          </div>
        </div>
      </aside>
    </>
  );
};

export default Sidebar; 