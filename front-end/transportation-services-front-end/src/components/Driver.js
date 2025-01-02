import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from 'react-router-dom';
import Navbar from './Navbar';
import Mybookings from './Mybookings';

const Driver = () => {
  const location = useLocation();
  const { email } = location.state || {};

  const [shipments, setShipments] = useState([]); // Holds shipment details

  // Fetch shipment details when the component loads
  useEffect(() => {
    fetchShipmentDetails();
  }, []);

  const fetchShipmentDetails = async () => {
    try {
      const response = await fetch(`http://localhost:7070/api/users/shipments?email=${email}`);
      const data = await response.json();
      setShipments(data);
      console.log(data[0]);
    } catch (error) {
      console.error("Error fetching shipment details:", error);
    }
  };

  const handleStart = async () => {
    try {
      const response = await fetch(`http://localhost:7070/api/users/shipments/start?shipmentId=${shipments[0].shipmentId}`, {
        method: "GET",
      });
  
      if (response.ok) {
        alert("Shipment started successfully!");
        // Optionally, refetch shipments to update the status on the front end
        //fetchShipmentDetails();
      } else {
        alert("Failed to start shipment.");
      }
    } catch (error) {
      console.error("Error starting the shipment:", error);
      alert("Error starting the shipment.");
    }
  };

  const handleEnd = async () => {
    try {
      const response = await fetch(`http://localhost:7070/api/users/shipments/end?shipmentId=${shipments[0].shipmentId}`, {
        method: "GET",
      });
  
      if (response.ok) {
        alert("Shipment ended successfully!");
        // Optionally, refetch shipments to update the status on the front end
        //fetchShipmentDetails();
      } else {
        alert("Failed to end shipment.");
      }
    } catch (error) {
      console.error("Error ending the shipment:", error);
      alert("Error ending the shipment.");
    }
  };

  return (
    <div>
      <Navbar />
      <div style={{ padding: "20px", maxWidth: "500px", margin: "auto" }}>
        <h2>Driver Page for {email}</h2>
          {shipments.length > 0 ? (
            <div style={{ marginBottom: "20px" }}>
              <h3>Shipment Details</h3>
              <p><strong>Shipment ID:</strong> {shipments[0].shipmentId}</p>
              <p><strong>Origin:</strong> {shipments[0].pickupLabel}</p>
              <p><strong>Destination:</strong> {shipments[0].destinationLabel}</p>
              <p><strong>Customer's Email:</strong> {shipments[0].customer_email}</p>
              <p><strong>Price to be paid by the customer:</strong> {shipments[0].price}</p>

              <div>
                <button
                  onClick={handleStart}
                  style={{
                    padding: "10px 20px",
                    marginRight: "10px",
                    backgroundColor: "#4CAF50",
                    color: "white",
                    border: "none",
                    cursor: "pointer",
                    borderRadius: "5px",
                  }}
                >
                  Start
                </button>
                <button
                  onClick={handleEnd}
                  style={{
                    padding: "10px 20px",
                    backgroundColor: "#f44336",
                    color: "white",
                    border: "none",
                    cursor: "pointer",
                    borderRadius: "5px",
                  }}
                >
                  End
                </button>
              </div>

            </div>
          ) : (
            <p>No shipment assigned yet...</p>
          )}
      </div>
    </div>
  );
};

export default Driver;