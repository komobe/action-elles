import {LabeledDropdown} from '@/components/form/LabeledDropdown';
import LabeledInput from '@/components/form/LabeledInput';
import LabeledPassword from '@/components/form/LabeledPassword';
import {useToast} from '@contexts/ToastContext';
import {Button} from 'primereact/button';
import {Column} from 'primereact/column';
import {DataTable} from 'primereact/datatable';
import {Dialog} from 'primereact/dialog';
import {Tag} from 'primereact/tag';
import {useCallback, useEffect, useMemo, useState} from 'react';
import {User, utilisateurHttpService} from '@/services/utilisateur.http-service';
import {Role, roleHttpService} from '@/services/role.http-service';
import {isPaginatedResponse} from '@/services/http/helpers';
import {formatDateFr} from '@/utils/dateUtils';

interface EditUserForm {
  username: string;
  role: string;
  password?: string;
}

// Constants
const DEFAULT_PAGE_SIZE = 10;
const PAGE_SIZE_OPTIONS = [5, 10, 20, 50];

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

const ListerUtilisateurs = () => {
  // State Management
  const [users, setUsers] = useState<User[]>([]);
  const [roles, setRoles] = useState<Role[]>([]);
  const [totalRecords, setTotalRecords] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  const [pageSize, setPageSize] = useState(DEFAULT_PAGE_SIZE);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Dialog States
  const [deleteDialogVisible, setDeleteDialogVisible] = useState(false);
  const [editDialogVisible, setEditDialogVisible] = useState(false);
  const [passwordDialogVisible, setPasswordDialogVisible] = useState(false);
  const [selectedUser, setSelectedUser] = useState<User | null>(null);

  // Form States
  const [editForm, setEditForm] = useState<EditUserForm>({
    username: '',
    role: '',
    password: ''
  });
  const [newPassword, setNewPassword] = useState('');

  // Hooks
  const { success: showSuccess, error: showError } = useToast();

  // Memoized Values
  const isFormValid = useMemo(() => {
    return editForm.username.trim() !== '' && editForm.role !== '';
  }, [editForm.username, editForm.role]);

  const isPasswordValid = useMemo(() => {
    return newPassword.trim().length >= 6; // Minimum password length
  }, [newPassword]);

  // API Calls
  const fetchRoles = useCallback(async () => {
    try {
      const response = await roleHttpService.lister();
      setRoles(response || []);
    } catch (error) {
      console.error('Error fetching roles:', error);
      showError(MESSAGES.ERROR.LOADING_ROLES);
    }
  }, [showError]);

  const fetchUsers = useCallback(async (page: number = 1, size: number = pageSize) => {
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
  }, [pageSize, showError]);

  // Event Handlers
  const handlePageChange = useCallback((event: { page?: number; first: number; rows: number }) => {
    const newPage = (event.page ?? 0) + 1;
    setCurrentPage(newPage);
    setPageSize(event.rows);
    fetchUsers(newPage, event.rows);
  }, [fetchUsers]);

  const handleDeleteUser = useCallback(async (userId: string) => {
    if (!userId) return;

    try {
      await utilisateurHttpService.supprimer(userId);
      setUsers(prevUsers => prevUsers.filter(user => user.id !== userId));
      showSuccess(MESSAGES.SUCCESS.USER_DELETED);
      setDeleteDialogVisible(false);
      setSelectedUser(null);
    } catch (error) {
      showError(MESSAGES.ERROR.DELETING_USER);
    }
  }, [showSuccess, showError]);

  const handleEditUser = useCallback(async () => {
    if (!selectedUser || !isFormValid) return;

    try {
      const updateData: Partial<EditUserForm> = {
        username: editForm.username.trim(),
        role: editForm.role
      };

      if (editForm.password?.trim()) {
        updateData.password = editForm.password.trim();
      }

      await utilisateurHttpService.modifier(selectedUser.id, updateData);

      setUsers(prevUsers =>
        prevUsers.map(user => user.id === selectedUser.id ? { ...user, ...updateData } : user)
      );

      showSuccess(MESSAGES.SUCCESS.USER_UPDATED);
      closeEditDialog();
    } catch (error) {
      showError(MESSAGES.ERROR.UPDATING_USER);
    }
  }, [selectedUser, editForm, isFormValid, showSuccess, showError]);

  const handlePasswordReset = useCallback(async () => {
    if (!selectedUser || !isPasswordValid) return;

    try {
      await utilisateurHttpService.changerMot2Passe(selectedUser.id, newPassword.trim())
      showSuccess(MESSAGES.SUCCESS.PASSWORD_RESET);
      closePasswordDialog();
    } catch (error) {
      showError(MESSAGES.ERROR.RESETTING_PASSWORD);
    }
  }, [selectedUser, newPassword, isPasswordValid, showSuccess, showError]);

  // Dialog Management
  const openEditDialog = useCallback((user: User) => {
    setSelectedUser(user);
    setEditForm({
      username: user.username,
      role: user.role,
      password: ''
    });
    setEditDialogVisible(true);
  }, []);

  const closeEditDialog = useCallback(() => {
    setEditDialogVisible(false);
    setSelectedUser(null);
    setEditForm({ username: '', role: '', password: '' });
  }, []);

  const openDeleteDialog = useCallback((user: User) => {
    setSelectedUser(user);
    setDeleteDialogVisible(true);
  }, []);

  const closeDeleteDialog = useCallback(() => {
    setDeleteDialogVisible(false);
    setSelectedUser(null);
  }, []);

  const openPasswordDialog = useCallback((user: User) => {
    setSelectedUser(user);
    setNewPassword('');
    setPasswordDialogVisible(true);
  }, []);

  const closePasswordDialog = useCallback(() => {
    setPasswordDialogVisible(false);
    setSelectedUser(null);
    setNewPassword('');
  }, []);

  // Template Functions
  const userTemplate = useCallback((user: User) => (
    <div className="font-medium">{user.username}</div>
  ), []);

  const dateTemplate = useCallback((user: User) => {
    if (!user.createdAt) return <span className="text-gray-400">N/A</span>;
    return <span>{formatDateFr(user.createdAt)}</span>;
  }, []);

  const rolesTemplate = useCallback((user: User) => (
    <Tag value={user.role} className="bg-gray-200 text-gray-800 border-none" />
  ), []);

  const statusTemplate = useCallback((user: User) => (
    <Tag
      value={user.isActive ? 'Actif' : 'Inactif'}
      severity={user.isActive ? 'success' : 'danger'}
    />
  ), []);

  const actionsTemplate = useCallback((user: User) => (
    <div className="flex gap-6 justify-center">
      <i
        className="pi pi-pencil cursor-pointer text-gray-600 hover:text-blue-600 transition-colors duration-200"
        onClick={() => openEditDialog(user)}
        title="Modifier l'utilisateur"
        role="button"
        tabIndex={0}
        onKeyDown={(e) => e.key === 'Enter' && openEditDialog(user)}
      />
      <i
        className="pi pi-key cursor-pointer text-gray-600 hover:text-yellow-600 transition-colors duration-200"
        onClick={() => openPasswordDialog(user)}
        title="Réinitialiser le mot de passe"
        role="button"
        tabIndex={0}
        onKeyDown={(e) => e.key === 'Enter' && openPasswordDialog(user)}
      />
      <i
        className="pi pi-trash cursor-pointer text-gray-600 hover:text-red-600 transition-colors duration-200"
        onClick={() => openDeleteDialog(user)}
        title="Supprimer l'utilisateur"
        role="button"
        tabIndex={0}
        onKeyDown={(e) => e.key === 'Enter' && openDeleteDialog(user)}
      />
    </div>
  ), [openEditDialog, openPasswordDialog, openDeleteDialog]);

  // Effects
  useEffect(() => {
    fetchUsers();
    fetchRoles();
  }, [fetchUsers, fetchRoles]);

  // Form Handlers
  const handleFormChange = useCallback((field: keyof EditUserForm, value: string) => {
    setEditForm(prev => ({ ...prev, [field]: value }));
  }, []);

  // Handler spécifique pour le dropdown si nécessaire
  const handleRoleChange = useCallback((value: string) => {
    handleFormChange('role', value);
  }, [handleFormChange]);

  // Dialog Footers
  const editDialogFooter = useMemo(() => (
    <div className="flex justify-end space-x-3 px-6 py-4 bg-gray-50 dark:bg-gray-700">
      <Button
        label="Annuler"
        icon="pi pi-times"
        severity='danger'
        onClick={closeEditDialog}
      />
      <Button
        label="Enregistrer"
        icon="pi pi-check"
        severity='success'
        onClick={handleEditUser}
        disabled={!isFormValid}
      />
    </div>
  ), [closeEditDialog, handleEditUser, isFormValid]);

  const passwordDialogFooter = useMemo(() => (
    <div className="flex justify-end space-x-3 px-6 py-4 bg-gray-50 dark:bg-gray-700">
      <Button
        label="Annuler"
        icon="pi pi-times"
        severity='danger'
        onClick={closePasswordDialog}
      />
      <Button
        label="Réinitialiser"
        icon="pi pi-refresh"
        severity='success'
        onClick={handlePasswordReset}
        disabled={!isPasswordValid}
      />
    </div>
  ), [closePasswordDialog, handlePasswordReset, isPasswordValid]);

  const deleteDialogFooter = useMemo(() => (
    <div className="flex justify-end space-x-3 px-6 py-4 bg-gray-50 dark:bg-gray-700">
      <Button
        label="Annuler"
        icon="pi pi-times"
        onClick={closeDeleteDialog}
        className="p-button-text p-button-secondary"
      />
      <Button
        label="Supprimer"
        icon="pi pi-trash"
        onClick={() => selectedUser && handleDeleteUser(selectedUser.id)}
        className="p-button-danger"
      />
    </div>
  ), [closeDeleteDialog, handleDeleteUser, selectedUser]);

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
                  onClick={() => fetchUsers()}
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
    <div className="flex flex-col gap-1">
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-bold text-gray-900 dark:text-white">
          Liste des utilisateurs
        </h1>
      </div>
      <div className="bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-700">
        <DataTable
          dataKey="id" value={users}
          size="small" paginator lazy
          first={(currentPage - 1) * pageSize}
          rows={pageSize} totalRecords={totalRecords}
          onPage={handlePageChange} loading={isLoading}
          emptyMessage="Aucun utilisateur trouvé"
          alwaysShowPaginator={false}
          rowsPerPageOptions={PAGE_SIZE_OPTIONS}
          tableStyle={{ minWidth: '50rem' }}
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
          onHide={closeEditDialog}
          header={`Modifier l'utilisateur: ${selectedUser?.username || ''}`}
          className="w-full max-w-lg rounded-xl overflow-hidden bg-white dark:bg-gray-800 p-0"
          modal closeOnEscape focusOnShow
          footer={editDialogFooter}
        >
          <div className="p-6 space-y-6">
            <LabeledInput
              id="edit-username"
              name="edit-username"
              label="Nom d'utilisateur"
              value={editForm.username}
              onChange={(e) => handleFormChange('username', e.target.value)}
              required
            />

            <LabeledDropdown
              id="edit-role"
              label="Rôle"
              value={editForm.role}
              options={roles}
              onChange={handleRoleChange}
              optionLabel="label"
              optionValue="name"
              placeholder="Sélectionner un rôle"
              required
            />
            {false && <LabeledPassword
              id="edit-password"
              name="edit-password"
              label="Nouveau mot de passe (optionnel)"
              value={editForm.password || ''}
              placeholder="Laisser vide pour ne pas modifier"
              onChange={(e) => handleFormChange('password', e.target.value)}
            />}
          </div>
        </Dialog>

        {/* Modal de réinitialisation du mot de passe */}
        <Dialog
          visible={passwordDialogVisible}
          onHide={closePasswordDialog}
          header={`Réinitialiser le mot de passe: ${selectedUser?.username || ''}`}
          className="w-full max-w-lg rounded-xl overflow-hidden bg-white dark:bg-gray-800 p-0"
          modal closeOnEscape focusOnShow
          footer={passwordDialogFooter}
        >
          <div className="p-6 space-y-6">
            <LabeledPassword
              id="new-password"
              name="new-password"
              label="Nouveau mot de passe"
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
              placeholder="Entrez le nouveau mot de passe"
              required
            />
          </div>
        </Dialog>

        {/* Modal de confirmation de suppression */}
        <Dialog
          visible={deleteDialogVisible}
          onHide={closeDeleteDialog}
          header="Confirmer la suppression"
          className="w-full max-w-lg rounded-xl overflow-hidden bg-white dark:bg-gray-800 p-0"
          modal closeOnEscape focusOnShow
          footer={deleteDialogFooter}
        >
          <div className="p-6">
            <div className="flex items-center space-x-3">
              <i className="pi pi-exclamation-triangle text-red-500 text-2xl" />
              <div>
                <p className="text-gray-700 dark:text-gray-300">
                  Êtes-vous sûr de vouloir supprimer l'utilisateur{' '}
                  <strong className="text-red-600">{selectedUser?.username}</strong> ?
                </p>
                <p className="text-sm text-gray-500 mt-1">
                  Cette action est irréversible.
                </p>
              </div>
            </div>
          </div>
        </Dialog>
      </div>
    </div>
  );
};

export default ListerUtilisateurs;