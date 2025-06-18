export const LOCALE_PAR_DEFAUT = 'fr-FR';
export const DEVISE_PAR_DEFAUT = 'XOF';
export const NOMBRE_MAX_DECIMALES_DEVISE = 0;

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