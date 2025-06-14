/**
 * Utilitaires pour le formatage des dates
 */

/**
 * Formate une date en français avec année, mois, jour, heure et minute
 * @param date - La date à formater (string, Date ou timestamp)
 * @returns La date formatée en français
 */
export const formatDateFr = (date: string | Date | number): string => {
  const dateObj = new Date(date);

  return dateObj.toLocaleDateString('fr-FR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
};

/**
 * Formate une date en français avec seulement la date (sans l'heure)
 * @param date - La date à formater (string, Date ou timestamp)
 * @returns La date formatée en français
 */
export const formatDateOnlyFr = (date: string | Date | number): string => {
  const dateObj = new Date(date);

  return dateObj.toLocaleDateString('fr-FR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  });
};

/**
 * Formate une date en français avec seulement l'heure
 * @param date - La date à formater (string, Date ou timestamp)
 * @returns L'heure formatée en français
 */
export const formatTimeFr = (date: string | Date | number): string => {
  const dateObj = new Date(date);

  return dateObj.toLocaleTimeString('fr-FR', {
    hour: '2-digit',
    minute: '2-digit'
  });
}; 