import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import Navbar from './Navbar';

const ProductDetails = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { pickup, destination, email } = location.state || {};
  const [weight, setWeight] = useState('');
  const [length, setLength] = useState('');
  const [width, setWidth] = useState('');
  const [height, setHeight] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    // weight
    // length
    // width
    // height
    // email
    // pickup.label --> full address
    // destination.label --> full address
    // pickup.lon    destination.lon   pickup.lat   destination.lat
    alert(`--> ${destination.lon}`);
    // You can further process or redirect as needed
  };

  const goBack = () => {
    navigate(-1); // Navigate to the previous page
  };

  return (
    <div>
      <Navbar />
    <section className="section">
      <h1>Product Details</h1>
      <p><strong>Pickup Address:</strong> {pickup?.label}</p>
      <p><strong>Destination Address:</strong> {destination?.label}</p>

      <form onSubmit={handleSubmit}>
        <div>
          <label>Weight (kg):</label>
          <input type="number" placeholder="Enter weight" value={weight}
            onChange={(e) => setWeight(e.target.value)} required />
        </div>
        <div>
          <label>Length (in cm):</label>
          <input type="text" placeholder="Enter length" value={length}
            onChange={(e) => setLength(e.target.value)} required />
        </div>
        <div>
          <label>Width (in cm):</label>
          <input type="text" placeholder="Enter width" value={width}
            onChange={(e) => setWidth(e.target.value)} required />
        </div>
        <div>
          <label>Height (in cm):</label>
          <input type="text" placeholder="Enter height" value={height}
            onChange={(e) => setHeight(e.target.value)} required />
        </div>
        <button type="submit">Submit</button>
      </form>

      <button onClick={goBack} style={{ marginTop: '20px', background: '#ccc' }}>
        Back
      </button>
    </section>
    </div>
  );
};

export default ProductDetails;