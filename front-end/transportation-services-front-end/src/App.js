import logo from './logo.svg';
import './App.css';

import React, { useEffect, useState } from "react";

function App() {
    const [message, setMessage] = useState("");

    useEffect(() => {
        // Funktion, die die Anfrage an die REST API stellt
        fetch("http://localhost:7070/hello")
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Network response was not ok");
                }
                return response.text();
            })
            .then((data) => setMessage(data))
            .catch((error) => {
                console.error("Error fetching the data:", error);
                setMessage("Error connecting to the API");
            });
    }, []);

    return (
        <div className="App">
            <h1>React-Spring Boot Verbindungstest</h1>
            <p>{message}</p>
        </div>
    );
}

export default App;