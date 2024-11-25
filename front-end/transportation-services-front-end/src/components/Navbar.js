import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles.css';

const Navbar = () => {
  const navigate = useNavigate(); // Navigation hook

  return (
    <nav className="navbar">
      <div className="navbar-logo">Transerv</div>
      <ul className="navbar-links">
        <li><a href="#how_it_works">How it works</a></li>
        <li><a href="#services_and_benefits">Services and benefits</a></li>
        <li><a href="#contact">Contact</a></li>
        <li><a href="#logout" onClick={() => navigate('/')}>Log out</a></li>
      </ul>
    </nav>
  );
};

export default Navbar;