import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import Navbar from './Navbar';
import AsyncSelect from 'react-select/async';

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

const Homepage = () => {
  const [pickup, setPickup] = useState({ label: '', lat: null, lon: null });
  const [destination, setDestination] = useState({ label: '', lat: null, lon: null });
  const navigate = useNavigate();
  const location = useLocation();
  const { email } = location.state || {};

  const handleSubmit = (e) => {
    e.preventDefault();
    if (pickup.label && destination.label) {
      navigate('/product-details', { state: { pickup, destination, email } });
    } else {
      alert('Please fill in both fields.');
    }
  };

  return (
    <div>
      <Navbar />
      <section id="home" className="section">
        <h1>Welcome to Transver</h1>
        <form onSubmit={handleSubmit} className="address-form">
          <AddressAutocomplete
            label="Pickup Address"
            value={pickup}
            onChange={(option) =>
              setPickup({ label: option.label, lat: option.lat, lon: option.lon })
            }
          />
          <AddressAutocomplete
            label="Destination Address"
            value={destination}
            onChange={(option) =>
              setDestination({ label: option.label, lat: option.lat, lon: option.lon })
            }
          />
          <button type="submit">Apply</button>
        </form>
      </section>

      <section id="how_it_works" className="section">
        <h2>How it works</h2>
        <p>
          We are a team dedicated to providing the best services for our customers. With years of
          experience in our field, we ensure quality and satisfaction.
        </p>
      </section>

      <section id="services_and_benefits" className="section">
        <h2>Services and benefits</h2>
        <ul>
          <li>Transportation at low cost</li>
          <li>Transport in all places in Austria</li>
          <li>Fast delivery</li>
          <li>Easy booking</li>
        </ul>
      </section>

      <section id="contact" className="section">
        <h2>Contact Us</h2>
        <p>Email: contact@myapp.com</p>
        <p>Phone: +123-456-7890</p>
        <p>Address: 123 MyApp Lane, Tech City</p>
      </section>
    </div>
  );
};

export default Homepage;