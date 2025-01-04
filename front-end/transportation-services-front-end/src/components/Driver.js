import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from 'react-router-dom';
import Navbar from './Navbar';
import Mybookings from './Mybookings';

const Driver = () => {
  const location = useLocation();
  const { email } = location.state || {};

  const [shipments, setShipments] = useState([]);

  // Fetch shipment details when the component loads
  useEffect(() => {
    fetchShipmentDetails();
  }, []);

  const fetchShipmentDetails = async () => {
    try {
      const response = await fetch(`http://localhost:7070/api/users/shipments?email=${email}`);
      const data = await response.json();
      setShipments(data);
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
        <h2>Driver Page</h2>
        {shipments.length > 0 ? (
          shipments.filter(shipment => shipment.status !== "DELIVERED").length > 0 ? (
            <div style={{ marginBottom: "20px" }}>
              <h3>Shipment Details</h3>
              {/* Get the first shipment that is not delivered */}
              {shipments
                .filter(shipment => shipment.status !== "DELIVERED")
                .map((shipment, index) => (
                  <div key={index}>
                    <p><strong>Shipment ID:</strong> {shipment.shipmentId}</p>
                    <p><strong>Origin:</strong> {shipment.pickupLabel}</p>
                    <p><strong>Destination:</strong> {shipment.destinationLabel}</p>
                    <p><strong>Customer's Email:</strong> {shipment.customer_email}</p>
                    <p><strong>Price to be paid by the customer:</strong> {shipment.price}</p>
  
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
                ))}
            </div>
          ) : (
            <p>No shipment assigned yet...</p>
          )
        ) : (
          <p>No shipment assigned yet...</p>
        )}
      </div>
    </div>
  );
};

export default Driver;