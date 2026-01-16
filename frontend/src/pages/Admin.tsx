import React from 'react';
import { useNavigate } from 'react-router-dom';

const Admin = () => {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem('demo_auth');
    navigate('/login');
  };

  return (
    <div className="page">
      <div className="card">
        <h1>Tableau de bord</h1>
        <p className="muted">
          Vous etes connecte en tant qu&apos;administrateur (mode demo).
        </p>
        <div className="grid">
          <div className="stat">
            <span>Factures</span>
            <strong>128</strong>
          </div>
          <div className="stat">
            <span>Paiements</span>
            <strong>54</strong>
          </div>
          <div className="stat">
            <span>Clients</span>
            <strong>23</strong>
          </div>
        </div>
        <button className="secondary" onClick={handleLogout}>
          Se deconnecter
        </button>
      </div>
    </div>
  );
};

export default Admin;
