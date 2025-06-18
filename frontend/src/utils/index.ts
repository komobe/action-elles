/**
 * Export de tous les utilitaires
 */

export * from './dateUtils';
export * from './formatUtils';
export * from './apiUtils';

export function buildUrl(base: string, ...segments: (string | number)[]): string {
  const cleanedBase = base.replace(/\/$/, '');
  const cleanedSegments = segments.map(s => String(s).replace(/^\//, ''));
  return [cleanedBase, ...cleanedSegments].join('/');
} 