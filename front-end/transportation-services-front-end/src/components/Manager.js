import React, { useState } from "react";
import AsyncSelect from 'react-select/async';
import axios from "axios";
import Navbar from './Navbar';

const fetchSuggestions = async (inputValue) => {
    if (!inputValue) return [];
    const response = await fetch(
      `https://nominatim.openstreetmap.org/search?format=geojson&q=${inputValue}`
    );
    const data = await response.json();
    return data.features.map((feature) => ({
      label: feature.properties.display_name, // Full address name
      value: feature.properties.name, // Short address name
      lat: feature.geometry.coordinates[1],  // Latitude
      lon: feature.geometry.coordinates[0],  // Longitude
    }));
  };
  
  const AddressAutocomplete = ({ label, value, onChange }) => (
    <div>
      <label>{label}:</label>
      <AsyncSelect
        cacheOptions
        loadOptions={fetchSuggestions}
        onChange={(selectedOption) => onChange(selectedOption)}
        value={value ? { label: value.label } : null} // Maintain selection
      />
    </div>
  );

const Manager = () => {
  const [driver, setDriver] = useState({
    name: "",
    email: "",
    password: "",
    label: "",
    lat: null,
    lon: null,
    available: "Available"
  });

  const [vehicle, setVehicle] = useState({
    label: "",
    vehicleType: "",
    maxWeight: "",
    maxVolume: "",
    lat: null,
    lon: null,
    available: "Available"
  });

  const [deleteDriverId, setDeleteDriverId] = useState("");
  const [deleteVehicleId, setDeleteVehicleId] = useState("");

  const handleDriverSubmit = async (e) => {
    e.preventDefault();
    console.log("Driver Data:", driver);

    try {
        const response = await axios.post("http://localhost:7070/api/users/registerDriver", {
          name: driver.name,
          email: driver.email,
          password: driver.password,
          address: driver.label,
          positionLatitude: driver.lat,
          positionLongitude: driver.lon,
          available: driver.available === "Available" ? 1 : 0
        });
        alert(response.data);
      } catch (error) {
        alert(error.response?.data || "Something went wrong");
      }
  };

  const handleVehicleSubmit = async (e) => {
    e.preventDefault();
    console.log("Vehicle Data:", vehicle);

    try {
        const response = await axios.post("http://localhost:7070/api/users/registerVehicle", {
          vehicleType: vehicle.vehicleType,
          address: vehicle.label,
          currentPositionLatitude: vehicle.lat,
          currentPositionLongitude: vehicle.lon,
          available: vehicle.available === "Available" ? 1 : 0,
          maxWeight: vehicle.maxWeight,
          maxVolume: vehicle.maxVolume
        });
        alert(response.data);
      } catch (error) {
        alert(error.response?.data || "Something went wrong");
      }
  };

  const handleDeleteDriver = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.delete(`http://localhost:7070/api/users/deleteDriver/${deleteDriverId}`);
      alert(response.data);
    } catch (error) {
      alert(error.response?.data || "Something went wrong");
    }
  };

  const handleDeleteVehicle = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.delete(`http://localhost:7070/api/users/deleteVehicle/${deleteVehicleId}`);
      alert(response.data);
    } catch (error) {
      alert(error.response?.data || "Something went wrong");
    }
  };

  return (
    <div style={{ padding: "20px" }}>
      <Navbar />
      <h1>Transportation Management</h1>

      <div style={{ marginBottom: "30px" }}>
        <h2>Add Driver</h2>
        <form onSubmit={handleDriverSubmit}>
          <div>
            <label>Name:</label>
            <input
              type="text"
              value={driver.name}
              onChange={(e) =>
                setDriver((prev) => ({ ...prev, name: e.target.value }))
              }
              required
            />
          </div>
          <div>
            <label>Email:</label>
            <input
              type="email"
              value={driver.email}
              onChange={(e) =>
                setDriver((prev) => ({ ...prev, email: e.target.value }))
              }
              required
            />
          </div>
          <div>
            <label>Password:</label>
            <input
              type="password"
              value={driver.password}
              onChange={(e) =>
                setDriver((prev) => ({ ...prev, password: e.target.value }))
              }
              required
            />
          </div>
          <div>
            <AddressAutocomplete
                label="Driver address"
                value={driver}
                onChange={(option) =>
                  setDriver((prev) => ({ ...prev, label: option.label, lat: option.lat, lon: option.lon }))
                }
            />
          </div>
          <div>
            <label>Status:</label>
            <select
              value={driver.available}
              onChange={(e) =>
                setDriver((prev) => ({ ...prev, available: e.target.value }))
              }
              required
            >
              <option value="Available">Available</option>
              <option value="Unavailable">Unavailable</option>
            </select>
          </div>
          <button type="submit">Add Driver</button>
        </form>
        <h2>Remove Driver</h2>
        <form onSubmit={handleDeleteDriver}>
          <div>
            <label>Driver ID:</label>
            <input
              type="text"
              value={deleteDriverId}
              onChange={(e) => setDeleteDriverId(e.target.value)}
              required
            />
          </div>
          <button type="submit">Remove Driver</button>
        </form>
      </div>

      <div>
        <h2>Add Vehicle</h2>
        <form onSubmit={handleVehicleSubmit}>
          <div>
            <label>Vehicle Type:</label>
            <select
              value={vehicle.vehicleType}
              onChange={(e) =>
                setVehicle((prev) => ({ ...prev, vehicleType: e.target.value }))
              }
              required
            >
              <option value="" disabled>
                Select a type
              </option>
              <option value="Truck">Truck</option>
              <option value="Van">Van</option>
              <option value="Car">Car</option>
            </select>
          </div>
          <div>
            <label>Max Weight (kg):</label>
            <input
              type="number"
              value={vehicle.maxWeight}
              onChange={(e) =>
                setVehicle((prev) => ({ ...prev, maxWeight: e.target.value }))
              }
              required
            />
          </div>
          <div>
            <label>Max Volume (cubic m):</label>
            <input
              type="number"
              value={vehicle.maxVolume}
              onChange={(e) =>
                setVehicle((prev) => ({ ...prev, maxVolume: e.target.value }))
              }
              required
            />
          </div>
          <div>
            <AddressAutocomplete
                label="Vehicle parking address"
                value={vehicle}
                onChange={(option) =>
                  setVehicle((prev) => ({ ...prev, label: option.label, lat: option.lat, lon: option.lon }))
                }
            />
          </div>
          <div>
            <label>Status:</label>
            <select
              value={vehicle.available}
              onChange={(e) =>
                setVehicle((prev) => ({ ...prev, available: e.target.value }))
              }
              required
            >
              <option value="Available">Available</option>
              <option value="Unavailable">Unavailable</option>
            </select>
          </div>
          <button type="submit">Add Vehicle</button>
        </form>
        <h2>Remove Vehicle</h2>
        <form onSubmit={handleDeleteVehicle}>
          <div>
            <label>Vehicle ID:</label>
            <input
              type="text"
              value={deleteVehicleId}
              onChange={(e) => setDeleteVehicleId(e.target.value)}
              required
            />
          </div>
          <button type="submit">Remove Vehicle</button>
        </form>
      </div>
    </div>
  );
};

export default Manager;