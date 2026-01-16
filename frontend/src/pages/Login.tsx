import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const DEMO_EMAIL = 'admin@example.com';
const DEMO_PASSWORD = 'admin123!';

const Login = () => {
  const [email, setEmail] = useState(DEMO_EMAIL);
  const [password, setPassword] = useState(DEMO_PASSWORD);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = (event: React.FormEvent) => {
    event.preventDefault();
    if (email === DEMO_EMAIL && password === DEMO_PASSWORD) {
      localStorage.setItem('demo_auth', 'true');
      setError('');
      navigate('/admin');
      return;
    }
    setError('Identifiants invalides.');
  };

  return (
    <div className="flex min-h-screen items-center justify-center bg-[radial-gradient(circle_at_top,_#ffffff,_#e7ecf7,_#d9e1f1)] px-6 py-10">
      <div className="w-full max-w-md rounded-3xl border border-white/80 bg-white p-8 shadow-card">
        <div className="mb-6">
          <p className="text-xs font-semibold uppercase tracking-[0.2em] text-ink-500">
            Portail admin
          </p>
          <h1 className="font-display text-2xl font-semibold text-ink-900">
            Connexion
          </h1>
          <p className="text-sm text-slate-500">
            Acces administrateur (mode demo).
          </p>
        </div>
        <form onSubmit={handleSubmit} className="space-y-4">
          <label className="text-sm font-semibold text-slate-600">
            Email
            <input
              type="email"
              value={email}
              onChange={(event) => setEmail(event.target.value)}
              required
              className="mt-2 w-full rounded-2xl border border-slate-200 bg-slate-50 px-4 py-2 text-sm"
            />
          </label>
          <label className="text-sm font-semibold text-slate-600">
            Mot de passe
            <input
              type="password"
              value={password}
              onChange={(event) => setPassword(event.target.value)}
              required
              className="mt-2 w-full rounded-2xl border border-slate-200 bg-slate-50 px-4 py-2 text-sm"
            />
          </label>
          {error ? (
            <div className="rounded-2xl border border-rose-200 bg-rose-50 px-4 py-2 text-xs font-semibold text-rose-600">
              {error}
            </div>
          ) : null}
          <button
            type="submit"
            className="w-full rounded-2xl bg-ink-500 px-4 py-3 text-sm font-semibold text-white shadow-soft"
          >
            Se connecter
          </button>
        </form>
        <div className="mt-6 rounded-2xl border border-ink-100 bg-ink-50 px-4 py-3 text-xs text-ink-700">
          Identifiants: <strong>{DEMO_EMAIL}</strong> /{' '}
          <strong>{DEMO_PASSWORD}</strong>
        </div>
      </div>
    </div>
  );
};

export default Login;
