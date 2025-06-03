import { useAuth } from '../contexts/AuthContext';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBell, faCircleUser } from '@fortawesome/free-solid-svg-icons';
import { useState, memo, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';

interface TopbarProps {
  isCollapsed: boolean;
}

const Topbar = memo(({ isCollapsed }: TopbarProps) => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [isProfileOpen, setIsProfileOpen] = useState(false);

  const handleLogout = useCallback(() => {
    logout();
    navigate('/login');
    setIsProfileOpen(false);
  }, [logout, navigate]);

  const toggleProfile = useCallback(() => {
    setIsProfileOpen(prev => !prev);
  }, []);

  const handleProfileClick = useCallback(() => {
    navigate('/profile');
    setIsProfileOpen(false);
  }, [navigate]);

  const handleSettingsClick = useCallback(() => {
    navigate('/settings');
    setIsProfileOpen(false);
  }, [navigate]);

  return (
    <div className={`fixed top-0 right-0 bg-white dark:bg-gray-800 z-20 border-b border-gray-200 dark:border-gray-700
      ${isCollapsed ? 'lg:left-20' : 'lg:left-64'}
      left-0`}
    >
      <div className="h-16 px-4 flex items-center justify-end">
        <div className="flex items-center space-x-4">
          <button
            className="p-2 text-gray-600 hover:text-gray-900 dark:text-gray-300 dark:hover:text-white hover:bg-gray-100 dark:hover:bg-gray-700 rounded"
            aria-label="Notifications"
          >
            <FontAwesomeIcon icon={faBell} className="w-5 h-5" />
          </button>

          <div className="relative">
            <button
              onClick={toggleProfile}
              className="flex items-center space-x-3 p-2 hover:bg-gray-100 dark:hover:bg-gray-700 rounded"
            >
              <FontAwesomeIcon icon={faCircleUser} className="w-6 h-6 text-gray-600 dark:text-gray-300" />
              <span className="hidden sm:block text-sm text-gray-600 dark:text-gray-300">
                {user?.username}
              </span>
            </button>

            {isProfileOpen && (
              <div className="absolute right-0 mt-2 w-48 bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-700 rounded">
                <div className="px-4 py-2 border-b border-gray-200 dark:border-gray-700">
                  <p className="text-sm font-medium text-gray-900 dark:text-white">
                    {user?.username}
                  </p>
                </div>
                <button
                  onClick={handleProfileClick}
                  className="block w-full text-left px-4 py-2 text-sm text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 rounded"
                >
                  Profil
                </button>
                <button
                  onClick={handleSettingsClick}
                  className="block w-full text-left px-4 py-2 text-sm text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 rounded"
                >
                  Paramètres
                </button>
                <button
                  onClick={handleLogout}
                  className="block w-full text-left px-4 py-2 text-sm text-red-600 dark:text-red-400 hover:bg-gray-100 dark:hover:bg-gray-700 rounded"
                >
                  Déconnexion
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
});

Topbar.displayName = 'Topbar';

export default Topbar; 