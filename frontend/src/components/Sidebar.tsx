import React from 'react';
import { NavLink } from 'react-router-dom';

const navLinkClass = ({ isActive }: { isActive: boolean }) =>
  [
    'flex items-center gap-3 rounded-xl px-4 py-3 text-sm font-semibold transition',
    isActive
      ? 'bg-ink-500 text-white shadow-soft'
      : 'text-slate-600 hover:bg-white hover:text-ink-700',
  ].join(' ');

const Sidebar = () => {
  return (
    <aside className="w-full max-w-[260px] border-r border-white/40 bg-white/60 p-6 backdrop-blur-xl lg:min-h-screen">
      <div className="flex items-center gap-3 rounded-2xl bg-white px-4 py-3 shadow-card">
        <div className="grid h-10 w-10 place-items-center rounded-xl bg-ink-500 text-white">
          PE
        </div>
        <div>
          <p className="font-display text-sm font-semibold text-ink-900">
            Plateforme
          </p>
          <p className="text-xs text-slate-500">Paiement electronique</p>
        </div>
      </div>

      <nav className="mt-10 grid gap-2">
        <NavLink to="/admin" end className={navLinkClass}>
          Dashboard
        </NavLink>
        <NavLink to="/admin/invoices" className={navLinkClass}>
          Factures
        </NavLink>
        <NavLink to="/admin/clients" className={navLinkClass}>
          Clients
        </NavLink>
        <NavLink to="/admin/payments" className={navLinkClass}>
          Paiements
        </NavLink>
        <NavLink to="/admin/settings" className={navLinkClass}>
          Parametres
        </NavLink>
      </nav>

      <div className="mt-10 rounded-2xl border border-white/70 bg-white/70 p-4 text-sm text-slate-600">
        <p className="font-semibold text-ink-800">Astuce du jour</p>
        <p className="mt-2">
          Centralisez vos factures et vos paiements pour un suivi en temps
          reel.
        </p>
      </div>
    </aside>
  );
};

export default Sidebar;
