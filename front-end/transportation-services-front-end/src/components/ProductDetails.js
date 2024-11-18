import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

const ProductDetails = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { pickup, destination } = location.state || {};
  const [weight, setWeight] = useState('');
  const [size, setSize] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    alert(`Details submitted successfully!\nPickup: ${pickup}\nDestination: ${destination}\nWeight: ${weight} kg\nSize: ${size}`);
    // You can further process or redirect as needed
  };

  const goBack = () => {
    navigate(-1); // Navigate to the previous page
  };

  return (
    <section className="section">
      <h1>Product Details</h1>
      <p><strong>Pickup Address:</strong> {pickup}</p>
      <p><strong>Destination Address:</strong> {destination}</p>

      <form onSubmit={handleSubmit}>
        <div>
          <label>Weight (kg):</label>
          <input type="number" placeholder="Enter weight" value={weight}
            onChange={(e) => setWeight(e.target.value)} required />
        </div>
        <div>
          <label>Size (LxWxH in cm):</label>
          <input type="text" placeholder="Enter size" value={size}
            onChange={(e) => setSize(e.target.value)} required />
        </div>
        <button type="submit">Submit</button>
      </form>

      <button onClick={goBack} style={{ marginTop: '20px', background: '#ccc' }}>
        Back
      </button>
    </section>
  );
};

export default ProductDetails;