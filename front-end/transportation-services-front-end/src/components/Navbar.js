import React from 'react';
import '../styles.css';

const Navbar = () => {
  return (
    <nav className="navbar">
      <div className="navbar-logo">Transerv</div>
      <ul className="navbar-links">
        <li><a href="#how_it_works">How it works</a></li>
        <li><a href="#services_and_benefits">Services and benefits</a></li>
        <li><a href="#contact">Contact</a></li>
        <li><a href="#login">Login</a></li>
      </ul>
    </nav>
  );
};

export default Navbar;