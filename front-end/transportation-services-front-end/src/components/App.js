import React from "react";
import { BrowserRouter as Router, Routes, Route, Link, useLocation } from "react-router-dom";
import Login from "./Login";
import Signup from "./Signup";
import Offer from './Offer';
import Homepage from './Homepage';
import ProductDetails from './ProductDetails';
import Manager from './Manager';
import Mybookings from './Mybookings';
import Driver from './Driver';
import '../App.css';

function App() {

  return (
    <Router>
      <div className="App">
        <ConditionalNav />
        <Routes>
          <Route path="/" element={<Login />} />
          <Route path="/signup" element={<Signup />} />
          <Route path="/homepage" element={<Homepage />} />
          <Route path="/manager" element={<Manager />} />
          <Route path="/product-details" element={<ProductDetails />} />
          <Route path="/offer" element={<Offer />} />
          <Route path="/mybookings" element={<Mybookings />} />
          <Route path="/driver" element={<Driver />} />
        </Routes>
      </div>
    </Router>
  );
}

function ConditionalNav() {
  const location = useLocation();

  // Show `nav` only for Login and Signup routes
  const showNav = location.pathname === "/" || location.pathname === "/signup";

  return (
    showNav && (
      <nav>
        <ul>
          <li>
            <Link to="/">Login</Link>
          </li>
          <li>
            <Link to="/signup">Signup</Link>
          </li>
        </ul>
      </nav>
    )
  );
}

export default App;