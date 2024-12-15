import React from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import Navbar from './Navbar';

const Offer = () => {
  const location = useLocation();
  const navigate = useNavigate();

  // Data passed from ProductDetails
  const { offerDetails } = location.state || {};

  if (!offerDetails) {
    return (
      <div>
        <h2>No offer available</h2>
        <button onClick={() => navigate('/product-details')}>Back to Product Details</button>
      </div>
    );
  }

  const { pickup, destination, weight, dimensions, email, email_driver, cost, duration } = offerDetails;

  return (
    <div>
      <Navbar />
      <section className='offer1'>
        <h1>Offer Summary</h1>
        <p><strong>Your email:</strong> {email}</p>
        <p><strong>Driver's email:</strong> {email_driver}</p>
        <p><strong>Pickup Address:</strong> {pickup.label}</p>
        <p><strong>Destination Address:</strong> {destination.label}</p>
        <p><strong>Weight:</strong> {weight} kg</p>
        <p><strong>Dimensions:</strong> {dimensions.length} cm x {dimensions.width} cm x {dimensions.height} cm</p>
        <p><strong>Duration:</strong> {duration} hours</p>
        <p><strong>Cost:</strong> €{cost}</p>
        <button onClick={() => navigate(-1)}>Modify Details</button>
      </section>
    </div>
  );
};

export default Offer;