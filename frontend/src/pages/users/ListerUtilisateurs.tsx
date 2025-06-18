import ActionsButtons, { ActionList } from '@/components/common/ActionsButtons';
import Pagination from '@/components/common/Pagination';
import { DropdownField, InputField, PasswordField } from '@/components/form';
import InputTextField from '@/components/form/InputTextField';
import { useAuth } from '@/hooks/useAuth';
import { isPaginatedResponse } from '@/services/http/helpers';
import { Role, roleHttpService } from '@/services/role.http-service';
import { User, utilisateurHttpService } from '@/services/utilisateur.http-service';
import { useToast } from '@/hooks/useToast';
import { faKey } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Button } from 'primereact/button';
import { Message } from "primereact/message";
import { Tag } from 'primereact/tag';
import React, { useCallback, useEffect, useMemo, useState } from 'react';
import { HttpError } from "@services/http/ http-error.ts";

interface RegisterForm {
  username: string;
  password: string;
  confirmPassword: string;
}

const MESSAGES = {
  SUCCESS: {
    USER_DELETED: 'Utilisateur supprimé avec succès',
    USER_UPDATED: 'Utilisateur mis à jour avec succès',
    PASSWORD_RESET: 'Mot de passe réinitialisé avec succès',
  },
  ERROR: {
    LOADING_USERS: 'Erreur de chargement des utilisateurs',
    LOADING_ROLES: 'Erreur lors du chargement des rôles',
    DELETING_USER: 'Erreur lors de la suppression de l\'utilisateur',
    UPDATING_USER: 'Erreur lors de la mise à jour de l\'utilisateur',
    RESETTING_PASSWORD: 'Erreur lors de la réinitialisation du mot de passe',
    GENERIC: 'Une erreur est survenue',
  },
} as const;

const DEFAULT_PAGE_SIZE = 5;

const ROLE_FILTER_ALL: Role = { label: 'Tous les rôles', value: 'ALL' }

