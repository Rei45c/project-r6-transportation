import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import '../navbar.css';

const Navbar = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const { email } = location.state || {};

  // Check if the current URL is `/product-details`
  const is_not_homepage = location.pathname === '/product-details' 
                          || location.pathname === '/offer' 
                          || location.pathname === '/manager' 
                          || location.pathname === '/mybookings'
                          || location.pathname === '/driver';

  return (
    <nav className="navbar">
      <div className="navbar-logo">Transerv</div>
      <ul className="navbar-links">
        {is_not_homepage ? (
          <li><a href="#logout" onClick={() => navigate('/')}>Log out</a></li>
        ) : (
          <>
            <li><a href="" onClick={() => navigate('/mybookings', { state: { email } })}>My bookings</a></li>
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