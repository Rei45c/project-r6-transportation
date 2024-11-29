import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import '../styles.css';

const Navbar = () => {
  const navigate = useNavigate();
  const location = useLocation();

  // Check if the current URL is `/product-details`
  const isProductDetails = location.pathname === '/product-details';

  return (
    <nav className="navbar">
      <div className="navbar-logo">Transerv</div>
      <ul className="navbar-links">
        {isProductDetails ? (
          <li><a href="#logout" onClick={() => navigate('/')}>Log out</a></li>
        ) : (
          <>
            <li><a href="#how_it_works">How it works</a></li>
            <li><a href="#services_and_benefits">Services and benefits</a></li>
            <li><a href="#contact">Contact</a></li>
            <li><a href="#logout" onClick={() => navigate('/')}>Log out</a></li>
          </>
        )}
      </ul>
    </nav>
  );
};

export default Navbar;