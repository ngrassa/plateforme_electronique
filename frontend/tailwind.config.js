/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./src/**/*.{js,jsx,ts,tsx}', './public/index.html'],
  theme: {
    extend: {
      fontFamily: {
        display: ['"Sora"', 'sans-serif'],
        body: ['"Manrope"', 'sans-serif'],
      },
      colors: {
        ink: {
          50: '#f4f6fb',
          100: '#e7ecf7',
          200: '#c9d6ef',
          300: '#9fb2e1',
          400: '#7b8ed2',
          500: '#5e6fc1',
          600: '#4956a7',
          700: '#3b4687',
          800: '#2f386a',
          900: '#242b50',
        },
      },
      boxShadow: {
        soft: '0 18px 45px -20px rgba(21, 30, 58, 0.35)',
        card: '0 20px 40px -30px rgba(21, 30, 58, 0.35)',
      },
    },
  },
  plugins: [],
};
