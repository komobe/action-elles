import {
  faBars,
  faCalculator,
  faChevronLeft,
  faChevronRight,
  faClipboardList,
  faHome,
  faUsers,
  faXmark
} from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { ReactElement } from 'react';
import { Link, useLocation } from 'react-router-dom';

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
  { path: '/home', label: 'Tableau de bord', icon: <FontAwesomeIcon icon={faHome} className="w-5 h-5" /> },
  { path: '/simuler-devis', label: 'Simuler un devis', icon: <FontAwesomeIcon icon={faCalculator} className="w-5 h-5" /> },
  { path: '/souscriptions', label: 'Souscriptions', icon: <FontAwesomeIcon icon={faClipboardList} className="w-5 h-5" /> },
  { path: '/utilisateurs', label: 'Gestion utilisateurs', icon: <FontAwesomeIcon icon={faUsers} className="w-5 h-5" /> },
];

//const bottomMenuItem: MenuItem = { path: '/about', label: 'À propos', icon: <FontAwesomeIcon icon={faCircleInfo} className="w-5 h-5" /> };

const Sidebar = ({ isCollapsed, isMobileOpen, toggleSidebar, toggleMobile }: SidebarProps) => {
  const location = useLocation();

  const isActiveRoute = (path: string) => {
    if (path === '/home' && (location.pathname === '/' || location.pathname === '/home')) {
      return true;
    }
    if (path === '/souscriptions' && location.pathname.startsWith('/souscription')) {
      return true;
    }
    return location.pathname === path;
  };

  const renderMenuItem = (item: MenuItem) => (
    <Link
      to={item.path}
      className={`flex items-center w-full px-4 h-14
        ${isActiveRoute(item.path)
          ? 'bg-indigo-50 text-indigo-600 dark:bg-indigo-900/30 dark:text-indigo-400 border-r-2 border-indigo-600 dark:border-indigo-400'
          : 'text-gray-600 hover:bg-gray-50 dark:text-gray-300 dark:hover:bg-gray-700/30'
        }
        ${isCollapsed ? 'justify-center px-0' : ''}`}
      onClick={() => {
        if (window.innerWidth < 1024) {
          toggleMobile();
        }
      }}
    >
      <div className="w-10 flex items-center justify-center">
        {item.icon}
      </div>
      {!isCollapsed && (
        <span className="text-sm font-medium truncate">{item.label}</span>
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
          className="fixed inset-0 bg-black/20 backdrop-blur-sm z-20 lg:hidden"
          onClick={toggleMobile}
        />
      )}

      {/* Bouton toggle mobile */}
      <button
        id="sidebar-toggle"
        onClick={toggleMobile}
        className="fixed top-4 left-4 z-30 lg:hidden bg-white dark:bg-gray-800 p-2 border border-gray-200 dark:border-gray-700 rounded-md"
        aria-label="Toggle Menu"
      >
        <FontAwesomeIcon
          icon={isMobileOpen ? faXmark : faBars}
          className="w-5 h-5 text-gray-600 dark:text-gray-300"
        />
      </button>

      {/* Sidebar */}
      <aside
        id="sidebar"
        className={`fixed top-0 left-0 h-full bg-white dark:bg-gray-800 z-30 border-r border-gray-200 dark:border-gray-700 flex flex-col
          ${isCollapsed ? 'w-16' : 'w-64'}
          ${isMobileOpen ? 'translate-x-0' : '-translate-x-full lg:translate-x-0'}`}
      >
        {/* En-tête */}
        <div className="h-16 px-4 flex items-center justify-between border-b border-gray-200 dark:border-gray-700">
          <div className={`flex items-center ${isCollapsed ? 'justify-center w-full' : ''}`}>
            {!isCollapsed && (
              <span className="text-lg font-semibold text-gray-900 dark:text-white truncate pl-1.5">
                Action'Elles
              </span>
            )}
          </div>
          <button
            onClick={toggleSidebar}
            className="hidden lg:flex items-center justify-center w-8 h-8 text-gray-400 hover:text-gray-600 dark:text-gray-500 dark:hover:text-gray-300 border border-current rounded-md"
            aria-label="Toggle Sidebar"
          >
            <FontAwesomeIcon
              icon={isCollapsed ? faChevronRight : faChevronLeft}
              className="w-4 h-4"
            />
          </button>
        </div>

        {/* Menu principal */}
        <nav className="flex-1 overflow-y-auto py-3">
          <ul className="flex flex-col space-y-1">
            {mainMenuItems.map((item) => (
              <li key={item.path} className="w-full">
                {renderMenuItem(item)}
              </li>
            ))}
          </ul>
        </nav>

        {/* Menu du bas
        <div className="border-t border-gray-200 dark:border-gray-700">
          <ul className="flex flex-col">
            <li className="w-full">
              {renderMenuItem(bottomMenuItem)}
            </li>
          </ul>
        </div>
*/}
        {/* Pied de page
        <div className="border-t border-gray-200 dark:border-gray-700 px-4 h-16 flex items-center">
          <div className={`flex items-center w-full ${isCollapsed ? 'justify-center' : 'space-x-3'}`}>
            {!isCollapsed && (
              <div className="flex-1 min-w-0 pl-1.5">
                <p className="text-sm font-medium text-gray-900 dark:text-white truncate">
                  {user?.username}
                </p>
              </div>
            )}
            <button
              onClick={handleLogout}
              className="w-8 h-8 flex items-center justify-center text-gray-400 hover:text-red-600 dark:text-gray-500 dark:hover:text-red-400 border border-current rounded-md"
              aria-label="Se déconnecter"
            >
              <FontAwesomeIcon icon={faRightFromBracket} className="w-4 h-4" />
              {isCollapsed && <span className="sr-only">Se déconnecter</span>}
            </button>
          </div>
        </div>
        */}
      </aside>
    </>
  );
};

export default Sidebar; 