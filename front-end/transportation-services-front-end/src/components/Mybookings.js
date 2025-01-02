import React, { useState, useEffect } from "react";
import { useLocation } from 'react-router-dom';
import { MapContainer, TileLayer, Marker, Popup, Polyline } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import "leaflet.awesome-markers";
import L from "leaflet";
import Navbar from './Navbar';
import { useCallback } from "react";

const customDivIcon = L.divIcon({
    html: `<div style="
        background-color: red; 
        width: 20px; 
        height: 20px; 
        border-radius: 50%; 
        border: 2px solid white;">
      </div>`,
    className: "", // Removes any default Leaflet styling
    iconAnchor: [10, 10], // Center the icon
  });

const Mybookings = () => {
  const location = useLocation();
  const { email } = location.state || {};
  const [shipments, setShipments] = useState([]);

  const [route, setRoute] = useState(null);
  const [distance, setDistance] = useState(null);
  const [duration, setDuration] = useState(null);
  const [driverPosition, setDriverPosition] = useState(null);
  const [isDriving, setIsDriving] = useState(false);

  const calculateRoute = useCallback(async () => {
    const pickupCoords = `${shipments[0].pickupLon},${shipments[0].pickupLat}`;
    const destinationCoords = `${shipments[0].destinationLon},${shipments[0].destinationLat}`;
  
    const response = await fetch(
      `https://router.project-osrm.org/route/v1/driving/${pickupCoords};${destinationCoords}?overview=full&geometries=geojson`
    );
    const data = await response.json();
  
    if (data.routes && data.routes.length > 0) {
      const routeData = data.routes[0];
      setRoute(routeData.geometry.coordinates.map(([lon, lat]) => [lat, lon]));
      setDistance((routeData.distance / 1000).toFixed(2));
      setDuration((routeData.duration / 60).toFixed(2));
      setDriverPosition([shipments[0].pickupLat, shipments[0].pickupLon]); // Set initial driver position
    }
  }, [shipments]);

  // const handleStart = () => {
  //   setIsDriving(true);
  // };

  // const handleEnd = () => {
  //   setIsDriving(false);
  // };

  useEffect(() => {
    const fetchShipments = async () => {
      try {
        const response = await fetch(`http://localhost:7070/api/users/shipments?email=${email}`);
        const data = await response.json();
        setShipments(data);
        //console.log(data[0]);
        if (data.length > 0 && data[0].status === 'ON_THE_WAY') {
          setIsDriving(true);
        }
      } catch (error) {
        console.error("Error fetching shipments:", error);
      }
    };
  
    if (email) {
      fetchShipments();
    }
  }, [email]);

  useEffect(() => {
    if (shipments.length > 0) {
      calculateRoute();
    }
  }, [shipments]);

  useEffect(() => { // triggers whenever isDriving, route or duration changes
    if (isDriving && route) {
      let currentIndex = 0;
 
      const interval = setInterval(() => {
        if (currentIndex < route.length) {
          setDriverPosition(route[currentIndex]);
          currentIndex++;
        } else {
          clearInterval(interval);
          setIsDriving(false);
        }
      }, (duration * 60 * 1000) / route.length); // total duration in ms / number of route points

      return () => clearInterval(interval);
    }
  }, [isDriving, route, duration]);

  // Optionally, if you want to update `isDriving` based on any status change after fetching:
  useEffect(() => {
    if (shipments.length > 0) {
      const status = shipments[0].status;
      if (status === 'ON_THE_WAY' && !isDriving) {
        setIsDriving(true);
      } else if (status !== 'ON_THE_WAY' && isDriving) {
        setIsDriving(false);
      }
    }
  }, [shipments, isDriving]); 

  return (
    <div>
      <Navbar />
      <h1>My bookings for {isDriving}</h1>
      <div style={{ display: "grid", gridTemplateColumns: "1fr 2fr", gap: "20px" }}>
        {shipments.length > 0 ? (
          <>
            <div>
              <h2>Booking Details</h2>
              <p><strong>Shipment ID:</strong> {shipments[0].shipmentId}</p>
              <p><strong>Pickup Location:</strong> {shipments[0].pickupLabel}</p>
              <p><strong>Destination:</strong> {shipments[0].destinationLabel}</p>
              <p><strong>Driver's Email:</strong> {shipments[0].driver_email}</p>
              <p><strong>Price:</strong> ${shipments[0].price}</p>
              <p><strong>Shipment's Status:</strong> {shipments[0].status}</p>

              {/* <button onClick={handleStart} style={{ marginTop: '20px', background: '#ccc' }}>Start</button>
              <button onClick={handleEnd} style={{ marginTop: '20px', background: '#ccc' }}>End</button> */}
  
              {/* Display Distance and Duration */}
              {distance && duration && (
                <div style={{ marginTop: "10px" }}>
                  <p><strong>Distance:</strong> {distance} km</p>
                  <p><strong>Duration:</strong> {duration} minutes</p>
                </div>
              )}
            </div>
  
            {/* Map Section */}
            <MapContainer
              center={[shipments[0].pickupLat, shipments[0].pickupLon]}
              zoom={14}
              style={{ height: "500px", width: "100%" }}
            >
              <TileLayer
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
              />
  
              {/* Route Polyline */}
              {route && <Polyline positions={route} color="blue" />}
  
              {/* Driver Position Marker */}
              {driverPosition && (
                <Marker position={driverPosition} icon={customDivIcon}>
                  <Popup>Driver's Current Position</Popup>
                </Marker>
              )}
            </MapContainer>
          </>
        ) : (
          <p style={{ textAlign: "center", marginTop: "20px", fontSize: "18px", color: "gray" }}>
            There are no bookings yet.
          </p>
        )}
      </div>
    </div>
  );
};

export default Mybookings;