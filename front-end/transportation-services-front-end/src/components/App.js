import React from 'react';
import Login from "./Login";
import Signup from "./Signup";
import Navbar from './Navbar';
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import Homepage from './Homepage';
import ProductDetails from './ProductDetails';
import '../App.css';

function App() {
  return (
    <Router>
      <div className="App">
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

        <Routes>
          <Route path="/" element={<Login />} />
          <Route path="/signup" element={<Signup />} />
          <Route path="/homepage" element={<Homepage />} />
          <Route path="/product-details" element={<ProductDetails />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;