const ListerUtilisateurs = () => {
  const [users, setUsers] = useState<User[]>([]);
  const [roles, setRoles] = useState<Role[]>([]);
  const [totalRecords, setTotalRecords] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  const [currentPageSize, setCurrentPageSize] = useState<number>(DEFAULT_PAGE_SIZE);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [roleFilter, setRoleFilter] = useState<string>(ROLE_FILTER_ALL.value);

  const [showEditModal, setShowEditModal] = useState(false);
  const [showPasswordModal, setShowPasswordModal] = useState(false);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [selectedUser, setSelectedUser] = useState<User | null>(null);
  const [editedUser, setEditedUser] = useState<Partial<User>>({});
  const [newPassword, setNewPassword] = useState('');

  const [showRegisterModal, setShowRegisterModal] = useState(false);
  const [registerForm, setRegisterForm] = useState<RegisterForm>({
    username: '',
    password: '',
    confirmPassword: ''
  });

  const [registerErrors, setRegisterErrors] = useState<Partial<RegisterForm>>({});
  const [registerGlobalError, setRegisterGlobalError] = useState<string>('');
  const [registerLoading, setRegisterLoading] = useState(false);
  const { register } = useAuth();
  const { success: showSuccess, error: showError } = useToast();

  const fetchRoles = useCallback(async () => {
    try {
      const response = await roleHttpService.lister();
      setRoles(response || []);
    } catch (error) {
      if (error instanceof HttpError) {
        showError(error.message);
      } else {
        showError(MESSAGES.ERROR.LOADING_ROLES);
      }
    }
  }, [showError]);

  const fetchUsers = useCallback(async (page: number, size: number) => {
    setIsLoading(true);
    setError(null);

    try {
      const response = await utilisateurHttpService.lister(page, size);
      setUsers(response.data ?? []);
      let totalElements = 0;
      let number = page;
      if (isPaginatedResponse(response)) {
        totalElements = response.metadata?.totalElements ?? 0;
        number = response.metadata?.number ?? page;
      }

      setTotalRecords(totalElements);
      setCurrentPage(number);
    } catch (error: unknown) {
      const errorMessage = error instanceof Error ? error.message : MESSAGES.ERROR.LOADING_USERS;
      setError(errorMessage);
      showError(errorMessage);
    } finally {
      setIsLoading(false);
    }
  }, [showError]);

  const roleOptions = useMemo(() => {
    return [ROLE_FILTER_ALL, ...roles];
  }, [roles]);

  const filteredUsers = useMemo(() => {
    return users.filter((user) => {
      const matchesSearch = user.username?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        user.email?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        user.role?.toLowerCase().includes(searchTerm.toLowerCase());

      const matchesRole = roleFilter === ROLE_FILTER_ALL.value || user.role === roleFilter;


      return matchesSearch && matchesRole;
    });
  }, [users, searchTerm, roleFilter]);

  const handlePageChange = useCallback((page: number) => {
    setCurrentPage(page);
  }, []);

  const handlePageSizeChange = useCallback((size: number) => {
    setCurrentPageSize(size);
    setCurrentPage(1);
  }, []);

  const handleEditClick = (user: User) => {
    setSelectedUser(user);
    setEditedUser({ ...user });
    setShowEditModal(true);
  };

  const handleEditConfirm = async (e: React.FormEvent) => {
    e?.preventDefault();

    if (selectedUser && editedUser) {
      try {
        await utilisateurHttpService.modifier(selectedUser.id, editedUser);
        showSuccess(MESSAGES.SUCCESS.USER_UPDATED);
        fetchUsers(currentPage, currentPageSize);
        setShowEditModal(false);
        setSelectedUser(null);
        setEditedUser({});
      } catch (error: unknown) {
        let message: string = MESSAGES.ERROR.UPDATING_USER;
        if (error instanceof HttpError) {
          message = error?.message
        }
        showError(message);
      }
    }
  };

  const handleEditCancel = () => {
    setShowEditModal(false);
    setSelectedUser(null);
    setEditedUser({});
  };

  const handlePasswordClick = (user: User) => {
    setSelectedUser(user);
    setNewPassword('');
    setShowPasswordModal(true);
  };

  const handlePasswordConfirm = async () => {
    if (selectedUser && newPassword) {
      try {
        await utilisateurHttpService.changerMot2Passe(selectedUser.id, newPassword);
        showSuccess('Mot de passe modifié avec succès');
        setShowPasswordModal(false);
        setSelectedUser(null);
        setNewPassword('');
      } catch (error: unknown) {
        if (error instanceof HttpError) {
          showError(error.message);
        } else {
          showError('Erreur lors de la modification du mot de passe');
        }
      }
    }
  };

  const handlePasswordCancel = () => {
    setShowPasswordModal(false);
    setSelectedUser(null);
    setNewPassword('');
  };

  const rolesTemplate = useCallback((user: User) => (
    <Tag value={user.role} className="bg-gray-200 text-gray-800 border-none" />
  ), []);


  const actionsTemplate = (user: User) => {
    const actions: ActionList = [
      {
        type: 'edit',
        onClick: (item: unknown) => handleEditClick(item as User),
      },
      {
        type: 'delete',
        onClick: (item: unknown) => handleDeleteClick(item as User),
      }
    ];

    return (
      <ActionsButtons item={user} actions={actions}>
        <button
          onClick={() => handlePasswordClick(user)}
          className="text-purple-600 hover:text-purple-900 dark:text-purple-400 dark:hover:text-purple-300 transition-colors duration-200"
          title="Réinitialiser le mot de passe"
        >
          <FontAwesomeIcon icon={faKey} className="w-4 h-4" />
        </button>
      </ActionsButtons>
    );
  };

  const handleDeleteClick = (user: User) => {
    setSelectedUser(user);
    setShowDeleteModal(true);
  };

  const handleDeleteConfirm = async () => {
    if (selectedUser) {
      try {
        await utilisateurHttpService.supprimer(selectedUser.id);
        showSuccess(MESSAGES.SUCCESS.USER_DELETED);
        fetchUsers(currentPage, currentPageSize);
        setShowDeleteModal(false);
        setSelectedUser(null);
      } catch (error: unknown) {
        if (error instanceof HttpError) {
          showError(error.message);
        } else {
          showError(MESSAGES.ERROR.DELETING_USER);
        }
      }
    }
  };

  const handleDeleteCancel = () => {
    setShowDeleteModal(false);
    setSelectedUser(null);
  };

  const handleRegisterChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setRegisterForm(prev => ({ ...prev, [name]: value }));
    setRegisterErrors({});
    setRegisterGlobalError('');
  };

  const validateRegisterForm = (): boolean => {
    const newErrors: Partial<typeof registerForm> = {};

    if (!registerForm.username.trim()) {
      newErrors.username = 'Le nom d\'utilisateur est requis';
    }

    if (!registerForm.password) {
      newErrors.password = 'Le mot de passe est requis';
    } else if (registerForm.password.length < 6) {
      newErrors.password = 'Le mot de passe doit contenir au moins 6 caractères';
    }

    if (!registerForm.confirmPassword) {
      newErrors.confirmPassword = 'La confirmation du mot de passe est requise';
    } else if (registerForm.password !== registerForm.confirmPassword) {
      newErrors.confirmPassword = 'Les mots de passe ne correspondent pas';
    }

    setRegisterErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleRegisterSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!validateRegisterForm()) {
      return;
    }

    setRegisterLoading(true);
    setRegisterGlobalError('');

    try {
      await register({ username: registerForm.username, password: registerForm.password });
      showSuccess('Utilisateur créé avec succès');
      setShowRegisterModal(false);
      setRegisterForm({ username: '', password: '', confirmPassword: '' });
      setRegisterErrors({});
      await fetchUsers(currentPage, currentPageSize);
    } catch (error: unknown) {
      if (error instanceof HttpError) {
        setRegisterGlobalError(error.message);
      } else {
        setRegisterGlobalError('Une erreur est survenue lors de la création de l\'utilisateur');
      }
    } finally {
      setRegisterLoading(false);
    }
  };

  useEffect(() => {
    fetchUsers(currentPage, currentPageSize);
  }, [currentPage, currentPageSize, fetchUsers]);

  useEffect(() => {
    fetchRoles()
  }, [fetchRoles]);

  if (error && !isLoading) {
    return (
      <div className="p-6">
        <div className="bg-red-50 border border-red-200 rounded-md p-4">
          <div className="flex">
            <div className="flex-shrink-0">
              <i className="pi pi-exclamation-triangle text-red-400" />
            </div>
            <div className="ml-3">
              <h3 className="text-sm font-medium text-red-800">Erreur</h3>
              <p className="mt-1 text-sm text-red-700">{error}</p>
              <div className="mt-4">
                <Button
                  label="Réessayer"
                  icon="pi pi-refresh"
                  onClick={() => fetchUsers(currentPage, currentPageSize)}
                  className="p-button-sm p-button-outlined p-button-danger"
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className='space-y-2'>
      <div className="flex flex-col gap-3">
        <div className="flex justify-between items-center app-form-fieldset py-2">
          <h1 className="text-2xl font-bold text-gray-900 dark:text-white mb-0">
            Liste des utilisateurs
          </h1>
          <button
            onClick={() => setShowRegisterModal(true)}
            className="app-form-button-primary"
          >
            Nouvel utilisateur
          </button>
        </div>

        <div
          className="bg-white dark:bg-gray-800 rounded-lg shadow-sm border border-gray-200 dark:border-gray-700 p-4">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <InputTextField
              id="search"
              name="search"
              label="Rechercher"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              placeholder="Rechercher par nom ou email..."
            />

            <DropdownField
              id="role"
              name="role"
              label="Rôle"
              options={roleOptions}
              value={roleFilter}
              onChange={(e) => setRoleFilter(e.target.value)}
              placeholder="Filtrer par rôle"
            />
          </div>
        </div>

        <div
          className="bg-white dark:bg-gray-800 rounded-lg shadow-sm border border-gray-200 dark:border-gray-700 overflow-hidden">
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200 dark:divide-gray-700">
              <thead className="bg-gray-50 dark:bg-gray-800">
                <tr>
                  <th scope="col"
                    className="px-2 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                    Nom
                  </th>
                  <th scope="col"
                    className="px-2 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                    Email
                  </th>
                  <th scope="col"
                    className="px-2 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                    Rôle
                  </th>
                  <th scope="col"
                    className="w-10 px-2 py-3 text-center text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                    Actions
                  </th>
                </tr>
              </thead>
              <tbody
                className="bg-white dark:bg-gray-900 divide-y divide-gray-200 dark:divide-gray-800">
                {filteredUsers.map((user) => (
                  <tr key={user.id} className="hover:bg-gray-50 dark:hover:bg-gray-800/50">
                    <td className="px-2 py-2 whitespace-nowrap text-sm font-medium text-gray-900 dark:text-white">
                      {user.username}
                    </td>
                    <td className="px-2 py-2 whitespace-nowrap text-sm text-gray-500 dark:text-gray-400">
                      {user.email}
                    </td>
                    <td className="px-2 py-2 whitespace-nowrap text-sm text-gray-500 dark:text-gray-400">
                      {rolesTemplate(user)}
                    </td>
                    <td className="px-2 whitespace-nowrap text-center text-sm font-medium">
                      {actionsTemplate(user)}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <Pagination
            currentPage={currentPage}
            currentPageSize={currentPageSize}
            totalRecords={totalRecords}
            onPageChange={handlePageChange}
            onPageSizeChange={handlePageSizeChange}
            displayedItemsCount={filteredUsers.length}
            defautPageSize={DEFAULT_PAGE_SIZE}
          />
        </div>
      </div>

      {
        showEditModal && selectedUser && (
          <div
            className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
            <div className="bg-white dark:bg-gray-800 shadow-xl max-w-md w-full mx-4"
              style={{ borderRadius: 'var(--border-radius)' }}>
              <div className="p-4 border-b border-gray-200 dark:border-gray-700">
                <h3 className="text-lg font-semibold text-gray-900 dark:text-white">
                  Modifier l'utilisateur : {selectedUser.username}
                </h3>
              </div>
              <div className="px-4 py-5 space-y-4">
                <form onSubmit={handleEditConfirm} className="space-y-4">
                  <InputTextField
                    id="username"
                    name="username"
                    label="Nom utilisateur"
                    value={editedUser.username ?? ''}
                    onChange={(e) => setEditedUser({
                      ...editedUser,
                      username: e.target.value
                    })}
                    required
                  />

                  <InputTextField
                    id="email"
                    name="email"
                    label="Email"
                    value={editedUser.email ?? ''}
                    onChange={(e) => setEditedUser({ ...editedUser, email: e.target.value })}
                  />

                  <DropdownField
                    id="role"
                    name="role"
                    label="Rôle"
                    options={roles}
                    value={editedUser.role ?? ''}
                    onChange={(e) => setEditedUser({ ...editedUser, role: e.target.value })}
                    required
                    disabled={isLoading}
                    placeholder="Sélectionnez un rôle"
                  />
                  <div className="flex justify-end space-x-3 mt-6">
                    <button
                      onClick={handleEditCancel}
                      className="app-form-button"
                    >
                      Annuler
                    </button>
                    <button
                      onClick={handleEditConfirm}
                      className="app-form-button-primary"
                    >
                      Enregistrer
                    </button>
                  </div>
                </form>
              </div>
            </div>
          </div>
        )
      }

      {
        showPasswordModal && selectedUser && (
          <div
            className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
            <div className="bg-white dark:bg-gray-800 rounded-lg p-6 max-w-md w-full mx-4">
              <h3 className="text-lg font-medium text-gray-900 dark:text-white mb-4">
                Modifier le mot de passe de : {selectedUser.username}
              </h3>
              <div className="space-y-4">
                <div className="app-form-group">
                  <PasswordField
                    id="password"
                    name="password"
                    label="Nouveau mot de passe"
                    value={newPassword}
                    onChange={(e) => setNewPassword(e.target.value)}
                    error={registerErrors.password}
                    placeholder="Saisissez le nouveau mot de passe"
                    required
                  />
                </div>
              </div>
              <div className="flex justify-end space-x-3 mt-6">
                <button
                  onClick={handlePasswordCancel}
                  className="app-form-button"
                >
                  Annuler
                </button>
                <button
                  onClick={handlePasswordConfirm}
                  className="app-form-button-primary"
                >
                  Enregistrer
                </button>
              </div>
            </div>
          </div>
        )
      }

      {
        showDeleteModal && selectedUser && (
          <div
            className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
            <div className="bg-white dark:bg-gray-800 rounded-lg p-6 max-w-md w-full mx-4">
              <h3 className="text-lg font-medium text-gray-900 dark:text-white mb-4">
                Confirmer la suppression de : {selectedUser.username}
              </h3>
              <p className="text-sm text-gray-500 dark:text-gray-400 mb-6">
                Êtes-vous sûr de vouloir supprimer cet utilisateur ? Cette action est
                irréversible.
              </p>
              <div className="flex justify-end space-x-3">
                <button
                  onClick={handleDeleteCancel}
                  className="app-form-button"
                >
                  Annuler
                </button>
                <button
                  onClick={handleDeleteConfirm}
                  className="app-form-button-primary bg-red-600 hover:bg-red-700"
                >
                  Supprimer
                </button>
              </div>
            </div>
          </div>
        )
      }

      {
        showRegisterModal && (
          <div
            className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
            <div className="bg-white dark:bg-gray-800 shadow-xl max-w-md w-full mx-4"
              style={{ borderRadius: 'var(--border-radius)' }}>
              <div className="p-4 border-b border-gray-200 dark:border-gray-700">
                <h3 className="text-lg font-semibold text-gray-900 dark:text-white">
                  Ajouter un nouvel utilisateur
                </h3>
              </div>
              <div className="px-4 py-5">
                {registerGlobalError && (
                  <div className="mb-4">
                    <Message className="!w-full" severity="error" text={registerGlobalError} />
                  </div>
                )}
                <form onSubmit={handleRegisterSubmit} className="space-y-4">
                  <InputField
                    id="username"
                    name="username"
                    label="Nom utilisateur"
                    value={registerForm.username}
                    onChange={handleRegisterChange}
                    required
                    error={registerErrors.username}
                    placeholder="Saisissez l'identifiant de l'utilisateur"
                  />

                  <PasswordField
                    id="password"
                    name="password"
                    label="Mot de passe"
                    value={registerForm.password}
                    onChange={handleRegisterChange}
                    required
                    error={registerErrors.password}
                    placeholder="Saisissez le mot de passe"
                  />

                  <PasswordField
                    id="confirmPassword"
                    name="confirmPassword"
                    label="Confirmer le mot de passe"
                    value={registerForm.confirmPassword}
                    onChange={handleRegisterChange}
                    required
                    error={registerErrors.confirmPassword}
                    placeholder="Saisissez à nouveau le mot de passe"
                  />

                  <div className="flex justify-end space-x-3 pt-4">
                    <button
                      type="button"
                      onClick={() => {
                        setShowRegisterModal(false);
                        setRegisterForm({ username: '', password: '', confirmPassword: '' });
                        setRegisterErrors({});
                        setRegisterGlobalError('');
                      }}
                      className="px-4 py-2 border border-gray-300 dark:border-gray-600 text-sm font-medium text-gray-700 dark:text-gray-300 hover:bg-gray-50 dark:hover:bg-gray-700 transition-colors duration-200"
                      style={{ borderRadius: 'var(--border-radius)' }}
                    >
                      Annuler
                    </button>
                    <button
                      type="submit"
                      disabled={registerLoading}
                      className="px-4 py-2 bg-indigo-600 text-white text-sm font-medium hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed transition-colors duration-200"
                      style={{ borderRadius: 'var(--border-radius)' }}
                    >
                      {registerLoading ? 'Ajout en cours...' : 'Ajouter'}
                    </button>
                  </div>
                </form>
              </div>
            </div>
          </div>
        )
      }
    </div>
  );
};

export default ListerUtilisateurs;