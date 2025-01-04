import React, { useState, useEffect, useRef } from "react";
import { useLocation } from 'react-router-dom';
import { MapContainer, TileLayer, Marker, Popup, Polyline, useMap } from "react-leaflet";
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
  const [currentRouteIndex, setCurrentRouteIndex] = useState(0);

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
    }
  }, [shipments]);

  useEffect(() => {
    const fetchShipments = async () => {
      try {
        const response = await fetch(`http://localhost:7070/api/users/shipments?email=${email}`);
        const data = await response.json();
        const nonDeliveredShipments = data.filter(shipment => shipment.status !== 'DELIVERED');
        setShipments(nonDeliveredShipments);
        //console.log(data[0]);
        if (nonDeliveredShipments.length > 0 && nonDeliveredShipments[0].status === 'ON_THE_WAY') {
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

  useEffect(() => {
    const fetchCurrentRouteIndex = async () => {
      try {
        const response = await fetch(`http://localhost:7070/api/users/shipments/index?shipmentId=${shipments[0].shipmentId}`);
        const data = await response.json();
        const { index } = data;
        setCurrentRouteIndex(index);
      } catch (error) {
        console.error("Error fetching the index:", error);
      }
    };
    
    if(email && shipments.length>0) {
      fetchCurrentRouteIndex();
    }

    if (isDriving && route) {
      let currentIndex = currentRouteIndex;

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
  
  // dynamically set the map view on the current driver position
  const SetViewOnDriverPosition = ({ driverPosition }) => {
    const map = useMap();
 
    useEffect(() => {
      if (driverPosition) {
        map.setView(driverPosition, map.getZoom());
      }
    }, [driverPosition, map]);
  
    return null;
  };

  return (
    <div>
      <Navbar />
      <h1>My bookings</h1>
      {shipments.length > 0 ? (
        shipments.filter(shipment => shipment.status !== "DELIVERED").length > 0 ? (
          shipments
            .filter(shipment => shipment.status !== "DELIVERED")
            .map((shipment, index) => (
              <div key={index} style={{ display: "flex", gap: "20px", alignItems: "flex-start" }}>
                {/* Left Section: Booking Details */}
                <div style={{ flex: 1 }}>
                  <h2>Booking Details</h2>
                  <p><strong>Shipment ID:</strong> {shipment.shipmentId}</p>
                  <p><strong>Pickup Location:</strong> {shipment.pickupLabel}</p>
                  <p><strong>Destination:</strong> {shipment.destinationLabel}</p>
                  <p><strong>Driver's Email:</strong> {shipment.driver_email}</p>
                  <p><strong>Price:</strong> ${shipment.price}</p>
                  <p><strong>Shipment's Status:</strong> {shipment.status}</p>
  
                  {/* Display Distance and Duration */}
                  {distance && duration && (
                    <div style={{ marginTop: "10px" }}>
                      <p><strong>Distance:</strong> {distance} km</p>
                      <p><strong>Duration:</strong> {duration} minutes</p>
                    </div>
                  )}
                </div>
  
                {/* Right Section: Map */}
                <div style={{ flex: 2 }}>
                  <MapContainer
                    center={[shipment.pickupLat, shipment.pickupLon]}
                    zoom={14}
                    style={{ height: "400px", width: "100%" }}
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
  
                    <SetViewOnDriverPosition driverPosition={driverPosition} />
                  </MapContainer>
                </div>
              </div>
            ))
        ) : (
          <p style={{ textAlign: "center", marginTop: "20px", fontSize: "18px", color: "gray" }}>
            There are no shipments yet.
          </p>
        )
      ) : (
        <p style={{ textAlign: "center", marginTop: "20px", fontSize: "18px", color: "gray" }}>
          There are no bookings yet.
        </p>
      )}
    </div>
  );
};

export default Mybookings;