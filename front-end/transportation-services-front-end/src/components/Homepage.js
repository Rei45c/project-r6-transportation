import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Navbar from './Navbar';
import AsyncSelect from 'react-select/async';

const fetchSuggestions = async (inputValue) => {
    if (!inputValue) return [];
    const response = await fetch(
      `https://nominatim.openstreetmap.org/search?format=geojson&q=${inputValue}`
    );
    const data = await response.json();
    return data.features.map((feature) => ({
      label: feature.properties.display_name, // Display name for dropdown (full address name)
      value: feature.properties.name // gives only the first name of the address for example at "Ginzkeyplatz, Salzburg, Austria" would be "Ginzkeyplatz"
    }));
  };
  
  const AddressAutocomplete = ({ label, value, onChange }) => (
    <div>
      <label>{label}:</label>
      <AsyncSelect
        cacheOptions
        loadOptions={fetchSuggestions}
        onChange={onChange}
        value={value}
      />
    </div>
  );

const Homepage = () => {
  const [pickup, setPickup] = useState('');
  const [destination, setDestination] = useState('');
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();
    if (pickup && destination) {
      // Redirect to product details page
      navigate('/product-details', { state: { pickup: pickup.label, destination: destination.label } });
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
            onChange={setPickup}
          />
          <AddressAutocomplete
            label="Destination Address"
            value={destination}
            onChange={setDestination}
          />
          <button type="submit">Apply</button>
        </form>
        </section>

        <section id="how_it_works" className="section">
          <h2>How it works</h2>
          <p>We are a team dedicated to providing the best services for our customers. With years of experience in our field, we ensure quality and satisfaction.</p>
        </section>
        
        <section id="services_and_benefits" className="section">
          <h2>Services and benefits</h2>
          <ul>″
            <li>Custom Web Development</li>
            <li>Mobile App Development</li>
            <li>Cloud Computing Solutions</li>
            <li>Technical Support</li>
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