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

  const handleSubmit = async (e) => {
    e.preventDefault();
  
    const requestData = {
      email,
      pickup: {label: pickup?.label, lat: pickup?.lat, lon: pickup?.lon},
      destination: {label: destination?.label, lat: destination?.lat, lon: destination?.lon},
      weight: parseFloat(weight),
      dimensions: {
        length: parseFloat(length),
        width: parseFloat(width),
        height: parseFloat(height),
      },
    };
    //console.log(requestData);
    try {
      const response = await fetch("http://localhost:7070/api/users/offer", {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' }, //Tells the server that the request body contains JSON data.
        body: JSON.stringify(requestData), //Sends the request data as a JSON string.
      });

      if (response.ok) {
        const data = await response.json(); //Parses the response body as JSON.
        //alert(data.cost);
        navigate('/offer', { state: { offerDetails: { ...requestData, cost: data.cost } } });
      } else {
        alert('Failed to generate offer. Please try again.');
      }
    } catch (error) {
      console.error('Error generating offer:', error);
      alert('An error occurred while generating the offer.');
    }
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