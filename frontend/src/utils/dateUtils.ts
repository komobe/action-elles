/**
 * Utilitaires pour le formatage des dates
 */

// Constantes de calcul
export const MILLISECONDES_PAR_JOUR = 1000 * 60 * 60 * 24;

type DateType = string | Date | number;
/**
 * Calcule le nombre de jours restants jusqu'à une date donnée
 * @param dateFin - La date de fin (string, Date ou timestamp)
 * @returns Le nombre de jours restants (arrondi au supérieur)
 */
export const calculerJoursRestants = (dateFin: DateType): number => {
  const dateFinObj = new Date(dateFin);
  const aujourdHui = new Date();
  const differenceTemps = dateFinObj.getTime() - aujourdHui.getTime();
  return Math.ceil(differenceTemps / MILLISECONDES_PAR_JOUR);
};

/**
 * Formate une date en français avec ou sans l'heure
 * @param date - La date à formater (string, Date ou timestamp)
 * @param locale - La locale à utiliser (défaut : 'fr-FR')
 * @returns La date formatée en français
 */
export const formaterDateHeure = (date: DateType, locale: string = 'fr-FR'): string => {
  const objetDate = new Date(date);
  return objetDate.toLocaleDateString(locale, {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
};

/**
 * Formate une date en français avec ou sans l'heure
 * @param date - La date à formater (string, Date ou timestamp)
 * @param locale - La locale à utiliser (défaut : 'fr-FR')
 * @returns La date formatée en français
 */
export const formaterDate = (date: DateType, locale: string = 'fr-FR'): string => {
  const objetDate = (date instanceof Date) ? date : new Date(date);
  return objetDate.toLocaleDateString(locale, {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  });
};

/**
 * Formate une date en français avec seulement l'heure
 * @param date - La date à formater (string, Date ou timestamp)
 * @param locale - La locale à utiliser (défaut : 'fr-FR')
 * @returns L'heure formatée en français
 */
export const formaterHeure = (date: DateType, locale: string = 'fr-FR'): string => {
  const objetDate = (date instanceof Date) ? date : new Date(date);
  return objetDate.toLocaleTimeString(locale, {
    hour: '2-digit',
    minute: '2-digit'
  });
}; 