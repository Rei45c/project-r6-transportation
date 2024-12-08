package net.group.transportation.services.sp.transportationservicebackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import net.group.transportation.services.sp.transportationservicebackend.repositories.UserRepository;
import net.group.transportation.services.sp.transportationservicebackend.entity.User;

import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.http.HttpStatus;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
        
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

    User user = userOptional.get();

    if (!user.getPassword().equals(loginRequest.getPassword())) {
        // If password doesn't match, return 401 Unauthorized
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
    }

    // Return user role as response for redirection in the user app
    return ResponseEntity.ok(Map.of("role", user.getRole().toString()));
    }

    @PostMapping("/offer")
    public ResponseEntity<?> generateOffer(@RequestBody Map<String, Object> offerRequest) {
        try {
            // Extract fields from the request
            String email = (String) offerRequest.get("email");
            Map<String, Object> pickup = (Map<String, Object>) offerRequest.get("pickup");
            Map<String, Object> destination = (Map<String, Object>) offerRequest.get("destination");
            Map<String, Object> dimensions = (Map<String, Object>) offerRequest.get("dimensions");
            double weight = Double.parseDouble(offerRequest.get("weight").toString());
            double length = Double.parseDouble(dimensions.get("length").toString());
            double width = Double.parseDouble(dimensions.get("width").toString());
            double height = Double.parseDouble(dimensions.get("height").toString());

            // Extract coordinates from pickup and destination
            double pickupLat = Double.parseDouble(pickup.get("lat").toString());
            double pickupLon = Double.parseDouble(pickup.get("lon").toString());
            double destinationLat = Double.parseDouble(destination.get("lat").toString());
            double destinationLon = Double.parseDouble(destination.get("lon").toString());

            double distance = calculateDistance(pickupLat, pickupLon, destinationLat, destinationLon);
            double volume = length * width * height;
            double cost = calculateCost(distance, weight, volume);

            Map<String, Object> response = Map.of(
                "email", email,
                "pickup", Map.of("label", pickup.get("label"), "lat", pickupLat, "lon", pickupLon),
                "destination", Map.of("label", destination.get("label"), "lat", destinationLat, "lon", destinationLon),
                "weight", weight,
                "dimensions", Map.of("length", length, "width", width, "height", height),
                "cost", cost
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // exceptions such as missing fields or invalid data
            return ResponseEntity.badRequest().body("Invalid request: " + e.getMessage());
        }
    }

    // Haversine formula to calculate the distance between 2 points in a sphere (Earth)
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double earth_radius = 6367.5; // approx Earth radius in km

        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double lon1Rad = Math.toRadians(lon1);
        double lon2Rad = Math.toRadians(lon2);

        double deltaPhi = lat2Rad - lat1Rad;
        double deltaLambda = lon2Rad - lon1Rad;

        double enumerator = 1 - Math.cos(deltaPhi) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * (1 - Math.cos(deltaLambda));

        double dist = 2 * earth_radius * Math.asin(Math.sqrt(enumerator/2)); 

        return dist; // in km
    }

    private double calculateCost(double distance, double weight, double volume) {
        double baseRate = 5.0; // base rate per km
        double weightRate = 2.0; // weight rate per kg
        double volumeRate = 0.05; // volume rate per cubic cm
        return (baseRate * distance) + (weightRate * weight) + (volumeRate * volume);
    }
}