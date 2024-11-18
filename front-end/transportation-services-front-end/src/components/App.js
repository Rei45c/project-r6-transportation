import React from 'react';
import Navbar from './Navbar';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Homepage from './Homepage';
import ProductDetails from './ProductDetails';

function App() {
  return (
    <Router>
      <div>

        <Routes>
          <Route path="/" element={<Homepage />} />
          <Route path="/product-details" element={<ProductDetails />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;

// npm install @react-google-maps/api
// npm install react-select react-select-async-paginate
// npm install react-router-dom