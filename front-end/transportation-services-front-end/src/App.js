import React, { useEffect, useState } from "react";
//import { useForm } from "react-hook-form";
import Login from "./Login";
import Signup from "./Signup";
import './App.css';
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";

function App() {
    return (
      <Router>
        <div className="App">
          <nav>
            <ul>
              <li>
                <Link to="/">Login</Link>
              </li>
              <li>
                <Link to="/signup">Signup</Link>
              </li>
            </ul>
          </nav>
  
          <Routes>
            <Route path="/" element={<Login />} />
            <Route path="/signup" element={<Signup />} />
          </Routes>
        </div>
      </Router>
    );
  }

export default App;


// function App() {
//     const [message, setMessage] = useState("");

//     useEffect(() => {
//         // Funktion, die die Anfrage an die REST API stellt
//         fetch("http://localhost:7070/hello")
//             .then((response) => {
//                 if (!response.ok) {
//                     throw new Error("Network response was not ok");
//                 }
//                 return response.text();
//             })
//             .then((data) => setMessage(data))
//             .catch((error) => {
//                 console.error("Error fetching the data:", error);
//                 setMessage("Error connecting to the API");
//             });
//     }, []);

//     return (
//         <div className="App">
//             <h1>React-Spring Boot Verbindungstest</h1>
//             <p>{message}</p>
//         </div>
//     );
// }