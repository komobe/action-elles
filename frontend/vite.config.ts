import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import { fileURLToPath } from 'url'

// Fonction helper pour créer des chemins absolus
const createPath = (relativePath: string) => fileURLToPath(new URL(relativePath, import.meta.url))

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      '@': createPath('./src'),
      '@components': createPath('./src/components'),
      '@pages': createPath('./src/pages'),
      '@assets': createPath('./src/assets'),
      '@styles': createPath('./src/styles'),
      '@utils': createPath('./src/utils'),
      '@hooks': createPath('./src/hooks'),
      '@services': createPath('./src/services'),
      '@types': createPath('./src/types'),
    },
  },
})
