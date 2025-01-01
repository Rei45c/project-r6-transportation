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

  const { pickup, destination, weight, dimensions, email, email_driver, address_vehicle, vehicle_id, cost, duration } = offerDetails;

  const handleAccept = async (e) => {
    //e.preventDefault();

    // Shipment[id, status, price, driver_id, createdBy(*to1), pickuplat, pickuplon, destlat, destlon]
    const requestData = {
      email_driver: email_driver,
      email_customer: email,
      vehicle_id: vehicle_id,
      pickup: {label: pickup?.label, lat: pickup?.lat, lon: pickup?.lon},
      destination: {label: destination?.label, lat: destination?.lat, lon: destination?.lon},
      price: cost
    };

    try {
      const response = await fetch("http://localhost:7070/api/users/accepted", {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' }, //Tells the server that the request body contains JSON data.
        body: JSON.stringify(requestData), //Sends the request data as a JSON string.
      });
      if (response.ok) {
        const message = await response.text();
        alert(message);
        navigate(-2);
      } else {
        alert("Failed to accept offer. Please try again.");
      }
    } catch (error) {
      alert(error.response?.data || "Something went wrong");
    }
  };

  return (
    <div>
      <Navbar />
      <section className='offer1'>
        <h1>Offer Summary</h1>
        <p><strong>Your email:</strong> {email}</p>
        <p><strong>Driver's email:</strong> {email_driver}</p>
        <p><strong>Vehicle's parking address:</strong> {address_vehicle}</p>
        <p><strong>Pickup Address:</strong> {pickup.label}</p>
        <p><strong>Destination Address:</strong> {destination.label}</p>
        <p><strong>Weight:</strong> {weight} kg</p>
        <p><strong>Dimensions:</strong> {dimensions.length} m x {dimensions.width} m x {dimensions.height} m</p>
        <p><strong>Duration:</strong> {duration} hours</p>
        <p><strong>Cost:</strong> €{cost}</p>
        <button onClick={() => navigate(-1)}>Modify Details</button>
        <button onClick={handleAccept} style={{ marginLeft: '10px' }}>Accept Offer</button>
      </section>
    </div>
  );
};

export default Offer;