/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./app/**/*.{js,ts,jsx,tsx}', './components/**/*.{js,ts,jsx,tsx}'],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        brand: { 50: '#e6f2ff', 100: '#b3d9ff', 200: '#80bfff', 300: '#4da6ff', 400: '#1a8cff', 500: '#0061A4', 600: '#004d84', 700: '#003963', 800: '#002642', 900: '#001221' },
      },
    },
  },
  plugins: [],
}
