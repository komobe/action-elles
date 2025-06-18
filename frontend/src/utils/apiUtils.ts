// URL de base de l'API
export const API_BASE_URL = import.meta.env.VITE_API_URL ?? 'http://localhost:9083';

/**
 * Construit une URL d'API à partir de segments, en partant automatiquement de API_BASE_URL si le premier segment n'est pas une URL absolue.
 * Exemples :
 *   buildUrl('utilisateurs', 123, 'reset-password')
 *   buildUrl(API_BASE_URL, 'v1', 'utilisateurs', 123)
 */
export function buildUrl(...segments: (string | number)[]): string {
  let base = API_BASE_URL;
  let segs = segments;
  // Correction du test d'URL absolue
  if (typeof segments[0] === 'string' && /^https?:\/\//.test(segments[0])) {
    base = String(segments[0]);
    segs = segments.slice(1);
  }
  const cleanedBase = base.replace(/\/$/, '');
  const cleanedSegments = segs.map(s => String(s).replace(/^\/+/, ''));
  return [cleanedBase, ...cleanedSegments].join('/');
} 