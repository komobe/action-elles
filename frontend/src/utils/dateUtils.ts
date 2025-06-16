/**
 * Utilitaires pour le formatage des dates et des montants
 */

// Constantes configurables
export const LOCALE_PAR_DEFAUT = 'fr-FR';
export const DEVISE_PAR_DEFAUT = 'XOF';

// Constantes de calcul
export const MILLISECONDES_PAR_JOUR = 1000 * 60 * 60 * 24;

// Constantes de formatage
export const NOMBRE_MAX_DECIMALES_DEVISE = 0;

// Options de formatage
export const OPTIONS_FORMATAGE_DEVISE = {
  style: 'currency' as const,
  currency: DEVISE_PAR_DEFAUT,
  maximumFractionDigits: NOMBRE_MAX_DECIMALES_DEVISE
};

/**
 * Formate un montant en devise XOF (Franc CFA)
 * @param montant - Le montant à formater
 * @param locale - La locale à utiliser (défaut: 'fr-FR')
 * @returns Le montant formaté en XOF
 */
export const formaterMontant = (montant: number, locale: string = LOCALE_PAR_DEFAUT): string => {
  return new Intl.NumberFormat(locale, OPTIONS_FORMATAGE_DEVISE).format(montant);
};

/**
 * Calcule le nombre de jours restants jusqu'à une date donnée
 * @param dateFin - La date de fin (string, Date ou timestamp)
 * @returns Le nombre de jours restants (arrondi au supérieur)
 */
export const calculerJoursRestants = (dateFin: string | Date | number): number => {
  const dateFinObj = new Date(dateFin);
  const aujourdHui = new Date();
  const differenceTemps = dateFinObj.getTime() - aujourdHui.getTime();
  return Math.ceil(differenceTemps / MILLISECONDES_PAR_JOUR);
};


/**
 * Formate une date en français avec ou sans l'heure
 * @param date - La date à formater (string, Date ou timestamp)
 * @param inclureHeure - Inclure l'heure dans le formatage (défaut: false)
 * @param locale - La locale à utiliser (défaut: 'fr-FR')
 * @returns La date formatée en français
 */
export const formaterDateHeure = (date: string | Date | number, inclureHeure: boolean = false, locale: string = LOCALE_PAR_DEFAUT): string => {
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
 * @param locale - La locale à utiliser (défaut: 'fr-FR')
 * @returns La date formatée en français
 */
export const formaterDate = (date: string | Date | number, locale: string = LOCALE_PAR_DEFAUT): string => {

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
 * @param locale - La locale à utiliser (défaut: 'fr-FR')
 * @returns L'heure formatée en français
 */
export const formaterHeure = (date: string | Date | number, locale: string = LOCALE_PAR_DEFAUT): string => {
  const objetDate = (date instanceof Date) ? date : new Date(date);

  return objetDate.toLocaleTimeString(locale, {
    hour: '2-digit',
    minute: '2-digit'
  });
}; 