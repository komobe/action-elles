import { useState, useEffect } from 'react';
import { http } from '@services/http';
import { API_ENDPOINTS } from '@/config/api';
import { useToast } from '@contexts/ToastContext';
import { Dialog } from 'primereact/dialog';
import { Button } from 'primereact/button';
import { Dropdown } from 'primereact/dropdown';
import { DataTable, DataTablePageEvent, DataTableRowClassNameOptions } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { Tag } from 'primereact/tag';
import { InputText } from 'primereact/inputtext';
import { Password } from 'primereact/password';
import { FaEye, FaEyeSlash } from 'react-icons/fa';

interface User {
  id: string;
  username: string;
  email: string;
  role: string;
  createdAt: string;
  isActive: boolean;
}

interface EditUserForm {
  username: string;
  email: string;
  role: string;
  password?: string;
}

interface Role {
  id: string;
  name: string;
  label: string;
}

const ListerUtilisateurs = () => {
  const [users, setUsers] = useState<User[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const { success: showSuccess, error: showError } = useToast();
  const [deleteDialogVisible, setDeleteDialogVisible] = useState(false);
  const [selectedUser, setSelectedUser] = useState<User | null>(null);
  const [editDialogVisible, setEditDialogVisible] = useState(false);
  const [passwordDialogVisible, setPasswordDialogVisible] = useState(false);
  const [editForm, setEditForm] = useState<EditUserForm>({
    username: '',
    email: '',
    role: '',
    password: ''
  });
  const [showPassword, setShowPassword] = useState(false);
  const [newPassword, setNewPassword] = useState('');
  const [roles, setRoles] = useState<Role[]>([]);

  const availableRoles = [
    { label: 'Administrateur', value: 'ADMIN' },
    { label: 'Utilisateur', value: 'USER' },
    { label: 'Manager', value: 'MANAGER' }
  ];

  useEffect(() => {
    fetchUsers();
    fetchRoles();
  }, []);

  const fetchRoles = async () => {
    try {
      const response = await http.get<{ status: string, data: Role[] }>(API_ENDPOINTS.roles.list);
      if (response.status === 'success') {
        setRoles(response.data);
      }
    } catch (error) {
      showError('Erreur lors du chargement des rôles');
    }
  };

  const fetchUsers = async () => {
    try {
      setIsLoading(true);
      const response = await http.get<{ status: string, data: User[] }>(API_ENDPOINTS.users.list);
      if (response.status === 'success') {
        setUsers(response.data);
      }
    } catch (error: any) {
      setError('Une erreur est survenue lors du chargement des utilisateurs');
      showError('Erreur de chargement des utilisateurs');
    } finally {
      setIsLoading(false);
    }
  };

  const handleDelete = async (userId: string) => {
    try {
      await http.delete(API_ENDPOINTS.users.delete(userId));
      showSuccess('Utilisateur supprimé avec succès');
      setUsers(users.filter(user => user.id !== userId));
      setDeleteDialogVisible(false);
    } catch (error) {
      showError('Erreur lors de la suppression de l\'utilisateur');
    }
  };

  const handleEdit = async () => {
    if (!selectedUser) return;

    try {
      const updateData: Partial<EditUserForm> = {
        username: editForm.username,
        email: editForm.email,
        role: editForm.role
      };

      if (editForm.password) {
        updateData.password = editForm.password;
      }

      await http.put(API_ENDPOINTS.users.update(selectedUser.id), updateData);
      showSuccess('Utilisateur mis à jour avec succès');

      setUsers(users.map(user =>
        user.id === selectedUser.id
          ? { ...user, ...updateData }
          : user
      ));
      setEditDialogVisible(false);
    } catch (error) {
      showError('Erreur lors de la mise à jour de l\'utilisateur');
    }
  };

  const handlePasswordReset = async () => {
    if (!selectedUser) return;

    try {
      await http.put(API_ENDPOINTS.users.update(selectedUser.id), {
        password: newPassword
      });
      showSuccess('Mot de passe réinitialisé avec succès');
      setPasswordDialogVisible(false);
      setNewPassword('');
    } catch (error) {
      showError('Erreur lors de la réinitialisation du mot de passe');
    }
  };

  const toggleUserStatus = async (userId: string, currentStatus: boolean) => {
    try {
      await http.put(API_ENDPOINTS.users.update(userId), {
        isActive: !currentStatus
      });
      setUsers(users.map(user =>
        user.id === userId
          ? { ...user, isActive: !currentStatus }
          : user
      ));
      showSuccess(`Utilisateur ${currentStatus ? 'désactivé' : 'activé'} avec succès`);
    } catch (error) {
      showError(`Erreur lors de la ${currentStatus ? 'désactivation' : 'activation'} de l'utilisateur`);
    }
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('fr-FR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  const openEditDialog = (user: User) => {
    setSelectedUser(user);
    setEditForm({
      username: user.username,
      email: user.email,
      role: user.role,
      password: ''
    });
    setEditDialogVisible(true);
  };

  const openDeleteDialog = (user: User) => {
    setSelectedUser(user);
    setDeleteDialogVisible(true);
  };

  const openPasswordDialog = (user: User) => {
    setSelectedUser(user);
    setNewPassword('');
    setPasswordDialogVisible(true);
  };

  const userTemplate = (user: User) => {
    return (
      <div className="font-medium">{user.username}</div>
    );
  };

  const dateTemplate = (user: User) => {
    return user.createdAt ? formatDate(user.createdAt) : 'N/A';
  };

  const rolesTemplate = (user: User) => {
    return <div className="font-bold">{user.role}</div>;
  };

  const statusTemplate = (user: User) => {
    return (
      <Tag
        value={user.isActive ? 'Actif' : 'Inactif'}
        severity={user.isActive ? 'success' : 'danger'}
      />
    );
  };

  const actionsTemplate = (user: User) => {
    return (
      <div className="flex gap-6 justify-center">
        <i
          className="pi pi-pencil cursor-pointer text-gray-600"
          onClick={() => openEditDialog(user)}
        />
        <i
          className="pi pi-key cursor-pointer text-gray-600"
          onClick={() => openPasswordDialog(user)}
        />
        <i
          className="pi pi-trash cursor-pointer text-gray-600"
          onClick={() => openDeleteDialog(user)}
        />
      </div>
    );
  };

  return (
    <div className="bg-white dark:bg-gray-800 shadow-lg rounded-lg overflow-hidden">
      <div className="p-6">
        <h1 className="text-2xl font-bold text-gray-900 dark:text-white mb-6">
          Gestion des Utilisateurs
        </h1>

        {error && (
          <div className="mb-4 bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 p-4 rounded-lg">
            <p className="text-sm text-red-600 dark:text-red-400">{error}</p>
          </div>
        )}

        <DataTable
          value={users}
          loading={isLoading}
          paginator
          rowsPerPageOptions={[5, 10, 25, 50]}
          rows={10}
          tableStyle={{ minWidth: '100%' }}
          emptyMessage="Aucun utilisateur trouvé"
          scrollable={true}
        >
          <Column
            field="username"
            header="Utilisateur"
            body={userTemplate}
            className="py-4"
            headerClassName="!text-gray-500 dark:!text-gray-300 !font-medium !uppercase !text-sm"
          />
          <Column
            field="email"
            header="Email"
            className="py-4"
            headerClassName="!text-gray-500 dark:!text-gray-300 !font-medium !uppercase !text-sm"
          />
          <Column
            field="role"
            header="Rôle"
            body={rolesTemplate}
            className="py-4"
            headerClassName="!text-gray-500 dark:!text-gray-300 !font-medium !uppercase !text-sm"
          />
          <Column
            field="createdAt"
            header="Date de création"
            body={dateTemplate}
            className="py-4"
            headerClassName="!text-gray-500 dark:!text-gray-300 !font-medium !uppercase !text-sm"
          />
          <Column
            field="isActive"
            header="Statut"
            body={statusTemplate}
            className="py-4"
            headerClassName="!text-gray-500 dark:!text-gray-300 !font-medium !uppercase !text-sm"
          />
          <Column
            body={actionsTemplate}
            header="Actions"
            className="py-4"
            headerClassName="!text-gray-500 dark:!text-gray-300 !font-medium !uppercase !text-sm"
            style={{ width: '180px' }}
          />
        </DataTable>
      </div>

      {/* Dialog de confirmation de suppression */}
      <Dialog
        visible={deleteDialogVisible}
        onHide={() => setDeleteDialogVisible(false)}
        header="Confirmer la suppression"
        className="bg-white dark:bg-gray-800"
        headerClassName="text-gray-900 dark:text-white border-b border-gray-200 dark:border-gray-700"
        contentClassName="text-gray-600 dark:text-gray-300"
        closeIcon="pi pi-times"
        closable
        closeOnEscape
        dismissableMask
        footer={
          <div className="flex gap-2 justify-end">
            <Button
              label="Annuler"
              icon="pi pi-times"
              onClick={() => setDeleteDialogVisible(false)}
              className="p-button-text"
              severity="secondary"
            />
            <Button
              label="Supprimer"
              icon="pi pi-trash"
              onClick={() => selectedUser && handleDelete(selectedUser.id)}
              severity="danger"
            />
          </div>
        }
      >
        <p>Êtes-vous sûr de vouloir supprimer cet utilisateur ?</p>
      </Dialog>

      {/* Dialog de modification */}
      <Dialog
        visible={editDialogVisible}
        onHide={() => setEditDialogVisible(false)}
        header={
          <div className="w-full">
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-3xl font-extrabold text-gray-900 dark:text-white">
                Modifier l'utilisateur
              </h2>
              <button
                onClick={() => {
                  setEditDialogVisible(false);
                  openPasswordDialog(selectedUser!);
                }}
                className="inline-flex items-center px-4 py-2 border-2 border-indigo-600 text-sm font-medium rounded-full text-indigo-600 bg-white hover:bg-indigo-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-colors duration-200"
              >
                <i className="pi pi-key mr-2 text-lg" />
                Réinitialiser le mot de passe
              </button>
            </div>
            <p className="text-base text-gray-600 dark:text-gray-400">
              Modifiez les informations de l'utilisateur sélectionné
            </p>
          </div>
        }
        className="bg-white dark:bg-gray-800"
        contentClassName="!p-0"
        closeIcon="pi pi-times"
        closable
        closeOnEscape
        dismissableMask
        style={{ width: '800px', maxWidth: '90vw' }}
        modal
        footer={
          <div className="px-8 py-4 bg-gray-50 dark:bg-gray-700/50">
            <div className="flex justify-between items-center">
              <button
                onClick={() => setEditDialogVisible(false)}
                className="px-4 py-2 text-sm font-medium text-gray-700 dark:text-gray-300 hover:text-gray-500 dark:hover:text-gray-400 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-500 transition-colors duration-200"
              >
                Annuler
              </button>
              <button
                onClick={handleEdit}
                className="px-6 py-2 border border-transparent text-base font-medium rounded-full text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed transition-colors duration-200"
                disabled={!editForm.username || !editForm.email || !editForm.role}
              >
                Enregistrer les modifications
              </button>
            </div>
          </div>
        }
      >
        <form className="p-8 space-y-8">
          <div className="grid grid-cols-2 gap-8">
            <div className="space-y-6">
              <div>
                <label htmlFor="username" className="block text-base font-medium text-gray-700 dark:text-gray-300 mb-2">
                  Nom d'utilisateur
                </label>
                <div className="relative">
                  <span className="absolute inset-y-0 left-0 pl-3 flex items-center text-gray-400">
                    <i className="pi pi-user" />
                  </span>
                  <input
                    id="username"
                    type="text"
                    value={editForm.username}
                    onChange={(e) => setEditForm({ ...editForm, username: e.target.value })}
                    className="appearance-none block w-full pl-10 px-3 py-3 border border-gray-300 dark:border-gray-600 rounded-lg placeholder-gray-400 dark:placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 bg-white dark:bg-gray-800 text-gray-900 dark:text-white text-base"
                    placeholder="Entrez le nom d'utilisateur"
                  />
                </div>
              </div>

              <div>
                <label htmlFor="email" className="block text-base font-medium text-gray-700 dark:text-gray-300 mb-2">
                  Email
                </label>
                <div className="relative">
                  <span className="absolute inset-y-0 left-0 pl-3 flex items-center text-gray-400">
                    <i className="pi pi-envelope" />
                  </span>
                  <input
                    id="email"
                    type="email"
                    value={editForm.email}
                    onChange={(e) => setEditForm({ ...editForm, email: e.target.value })}
                    className="appearance-none block w-full pl-10 px-3 py-3 border border-gray-300 dark:border-gray-600 rounded-lg placeholder-gray-400 dark:placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 bg-white dark:bg-gray-800 text-gray-900 dark:text-white text-base"
                    placeholder="Entrez l'adresse email"
                  />
                </div>
              </div>

              <div>
                <label htmlFor="role" className="block text-base font-medium text-gray-700 dark:text-gray-300 mb-2">
                  Rôle
                </label>
                <div className="relative">
                  <span className="absolute inset-y-0 left-0 pl-3 flex items-center text-gray-400 z-10">
                    <i className="pi pi-users" />
                  </span>
                  <Dropdown
                    id="role"
                    value={editForm.role}
                    onChange={(e) => setEditForm({ ...editForm, role: e.value })}
                    options={roles}
                    optionLabel="label"
                    optionValue="name"
                    placeholder="Sélectionnez un rôle"
                    className="w-full pl-10"
                  />
                </div>
              </div>
            </div>

            <div className="space-y-6">
              <div>
                <label className="block text-base font-medium text-gray-700 dark:text-gray-300 mb-2">
                  Informations complémentaires
                </label>
                <div className="bg-gray-50 dark:bg-gray-700/25 rounded-lg p-4">
                  <div className="flex items-center space-x-4 text-sm text-gray-500 dark:text-gray-400">
                    <div>
                      <i className="pi pi-circle-fill mr-2 text-xs" style={{ color: selectedUser?.isActive ? '#22c55e' : '#ef4444' }} />
                      {selectedUser?.isActive ? 'Actif' : 'Inactif'}
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </form>
      </Dialog>

      {/* Dialog de réinitialisation du mot de passe */}
      <Dialog
        visible={passwordDialogVisible}
        onHide={() => setPasswordDialogVisible(false)}
        header={
          <div>
            <h2 className="text-center text-2xl font-extrabold text-gray-900 dark:text-white">
              Réinitialiser le mot de passe
            </h2>
            <p className="mt-2 text-center text-sm text-gray-600 dark:text-gray-400">
              Définissez un nouveau mot de passe pour {selectedUser?.username}
            </p>
          </div>
        }
        className="bg-white dark:bg-gray-800"
        contentClassName="!p-0"
        closeIcon="pi pi-times"
        closable
        closeOnEscape
        dismissableMask
        style={{ width: '450px' }}
        modal
        footer={
          <div className="px-6 py-4 bg-gray-50 dark:bg-gray-700/50">
            <button
              onClick={handlePasswordReset}
              className="w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed transition-colors duration-200"
              disabled={!newPassword}
            >
              Réinitialiser le mot de passe
            </button>
          </div>
        }
      >
        <form className="p-6 space-y-6">
          <div>
            <label htmlFor="newPassword" className="block text-sm font-medium text-gray-700 dark:text-gray-300">
              Nouveau mot de passe
            </label>
            <div className="mt-1">
              <div className="relative">
                <input
                  id="newPassword"
                  type={showPassword ? "text" : "password"}
                  value={newPassword}
                  onChange={(e) => setNewPassword(e.target.value)}
                  className="appearance-none block w-full px-3 py-2 border border-gray-300 dark:border-gray-600 placeholder-gray-400 dark:placeholder-gray-500 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 bg-white dark:bg-gray-800 text-gray-900 dark:text-white sm:text-sm pr-10"
                  placeholder="Entrez le nouveau mot de passe"
                />
                <button
                  type="button"
                  className="absolute inset-y-0 right-0 pr-3 flex items-center"
                  onClick={() => setShowPassword(!showPassword)}
                >
                  {showPassword ? (
                    <FaEyeSlash className="h-5 w-5 text-gray-400 hover:text-gray-500" />
                  ) : (
                    <FaEye className="h-5 w-5 text-gray-400 hover:text-gray-500" />
                  )}
                </button>
              </div>
            </div>
          </div>
        </form>
      </Dialog>
    </div>
  );
};

export default ListerUtilisateurs; 