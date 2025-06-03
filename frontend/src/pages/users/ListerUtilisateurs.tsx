import { API_ENDPOINTS } from '@/config/api';
import { useToast } from '@contexts/ToastContext';
import { faEye, faEyeSlash } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { http } from '@services/http';
import { Button } from 'primereact/button';
import { Column } from 'primereact/column';
import { DataTable } from 'primereact/datatable';
import { Dialog } from 'primereact/dialog';
import { Dropdown } from 'primereact/dropdown';
import { Tag } from 'primereact/tag';
import { useEffect, useState } from 'react';

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
  role: string;
  password?: string;
}

interface Role {
  name: string;
  label: string;
}

interface ApiResponse {
  data: User[];
  metadata: {
    number: number;
    size: number;
    totalElements: number;
    totalPages: number;
    first: boolean;
    last: boolean;
    offset: number;
    remainingElements: number;
    remainingPages: number;
  };
  links: {
    current: string;
    first: string;
    last: string;
    next: string | null;
    prev: string | null;
  };
  size: number;
}

const ListerUtilisateurs = () => {
  const [users, setUsers] = useState<User[]>([]);
  const [totalRecords, setTotalRecords] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const { success: showSuccess, error: showError } = useToast();
  const [deleteDialogVisible, setDeleteDialogVisible] = useState(false);
  const [selectedUser, setSelectedUser] = useState<User | null>(null);
  const [editDialogVisible, setEditDialogVisible] = useState(false);
  const [passwordDialogVisible, setPasswordDialogVisible] = useState(false);
  const [editForm, setEditForm] = useState<EditUserForm>({
    username: '',
    role: '',
    password: ''
  });
  const [showPassword, setShowPassword] = useState(false);
  const [newPassword, setNewPassword] = useState('');
  const [roles, setRoles] = useState<Role[]>([]);


  useEffect(() => {
    fetchUsers();
    fetchRoles();
  }, []);

  const fetchRoles = async () => {
    try {
      const response = await http.get<{ status: string, data: string[] }>(API_ENDPOINTS.roles.list);
      if (response.status === 'success') {
        // Transformer les rôles pour le format du Dropdown
        const formattedRoles = response.data.map(role => ({
          name: role,
          label: role
        }));
        setRoles(formattedRoles);
      }
    } catch (error) {
      showError('Erreur lors du chargement des rôles');
    }
  };

  const fetchUsers = async (page: number = 1, size: number = pageSize) => {
    try {
      setIsLoading(true);
      const response = await http.get<ApiResponse>(API_ENDPOINTS.users.list + `?page=${page}&size=${size}`);
      setUsers(response.data);
      setTotalRecords(response.metadata.totalElements);
      setCurrentPage(response.metadata.number);
    } catch (error: any) {
      setError('Une erreur est survenue lors du chargement des utilisateurs');
      showError('Erreur de chargement des utilisateurs');
    } finally {
      setIsLoading(false);
    }
  };

  const onPage = (event: { page?: number; first: number; rows: number }) => {
    const newPage = (event.page ?? 0) + 1;
    setCurrentPage(newPage);
    fetchUsers(newPage, event.rows);
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
        role: editForm.role
      };


      if (editForm.password) {
        updateData.password = editForm.password;
      }

      await http.put<{ status: string }>(API_ENDPOINTS.users.update, {
        id: selectedUser.id,
        ...updateData
      });
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
      await http.put<{ status: string }>(API_ENDPOINTS.users.resetPassword, {
        id: selectedUser.id,
        password: newPassword
      });
      showSuccess('Mot de passe réinitialisé avec succès');
      setPasswordDialogVisible(false);
      setNewPassword('');
    } catch (error) {
      showError('Erreur lors de la réinitialisation du mot de passe');
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
    <div className="p-6">
      <DataTable
        value={users}
        lazy
        paginator
        first={(currentPage - 1) * pageSize}
        rows={pageSize}
        totalRecords={totalRecords}
        onPage={onPage}
        loading={isLoading}
        className="p-datatable-lg shadow-lg rounded-lg bg-white dark:bg-gray-800"
        emptyMessage="Aucun utilisateur trouvé"
        currentPageReportTemplate="Affichage de {first} à {last} sur {totalRecords} utilisateurs"
        paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
        rowsPerPageOptions={[5, 10, 20, 50]}
      >
        <Column field="username" header="Nom d'utilisateur" body={userTemplate} />
        <Column field="createdAt" header="Date de création" body={dateTemplate} />
        <Column field="role" header="Rôle" body={rolesTemplate} />
        <Column field="isActive" header="Statut" body={statusTemplate} />
        <Column body={actionsTemplate} header="Actions" style={{ width: '200px' }} />
      </DataTable>

      {/* Modal de modification */}
      <Dialog
        visible={editDialogVisible}
        onHide={() => setEditDialogVisible(false)}
        header="Modifier l'utilisateur"
        className="w-full max-w-lg rounded-xl overflow-hidden bg-white dark:bg-gray-800 p-0"
        modal
        footer={
          <div className="flex justify-end space-x-3 px-6 py-4 bg-gray-50 dark:bg-gray-700">
            <button
              onClick={() => setEditDialogVisible(false)}
              className="px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-lg hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 dark:bg-gray-600 dark:text-gray-200 dark:border-gray-500 dark:hover:bg-gray-500"
            >
              Annuler
            </button>
            <button
              onClick={handleEdit}
              className="px-4 py-2 text-sm font-medium text-white bg-indigo-600 border border-transparent rounded-lg hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
            >
              Enregistrer
            </button>
          </div>
        }
      >
        <div className="p-6 space-y-6">
          <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                Nom d'utilisateur
              </label>
              <input
                type="text"
                value={editForm.username}
                onChange={(e) => setEditForm({ ...editForm, username: e.target.value })}
                className="block w-full px-4 py-3 rounded-lg border border-gray-300 dark:border-gray-600 focus:ring-2 focus:ring-indigo-500 focus:border-transparent bg-white dark:bg-gray-800 text-gray-900 dark:text-white text-sm transition-all duration-200 ease-in-out transform hover:shadow-sm focus:shadow-md outline-none"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                Rôle
              </label>
              <Dropdown
                value={editForm.role}
                options={roles}
                onChange={(e) => setEditForm({ ...editForm, role: e.value })}
                optionLabel="label"
                optionValue="name"
                className="w-full"
              />
            </div>
          </div>
        </div>
      </Dialog>

      {/* Modal de réinitialisation du mot de passe */}
      <Dialog
        visible={passwordDialogVisible}
        onHide={() => setPasswordDialogVisible(false)}
        header="Réinitialiser le mot de passe"
        className="w-full max-w-lg rounded-xl overflow-hidden bg-white dark:bg-gray-800 p-0"
        modal
        footer={
          <div className="flex justify-end space-x-3 px-6 py-4 bg-gray-50 dark:bg-gray-700">
            <button
              onClick={() => setPasswordDialogVisible(false)}
              className="px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-lg hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 dark:bg-gray-600 dark:text-gray-200 dark:border-gray-500 dark:hover:bg-gray-500"
            >
              Annuler
            </button>
            <button
              onClick={handlePasswordReset}
              className="px-4 py-2 text-sm font-medium text-white bg-indigo-600 border border-transparent rounded-lg hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
            >
              Réinitialiser
            </button>
          </div>
        }
      >
        <div className="p-6 space-y-6">
          <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                Nouveau mot de passe
              </label>
              <div className="relative">
                <input
                  type={showPassword ? "text" : "password"}
                  value={newPassword}
                  onChange={(e) => setNewPassword(e.target.value)}
                  className="block w-full px-4 py-3 rounded-lg border border-gray-300 dark:border-gray-600 focus:ring-2 focus:ring-indigo-500 focus:border-transparent bg-white dark:bg-gray-800 text-gray-900 dark:text-white text-sm transition-all duration-200 ease-in-out transform hover:shadow-sm focus:shadow-md outline-none pr-12"
                />
                <button
                  type="button"
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 dark:hover:text-gray-300 transition-colors duration-200 w-10 h-10 flex items-center justify-center bg-transparent border-none p-0"
                  onClick={() => setShowPassword(!showPassword)}
                  tabIndex={-1}
                >
                  <FontAwesomeIcon
                    icon={showPassword ? faEyeSlash : faEye}
                    className="h-5 w-5"
                  />
                </button>
              </div>
            </div>
          </div>
        </div>
      </Dialog>

      {/* Modal de confirmation de suppression */}
      <Dialog
        visible={deleteDialogVisible}
        onHide={() => setDeleteDialogVisible(false)}
        header="Confirmer la suppression"
        className="w-full max-w-lg rounded-xl overflow-hidden bg-white dark:bg-gray-800 p-0"
        modal
        footer={
          <div className="flex justify-end space-x-3 px-6 py-4 bg-gray-50 dark:bg-gray-700">
            <button
              onClick={() => setDeleteDialogVisible(false)}
              className="px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-lg hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 dark:bg-gray-600 dark:text-gray-200 dark:border-gray-500 dark:hover:bg-gray-500"
            >
              Annuler
            </button>
            <button
              onClick={() => selectedUser && handleDelete(selectedUser.id)}
              className="px-4 py-2 text-sm font-medium text-white bg-red-600 border border-transparent rounded-lg hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500"
            >
              Supprimer
            </button>
          </div>
        }
      >
        <div className="p-6">
          <p className="text-gray-700 dark:text-gray-300">
            Êtes-vous sûr de vouloir supprimer cet utilisateur ? Cette action est irréversible.
          </p>
        </div>
      </Dialog>
    </div>
  );
};

export default ListerUtilisateurs; 