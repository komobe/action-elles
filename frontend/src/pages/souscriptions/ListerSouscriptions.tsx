import Pagination from '@/components/common/Pagination';
import { parseStatutToDisplay, Souscription, souscriptionHttpService } from '@/services/souscription.http-service';
import { formaterDate } from '@/utils/dateUtils';
import { useToast } from '@contexts/ToastContext';
import { faCar, faChevronDown, faChevronUp, faFileAlt, faTimes, faTrash, faUser } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import 'primeicons/primeicons.css';
import { Tag } from 'primereact/tag';
import React, { useCallback, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';



// Constante pour la taille de page par défaut
const DEFAULT_PAGE_SIZE = 10;

const renderStatutSouscription = (statut: string) => {
  return (<Tag className='bg-gray-200 text-black' value={parseStatutToDisplay(statut)} />)
}

export default function ListerSouscriptions() {
  const navigate = useNavigate();
  const [souscriptions, setSouscriptions] = useState<Souscription[]>([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState('');
  const [dateRange, setDateRange] = useState('all');
  const [currentPage, setCurrentPage] = useState(1);
  const [currentPageSize, setCurrentPageSize] = useState<number>(DEFAULT_PAGE_SIZE);
  const [totalRecords, setTotalRecords] = useState(0);

  const { error: showError, success: showSuccess } = useToast();

  const [selectedSouscription, setSelectedSouscription] = useState<Souscription | null>(null);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [souscriptionToDelete, setSouscriptionToDelete] = useState<Souscription | null>(null);
  const [expandedSections, setExpandedSections] = useState<{ [key: string]: boolean }>({});

  useEffect(() => {
    fetchSouscriptions();
  }, []);

  const fetchSouscriptions = async () => {
    try {
      const response = await souscriptionHttpService.lister();
      const responseData = response.data ?? [];
      setSouscriptions(responseData ?? []);
      setTotalRecords(responseData.length); //TODO: A modifier si pagination supporté par le backend
    } catch (error) {
      showError('Erreur lors du chargement des souscriptions');
    }
  };

  const handleDeleteClick = (souscription: Souscription) => {
    setSouscriptionToDelete(souscription);
    setShowDeleteModal(true);
  };

  const handleDeleteConfirm = async () => {
    if (souscriptionToDelete) {
      try {
        await souscriptionHttpService.delete(souscriptionToDelete.id);
        setSouscriptions(souscriptions.filter(s => s.id !== souscriptionToDelete.id));
        setShowDeleteModal(false);
        setSouscriptionToDelete(null);
        showSuccess('Souscription supprimée avec succès');
        setTotalRecords(prev => prev - 1);
      } catch (error) {
        showError('Erreur lors de la suppression de la souscription');
      }
    }
  };

  const handlePageChange = useCallback((page: number) => {
    setCurrentPage(page);
  }, []);

  const handlePageSizeChange = useCallback((size: number) => {
    setCurrentPageSize(size);
  }, []);


  const handleEditConfirm = async () => {
    if (selectedSouscription) {
      try {
        // TODO: Implémenter la logique de modification ici
        console.log('Modifier souscription:', selectedSouscription);
        setSelectedSouscription(null);
        showSuccess('Souscription modifiée avec succès');
        // Recharger les données
        await fetchSouscriptions();
      } catch (error) {
        showError('Erreur lors de la modification de la souscription');
      }
    }
  };

  const filteredSouscriptions = souscriptions.filter((souscription) => {
    const matchesSearch = souscription.id.includes(searchTerm) ||
      souscription.assure.nom.includes(searchTerm) ||
      souscription.assure.prenoms.includes(searchTerm);
    const matchesStatus = statusFilter === '' || souscription.statut === statusFilter;
    const matchesDate = dateRange === 'all' ||
      (dateRange === 'today' && new Date(souscription.dateSouscription).toDateString() === new Date().toDateString()) ||
      (dateRange === 'week' && new Date(souscription.dateSouscription).toDateString() >= new Date(Date.now() - 7 * 24 * 60 * 60 * 1000).toDateString()) ||
      (dateRange === 'month' && new Date(souscription.dateSouscription).toDateString() >= new Date(Date.now() - 30 * 24 * 60 * 60 * 1000).toDateString()) ||
      (dateRange === 'year' && new Date(souscription.dateSouscription).toDateString() >= new Date(Date.now() - 365 * 24 * 60 * 60 * 1000).toDateString());
    return matchesSearch && matchesStatus && matchesDate;
  });

  const toggleSection = (souscriptionId: string, sectionId: string) => {
    const key = `${souscriptionId}-${sectionId}`;
    setExpandedSections(prev => ({ ...prev, [key]: !prev[key] }));
  };

  const isSectionExpanded = (souscriptionId: string, sectionId: string) => {
    return expandedSections[`${souscriptionId}-${sectionId}`] || false;
  };

  const toggleDetails = (id: string) => {
    const key = `${id}-details`;
    setExpandedSections(prev => ({ ...prev, [key]: !prev[key] }));
  };

  return (
    <div className="space-y-2">
      {/* En-tête avec titre et bouton d'ajout */}
      <div className="flex justify-between items-center app-form-fieldset py-2">
        <h1 className="text-2xl font-bold text-gray-900 dark:text-white mb-0">Liste des souscriptions</h1>
        <button
          onClick={() => navigate('/souscriptions/creer')}
          className="app-form-button-primary"
        >
          Nouvelle souscription
        </button>
      </div>

      {/* Filtres */}
      <div className="bg-white dark:bg-gray-800 rounded-lg shadow-sm border border-gray-200 dark:border-gray-700 p-4">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <label htmlFor="search" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
              Rechercher
            </label>
            <input
              type="text"
              id="search"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              placeholder="Rechercher une souscription..."
              className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500 dark:bg-gray-700 dark:text-white"
            />
          </div>
          <div>
            <label htmlFor="status" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
              Statut
            </label>
            <select
              id="status"
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value)}
              className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500 dark:bg-gray-700 dark:text-white"
            >{
                //TODO: A modifier
              }
              <option value="">Tous les statuts</option>
              <option value="ACTIVE">Active</option>
              <option value="INACTIVE">Inactive</option>
              <option value="PENDING">En attente</option>
            </select>
          </div>
        </div>
      </div>

      {/* Tableau des souscriptions */}
      <div className="bg-white dark:bg-gray-800 rounded-lg shadow-sm border border-gray-200 dark:border-gray-700 overflow-hidden">
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200 dark:divide-gray-700">
            <thead className="bg-gray-50 dark:bg-gray-900/50">
              <tr>
                <th scope="col" className="px-2 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                </th>
                <th scope="col" className="px-2 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                  Numero
                </th>
                <th scope="col" className="px-2 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                  Utilisateur
                </th>
                <th scope="col" className="px-2 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                  Immatriculation
                </th>
                <th scope="col" className="px-2 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                  Statut
                </th>
                <th scope="col" className="px-2 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                  Date de souscription
                </th>
                <th scope="col" className="w-10 px-2 py-3 text-center text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody className="bg-white dark:bg-gray-800 divide-y divide-gray-200 dark:divide-gray-700">
              {filteredSouscriptions.map((souscription) => (
                <React.Fragment key={souscription.id}>
                  <tr className="hover:bg-gray-50 dark:hover:bg-gray-700/50 transition-colors duration-150">
                    <td className="w-10 p-2 whitespace-nowrap text-sm text-center">
                      <button
                        onClick={() => toggleDetails(souscription.id)}
                        className="text-gray-600 hover:text-gray-900 dark:text-gray-400 dark:hover:text-gray-300"
                      >
                        <FontAwesomeIcon
                          icon={isSectionExpanded(souscription.id, 'details') ? faChevronUp : faChevronDown}
                          className="w-4 h-4"
                        />
                      </button>
                    </td>
                    <td className="p-2 whitespace-nowrap text-sm text-gray-900 dark:text-white">
                      {souscription.numero}
                    </td>
                    <td className="p-2 whitespace-nowrap text-sm text-gray-900 dark:text-white">
                      {souscription.assure.nom} {souscription.assure.prenoms}
                    </td>
                    <td className="p-2 whitespace-nowrap text-sm text-gray-900 dark:text-white">
                      {souscription.vehicule.immatriculation}
                    </td>
                    <td className="p-2">
                      <span className={`inline-flex items-center text-xs font-medium rounded-md whitespace-nowrap`} >
                        {renderStatutSouscription(souscription.statut)}
                      </span>
                    </td>
                    <td className="p-2 whitespace-nowrap text-sm text-gray-900 dark:text-white">
                      {formaterDate(souscription.dateSouscription)}
                    </td>
                    <td className="p-2 whitespace-nowrap text-sm font-medium space-x-2">
                      <button
                        onClick={() => handleDeleteClick(souscription)}
                        className="text-red-600 hover:text-red-900 dark:text-red-400 dark:hover:text-red-300 transition-colors duration-200"
                        title="Supprimer"
                      >
                        <FontAwesomeIcon icon={faTrash} className="w-4 h-4" />
                      </button>
                    </td>
                  </tr>
                  {isSectionExpanded(souscription.id, 'details') && (
                    <tr>
                      <td colSpan={7} className="p-2 bg-gray-50 dark:bg-gray-900/50">
                        <div className="space-y-4">
                          <div className="bg-white dark:bg-gray-800 rounded-lg shadow-sm border border-gray-200 dark:border-gray-700">
                            <button
                              onClick={() => toggleSection(souscription.id, 'assure')}
                              className="w-full p-3 flex items-center justify-between text-left hover:bg-gray-50 dark:hover:bg-gray-700/50 transition-all duration-200"
                            >
                              <div className="flex items-center">
                                <FontAwesomeIcon icon={faUser} className="w-5 h-5 mr-3 text-indigo-600 dark:text-indigo-400" />
                                <h4 className="text-base font-semibold text-gray-800 dark:text-gray-200 mb-0">
                                  Informations de l'assuré
                                </h4>
                              </div>
                              <FontAwesomeIcon
                                icon={faChevronDown}
                                className={`w-4 h-4 text-gray-500 dark:text-gray-400 transition-transform duration-200 ${isSectionExpanded(souscription.id, 'assure') ? 'rotate-180' : ''}`}
                              />
                            </button>
                            <div className={`grid transition-all duration-200 ease-in-out ${isSectionExpanded(souscription.id, 'assure') ? 'grid-rows-[1fr] opacity-100' : 'grid-rows-[0fr] opacity-0'}`}>
                              <div className="overflow-hidden">
                                <div className="p-5">
                                  <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
                                    <div key="nom" className="flex flex-col space-y-1">
                                      <span className="text-sm font-medium text-gray-500 dark:text-gray-400">Nom</span>
                                      <span className="text-sm font-medium text-gray-900 dark:text-gray-100">{souscription.assure.nom}</span>
                                    </div>
                                    <div key="prenoms" className="flex flex-col space-y-1">
                                      <span className="text-sm font-medium text-gray-500 dark:text-gray-400">Prénoms</span>
                                      <span className="text-sm font-medium text-gray-900 dark:text-gray-100">{souscription.assure.prenoms}</span>
                                    </div>
                                    <div key="email" className="flex flex-col space-y-1">
                                      <span className="text-sm font-medium text-gray-500 dark:text-gray-400">Email</span>
                                      <span className="text-sm font-medium text-gray-900 dark:text-gray-100 break-all">{souscription.assure.email}</span>
                                    </div>
                                    <div key="telephone" className="flex flex-col space-y-1">
                                      <span className="text-sm font-medium text-gray-500 dark:text-gray-400">Téléphone</span>
                                      <span className="text-sm font-medium text-gray-900 dark:text-gray-100">{souscription.assure.telephone}</span>
                                    </div>
                                    <div key="adresse" className="flex flex-col space-y-1">
                                      <span className="text-sm font-medium text-gray-500 dark:text-gray-400">Adresse</span>
                                      <span className="text-sm font-medium text-gray-900 dark:text-gray-100 break-words">{souscription.assure.adresse}</span>
                                    </div>
                                    <div key="ville" className="flex flex-col space-y-1">
                                      <span className="text-sm font-medium text-gray-500 dark:text-gray-400">Ville</span>
                                      <span className="text-sm font-medium text-gray-900 dark:text-gray-100">{souscription.assure.ville}</span>
                                    </div>
                                    <div key="dateNaissance" className="flex flex-col space-y-1">
                                      <span className="text-sm font-medium text-gray-500 dark:text-gray-400">Date de naissance</span>
                                      <span className="text-sm font-medium text-gray-900 dark:text-gray-100">{formaterDate(souscription.assure.dateNaissance)}</span>
                                    </div>
                                    <div key="numeroCNI" className="flex flex-col space-y-1">
                                      <span className="text-sm font-medium text-gray-500 dark:text-gray-400">Numéro CNI</span>
                                      <span className="text-sm font-medium text-gray-900 dark:text-gray-100">{souscription.assure.numeroCarteIdentite}</span>
                                    </div>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </div>

                          <div className="bg-white dark:bg-gray-800 rounded-lg shadow-sm border border-gray-200 dark:border-gray-700">
                            <button
                              onClick={() => toggleSection(souscription.id, 'vehicule')}
                              className="w-full p-3 flex items-center justify-between text-left hover:bg-gray-50 dark:hover:bg-gray-700/50 transition-all duration-200"
                            >
                              <div className="flex items-center">
                                <FontAwesomeIcon icon={faCar} className="w-5 h-5 mr-3 text-indigo-600 dark:text-indigo-400" />
                                <h4 className="text-base font-semibold text-gray-800 dark:text-gray-200 mb-0">
                                  Informations du véhicule
                                </h4>
                              </div>
                              <FontAwesomeIcon
                                icon={faChevronDown}
                                className={`w-4 h-4 text-gray-500 dark:text-gray-400 transition-transform duration-200 ${isSectionExpanded(souscription.id, 'vehicule') ? 'rotate-180' : ''}`}
                              />
                            </button>
                            <div className={`grid transition-all duration-200 ease-in-out ${isSectionExpanded(souscription.id, 'vehicule') ? 'grid-rows-[1fr] opacity-100' : 'grid-rows-[0fr] opacity-0'}`}>
                              <div className="overflow-hidden">
                                <div className="p-5">
                                  <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
                                    <div key="immatriculation" className="flex flex-col space-y-1">
                                      <span className="text-sm font-medium text-gray-500 dark:text-gray-400">Immatriculation</span>
                                      <span className="text-sm font-medium text-gray-900 dark:text-gray-100">{souscription.vehicule.immatriculation}</span>
                                    </div>
                                    <div key="dateMiseEnCirculation" className="flex flex-col space-y-1">
                                      <span className="text-sm font-medium text-gray-500 dark:text-gray-400">Date de mise en circulation</span>
                                      <span className="text-sm font-medium text-gray-900 dark:text-gray-100">{formaterDate(souscription.vehicule.dateMiseEnCirculation)}</span>
                                    </div>
                                    <div key="couleur" className="flex flex-col space-y-1">
                                      <span className="text-sm font-medium text-gray-500 dark:text-gray-400">Couleur</span>
                                      <span className="text-sm font-medium text-gray-900 dark:text-gray-100">{souscription.vehicule.couleur}</span>
                                    </div>
                                    <div key="nombreSieges" className="flex flex-col space-y-1">
                                      <span className="text-sm font-medium text-gray-500 dark:text-gray-400">Nombre de sièges</span>
                                      <span className="text-sm font-medium text-gray-900 dark:text-gray-100">{souscription.vehicule.nombreDeSieges}</span>
                                    </div>
                                    <div key="nombrePortes" className="flex flex-col space-y-1">
                                      <span className="text-sm font-medium text-gray-500 dark:text-gray-400">Nombre de portes</span>
                                      <span className="text-sm font-medium text-gray-900 dark:text-gray-100">{souscription.vehicule.nombreDePortes}</span>
                                    </div>
                                    <div key="puissanceFiscale" className="flex flex-col space-y-1">
                                      <span className="text-sm font-medium text-gray-500 dark:text-gray-400">Puissance fiscale</span>
                                      <span className="text-sm font-medium text-gray-900 dark:text-gray-100">{souscription.vehicule.puissanceFiscale} CV</span>
                                    </div>
                                    <div key="categorie" className="flex flex-col space-y-1">
                                      <span className="text-sm font-medium text-gray-500 dark:text-gray-400">Catégorie</span>
                                      <span className="text-sm font-medium text-gray-900 dark:text-gray-100">{souscription.vehicule.categorie.libelle}</span>
                                    </div>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </div>

                          <div className="bg-white dark:bg-gray-800 rounded-lg shadow-sm border border-gray-200 dark:border-gray-700">
                            <button
                              onClick={() => toggleSection(souscription.id, 'souscription')}
                              className="w-full p-3 flex items-center justify-between text-left hover:bg-gray-50 dark:hover:bg-gray-700/50 transition-all duration-200"
                            >
                              <div className="flex items-center">
                                <FontAwesomeIcon icon={faFileAlt} className="w-5 h-5 mr-3 text-indigo-600 dark:text-indigo-400" />
                                <h4 className="text-base font-semibold text-gray-800 dark:text-gray-200 mb-0">
                                  Informations de la souscription
                                </h4>
                              </div>
                              <FontAwesomeIcon
                                icon={faChevronDown}
                                className={`w-4 h-4 text-gray-500 dark:text-gray-400 transition-transform duration-200 ${isSectionExpanded(souscription.id, 'souscription') ? 'rotate-180' : ''}`}
                              />
                            </button>
                            <div className={`grid transition-all duration-200 ease-in-out ${isSectionExpanded(souscription.id, 'souscription') ? 'grid-rows-[1fr] opacity-100' : 'grid-rows-[0fr] opacity-0'}`}>
                              <div className="overflow-hidden">
                                <div className="p-5">
                                  <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
                                    <div key="id" className="flex flex-col space-y-1">
                                      <span className="text-sm font-medium text-gray-500 dark:text-gray-400">Numero</span>
                                      <span className="text-sm font-medium text-gray-900 dark:text-gray-100">{souscription.numero}</span>
                                    </div>
                                    <div key="statut" className="flex flex-col space-y-1">
                                      <span className="text-sm font-medium text-gray-500 dark:text-gray-400">Statut</span>
                                      <span className={`inline-flex items-center text-xs font-medium rounded-md whitespace-nowrap`}>
                                        {renderStatutSouscription(souscription.statut)}
                                      </span>
                                    </div>
                                    <div key="dateCreation" className="flex flex-col space-y-1">
                                      <span className="text-sm font-medium text-gray-500 dark:text-gray-400">Date souscription</span>
                                      <span className="text-sm font-medium text-gray-900 dark:text-gray-100">{formaterDate(souscription.dateSouscription)}</span>
                                    </div>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>
                      </td>
                    </tr>
                  )}
                </React.Fragment>
              ))}
            </tbody>
          </table>
        </div>

        {/* Pagination du tableau */}
        <Pagination
          currentPage={currentPage}
          currentPageSize={currentPageSize}
          totalRecords={totalRecords} // TODO: A modifier lorsque le backend prendra en compte la pagination
          onPageChange={handlePageChange}
          onPageSizeChange={handlePageSizeChange}
          displayedItemsCount={filteredSouscriptions.length}
          defautPageSize={DEFAULT_PAGE_SIZE}
        />
      </div>

      {/* Modal de modification */}
      {selectedSouscription && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white dark:bg-gray-800 rounded-lg shadow-xl max-w-md w-full mx-4">
            <div className="p-2 border-b border-gray-200 dark:border-gray-700">
              <h3 className="text-lg font-medium text-gray-900 dark:text-white">
                Modifier la souscription : {selectedSouscription.id}
              </h3>
            </div>
            <div className="p-6 space-y-4">
              <div>
                <label htmlFor="editStatus" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                  Statut
                </label>
                <select
                  id="editStatus"
                  value={selectedSouscription.statut}
                  onChange={(e) => setSelectedSouscription(prev => prev ? { ...prev, statut: e.target.value as 'ACTIVE' | 'INACTIVE' | 'PENDING' } : null)}
                  className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500 dark:bg-gray-700 dark:text-white"
                >
                  <option value="ACTIVE">Active</option>
                  <option value="INACTIVE">Inactive</option>
                  <option value="PENDING">En attente</option>
                </select>
              </div>
            </div>
            <div className="p-2 border-t border-gray-200 dark:border-gray-700 flex justify-end space-x-3">
              <button
                onClick={() => setSelectedSouscription(null)}
                className="px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-md text-sm font-medium text-gray-700 dark:text-gray-300 hover:bg-gray-50 dark:hover:bg-gray-700 transition-colors duration-200"
              >
                Annuler
              </button>
              <button
                onClick={handleEditConfirm}
                className="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-colors duration-200"
              >
                Enregistrer
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Modal de suppression */}
      {showDeleteModal && souscriptionToDelete && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white dark:bg-gray-800 rounded-lg p-6 max-w-md w-full mx-4">
            <div className="flex items-center justify-between mb-6">
              <h3 className="text-lg font-medium text-gray-900 dark:text-white">
                Confirmer la suppression
              </h3>
              <button
                onClick={() => {
                  setShowDeleteModal(false);
                  setSouscriptionToDelete(null);
                }}
                className="text-gray-400 hover:text-gray-500 dark:hover:text-gray-300 transition-colors duration-200"
              >
                <FontAwesomeIcon icon={faTimes} className="w-5 h-5" />
              </button>
            </div>
            <div className="bg-gray-50 dark:bg-gray-700/50 rounded-lg p-4 mb-6 border border-gray-200 dark:border-gray-600">
              <div className="space-y-3">
                <p className="text-sm text-gray-600 dark:text-gray-300 flex items-center">
                  <span className="font-medium text-gray-700 dark:text-gray-200 w-20">Numero:</span>
                  <span className="font-mono text-xs">{souscriptionToDelete.numero}</span>
                </p>
                <p className="text-sm text-gray-600 dark:text-gray-300 flex items-center">
                  <span className="font-medium text-gray-700 dark:text-gray-200 w-20">Assuré:</span>
                  <span>{souscriptionToDelete.assure.nom} {souscriptionToDelete.assure.prenoms}</span>
                </p>
                <p className="text-sm text-gray-600 dark:text-gray-300 flex items-center">
                  <span className="font-medium text-gray-700 dark:text-gray-200 w-20">Véhicule:</span>
                  <span className="font-mono">{souscriptionToDelete.vehicule.immatriculation}</span>
                </p>
              </div>
            </div>
            <p className="text-sm text-gray-500 dark:text-gray-400 mb-6">
              Êtes-vous sûr de vouloir supprimer cette souscription ? Cette action est irréversible.
            </p>
            <div className="flex justify-end space-x-3">
              <button
                onClick={() => {
                  setShowDeleteModal(false);
                  setSouscriptionToDelete(null);
                }}
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
      )}
    </div>
  );
}