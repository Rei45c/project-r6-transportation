import React, {  useState } from "react";
import { useNavigate } from "react-router-dom"; 
import axios from "axios"; 
import "../App.css";
import Homepage from "./Homepage";

const Login = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const navigate = useNavigate(); // Hook for navigation
  
    const handleLogin = async (e) => {
      e.preventDefault();
      try {
        // Make API call to login endpoint
        const response = await axios.post("http://localhost:7070/api/users/login", {
          email,
          password,
        });
  
        const { role } = response.data; // Assuming response contains role
        if (role === "CUSTOMER") {
          navigate("/homepage", { state: { email: email } }); // Redirect to Customer part
        } else if (role === "MANAGER") {
          navigate("/manager"); // Redirect to Manager part
        }
         /*else if (role === "DRIVER") {
          navigate("/driver"); // Redirect to Driver part
        } */else {
          setErrorMessage("Unknown role. Please contact support.");
        }
      } catch (error) {
        setErrorMessage(
          error.response?.data
        );
      }
    };
  
    return (
      <div>
        <h2>Login</h2>
        <form onSubmit={handleLogin}>
          <input
            type="email"
            placeholder="Enter email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
          <input
            type="password"
            placeholder="Enter password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <button type="submit">Login</button>
        </form>
        {errorMessage && <p style={{ color: "red" }}>{errorMessage}</p>}
      </div>
    );
  };
  
  export default Login;