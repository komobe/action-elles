import { useState, useEffect } from 'react';
import { http } from '@services/http';
import { API_ENDPOINTS } from '@/config/api';
import { useToast } from '../../contexts/ToastContext';

interface Souscription {
  id: string;
  numero: string;
  dateCreation: string;
  dateEffet: string;
  statut: 'EN_COURS' | 'VALIDEE' | 'REJETEE' | 'EXPIREE';
  montant: number;
  assure: {
    id: string;
    nom: string;
    prenom: string;
    sexe: string;
    dateNaissance: string;
    email: string;
    numeroCarteIdentite: string;
    telephone: string;
    adresse: string;
    ville: string;
  };
  vehicule: {
    numeroImmatriculation: string;
    dateMiseEnCirculation: string;
    couleur: string;
    nombreDeSieges: number;
    nombreDePortes: number;
    categorieCode: string;
    puissanceFiscale: number;
    carburant: string;
    //numeroChassis: string;
  };
}

const SuiviSouscriptions = () => {
  const [souscriptions, setSouscriptions] = useState<Souscription[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const { error: showError } = useToast();
  const [selectedUser, setSelectedUser] = useState<string>('');
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    fetchSouscriptions();
  }, [selectedUser]);

  const fetchSouscriptions = async () => {
    try {
      setIsLoading(true);
      const url = selectedUser
        ? `${API_ENDPOINTS.souscription.list}?userId=${selectedUser}`
        : API_ENDPOINTS.souscription.list;

      const response = await http.get<{ status: string; data: Souscription[] }>(url);
      if (response.status === 'success') {
        setSouscriptions(response.data);
        console.log(response.data);
      }
    } catch (error) {
      const message = 'Une erreur est survenue lors du chargement des souscriptions';
      setError(message);
      showError(message);
    } finally {
      setIsLoading(false);
    }
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('fr-FR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  };

  const formatMontant = (montant: number) => {
    return new Intl.NumberFormat('fr-FR', {
      style: 'currency',
      currency: 'XOF',
      maximumFractionDigits: 0
    }).format(montant);
  };

  const getStatutBadgeClass = (statut: Souscription['statut']) => {
    switch (statut) {
      case 'VALIDEE':
        return 'bg-green-100 text-green-800 dark:bg-green-900/50 dark:text-green-200';
      case 'EN_COURS':
        return 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900/50 dark:text-yellow-200';
      case 'REJETEE':
        return 'bg-red-100 text-red-800 dark:bg-red-900/50 dark:text-red-200';
      case 'EXPIREE':
        return 'bg-gray-100 text-gray-800 dark:bg-gray-900/50 dark:text-gray-200';
      default:
        return 'bg-gray-100 text-gray-800 dark:bg-gray-900/50 dark:text-gray-200';
    }
  };

  const filteredSouscriptions = souscriptions.filter(souscription =>
    souscription.numero.toLowerCase().includes(searchTerm.toLowerCase()) ||
    souscription.vehicule.numeroImmatriculation.toLowerCase().includes(searchTerm.toLowerCase()) ||
    souscription.assure.nom.toLowerCase().includes(searchTerm.toLowerCase()) ||
    souscription.assure.prenom.toLowerCase().includes(searchTerm.toLowerCase()) ||
    souscription.assure.email.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="bg-white dark:bg-gray-800 shadow-lg rounded-lg overflow-hidden">
      <div className="p-6">
        <h1 className="text-2xl font-bold text-gray-900 dark:text-white mb-6">
          Suivi des Souscriptions
        </h1>

        {error && (
          <div className="mb-4 bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 p-4 rounded-lg">
            <p className="text-sm text-red-600 dark:text-red-400">{error}</p>
          </div>
        )}

        {/* Filtres */}
        <div className="mb-6 flex flex-col sm:flex-row gap-4">
          <div className="flex-1">
            <input
              type="text"
              placeholder="Rechercher par n° police, immatriculation ou utilisateur..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-700 text-gray-900 dark:text-white placeholder-gray-400 dark:placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-indigo-500"
            />
          </div>
        </div>

        {/* Table des souscriptions */}
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200 dark:divide-gray-700">
            <thead className="bg-gray-50 dark:bg-gray-700/50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                  N° Police
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                  Utilisateur
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                  Véhicule
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                  Date d'effet
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                  Montant
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                  Statut
                </th>
              </tr>
            </thead>
            <tbody className="bg-white dark:bg-gray-800 divide-y divide-gray-200 dark:divide-gray-700">
              {isLoading ? (
                <tr>
                  <td colSpan={6} className="px-6 py-4 text-center text-sm text-gray-500 dark:text-gray-400">
                    Chargement...
                  </td>
                </tr>
              ) : filteredSouscriptions.length === 0 ? (
                <tr>
                  <td colSpan={6} className="px-6 py-4 text-center text-sm text-gray-500 dark:text-gray-400">
                    Aucune souscription trouvée
                  </td>
                </tr>
              ) : (
                filteredSouscriptions.map((souscription) => (
                  <tr key={souscription.id}>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm font-medium text-gray-900 dark:text-white">
                        {souscription.numero}
                      </div>
                      <div className="text-xs text-gray-500 dark:text-gray-400">
                        Créée le {formatDate(souscription.dateCreation)}
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm text-gray-900 dark:text-white">
                        {souscription.assure.nom} {souscription.assure.prenom}
                      </div>
                      <div className="text-xs text-gray-500 dark:text-gray-400">
                        {souscription.assure.email}
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm text-gray-900 dark:text-white">
                        {souscription.vehicule.categorieCode}
                      </div>
                      <div className="text-xs text-gray-500 dark:text-gray-400">
                        {souscription.vehicule.numeroImmatriculation}
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-400">
                      {formatDate(souscription.dateEffet)}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900 dark:text-white">
                      {formatMontant(souscription.montant)}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${getStatutBadgeClass(souscription.statut)}`}>
                        {souscription.statut}
                      </span>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default SuiviSouscriptions; 