import { Outlet } from 'react-router-dom';
import { useState, memo, useCallback } from 'react';
import Sidebar from './Sidebar';
import Topbar from './Topbar';

const Layout = memo(() => {
  const [isCollapsed, setIsCollapsed] = useState(false);
  const [isMobileOpen, setIsMobileOpen] = useState(false);

  const toggleSidebar = useCallback(() => {
    setIsCollapsed(prev => !prev);
  }, []);

  const toggleMobile = useCallback(() => {
    setIsMobileOpen(prev => !prev);
  }, []);

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-900">
      <Sidebar
        isCollapsed={isCollapsed}
        isMobileOpen={isMobileOpen}
        toggleSidebar={toggleSidebar}
        toggleMobile={toggleMobile}
      />
      <Topbar isCollapsed={isCollapsed} />

      {/* Main Content */}
      <div className={`pt-16 transition-all duration-300 ease-in-out
        ${isCollapsed ? 'lg:ml-20' : 'lg:ml-64'}
        ${isMobileOpen ? 'ml-0' : 'ml-0'}`}
      >
        <main className="max-w-7xl mx-auto py-6 px-4 sm:px-6 lg:px-8">
          <Outlet />
        </main>
      </div>
    </div>
  );
});

Layout.displayName = 'Layout';

export default Layout; 