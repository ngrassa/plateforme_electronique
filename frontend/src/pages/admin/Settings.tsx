import React from 'react';

const Settings = () => {
  return (
    <div className="space-y-6">
      <div className="rounded-3xl border border-white/80 bg-white p-6 shadow-card">
        <h2 className="font-display text-xl font-semibold text-ink-900">
          Parametres
        </h2>
        <p className="text-sm text-slate-500">
          Gere les preferences de la plateforme et les profils utilisateurs.
        </p>
      </div>

      <div className="grid gap-4 md:grid-cols-2">
        <div className="rounded-3xl border border-white/80 bg-white p-6 shadow-card">
          <p className="text-sm font-semibold text-slate-600">
            Notifications
          </p>
          <p className="mt-2 text-sm text-slate-500">
            Parametrez les alertes de paiement et de facturation.
          </p>
          <button
            type="button"
            className="mt-4 rounded-2xl border border-ink-200 bg-ink-50 px-4 py-2 text-sm font-semibold text-ink-700"
          >
            Configurer
          </button>
        </div>
        <div className="rounded-3xl border border-white/80 bg-white p-6 shadow-card">
          <p className="text-sm font-semibold text-slate-600">
            API & securite
          </p>
          <p className="mt-2 text-sm text-slate-500">
            Mettez a jour les clefs et les roles d'acces.
          </p>
          <button
            type="button"
            className="mt-4 rounded-2xl border border-ink-200 bg-ink-50 px-4 py-2 text-sm font-semibold text-ink-700"
          >
            Gerer les acces
          </button>
        </div>
      </div>
    </div>
  );
};

export default Settings;
