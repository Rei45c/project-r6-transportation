package net.group.transportation.services.sp.transportationservicebackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpMethod;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.group.transportation.services.sp.transportationservicebackend.repositories.UserRepository;
import net.group.transportation.services.sp.transportationservicebackend.repositories.DriverRepository;
import net.group.transportation.services.sp.transportationservicebackend.repositories.VehicleRepository;
import net.group.transportation.services.sp.transportationservicebackend.repositories.ShipmentRepository;
import net.group.transportation.services.sp.transportationservicebackend.entity.User;
import net.group.transportation.services.sp.transportationservicebackend.entity.Driver;
import net.group.transportation.services.sp.transportationservicebackend.entity.Vehicle;
import net.group.transportation.services.sp.transportationservicebackend.entity.Shipment;
import net.group.transportation.services.sp.transportationservicebackend.dto.DriverDTO;
import net.group.transportation.services.sp.transportationservicebackend.enums.userRole;
import net.group.transportation.services.sp.transportationservicebackend.enums.shipmentStatus;


import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.http.HttpStatus;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.stream.Collectors;

import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/shipment")
@CrossOrigin(origins = "http://localhost:3000")
public class ShipmentController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private ShipmentRepository shipmentRepository;

    private final Map<Long, Boolean> shipmentThreadsRunning = new ConcurrentHashMap<>();

    @GetMapping("/shipments")
    public ResponseEntity<?> getShipmentsByCustomerEmail_or_DriverEmail(@RequestParam String email) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Customer not found with email: " + email));

            List<Shipment> shipments = null;
            if (user.getRole() == userRole.CUSTOMER) // * shipment to 1 customer (for Mybookings component)
              shipments = shipmentRepository.findByCreatedBy(user); // shipments created by that customer
            else if (user.getRole() == userRole.DRIVER) // 1 shipment to 1 driver (for Driver component)
              shipments = shipmentRepository.findByDriver((Driver)user); // shipment assigned to that driver

            List<Map<String, Object>> response = null;

            response = shipments.stream()
                                //.filter(shipment -> shipment.getShipmentStatus() != shipmentStatus.DELIVERED)
                                .map(shipment -> {
                Map<String, Object> shipmentData = new HashMap<>();
                shipmentData.put("shipmentId", shipment.getId());
                shipmentData.put("pickupLabel", shipment.getPickupLabel());
                shipmentData.put("pickupLat", shipment.getPickupLat());
                shipmentData.put("pickupLon", shipment.getPickupLon());
                shipmentData.put("destinationLabel", shipment.getDestinationLabel());
                shipmentData.put("destinationLat", shipment.getDestinationLat());
                shipmentData.put("destinationLon", shipment.getDestinationLon());
                shipmentData.put("price", shipment.getPrice());
                shipmentData.put("status", shipment.getShipmentStatus());
                if (user.getRole() == userRole.CUSTOMER)
                  shipmentData.put("driver_email", shipment.getDriver().getEmail());
                else if (user.getRole() == userRole.DRIVER)
                  shipmentData.put("customer_email", shipment.getCreatedBy().getEmail());
                return shipmentData;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving shipments: " + e.getMessage());
        }
    }

    @GetMapping("/shipments/start")
    public ResponseEntity<?> startShipment(@RequestParam Long shipmentId) {
        try {
            Optional<Shipment> optional_shipment = shipmentRepository.findById(shipmentId);

            if (optional_shipment.isPresent()) {
                Shipment shipment = optional_shipment.get();
                shipment.setShipmentStatus(shipmentStatus.ON_THE_WAY);

                double pickupLat = shipment.getPickupLat();
                double pickupLon = shipment.getPickupLon();
                double destinationLat = shipment.getDestinationLat();
                double destinationLon = shipment.getDestinationLon();

                // fetch the route from OSRM API
                String routeUrl = String.format("https://router.project-osrm.org/route/v1/driving/%f,%f;%f,%f?overview=full&geometries=geojson",
                        pickupLon, pickupLat, destinationLon, destinationLat);

                RestTemplate restTemplate = new RestTemplate(); //a Spring class used to make HTTP requests
                ResponseEntity<String> response = restTemplate.exchange(routeUrl, HttpMethod.GET, null, String.class);

                String responseData = response.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(responseData);

                JsonNode routes = rootNode.get("routes");
                if (routes == null || !routes.isArray() || routes.size() == 0) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No routes found in the response!");
                }
                JsonNode geometry = routes.get(0).get("geometry").get("coordinates");
                double distance = routes.get(0).get("distance").asDouble() / 1000;
                final double duration = Math.round(routes.get(0).get("duration").asDouble() / 60 * 100.0) / 100.0; 
                distance = Math.round(distance * 100.0) / 100.0;
                List<List<Double>> coordinates = objectMapper.convertValue(geometry, List.class);

                shipmentThreadsRunning.put(shipmentId, true);
                new Thread(() -> {
                    try {
                        for (int i = 0; i < coordinates.size(); i++) {
                            if (!shipmentThreadsRunning.getOrDefault(shipmentId, false)) { // stop the thread if the flag is false  
                                break;
                            }
                            shipment.setCurrentRouteIndex(i);
                            shipmentRepository.save(shipment);

                            long sleepDuration = (long) ((duration * 60 * 1000) / coordinates.size());
                            Thread.sleep(sleepDuration); // total duration in ms / number of route points
                        }

                        endShipmentLogic(shipmentId);
                    } catch (Exception e) {
                        System.err.println("Error progressing route: " + e.getMessage());
                    } finally {
                        shipmentThreadsRunning.remove(shipmentId); // clean up the flag
                    }
                }).start();

                return ResponseEntity.ok("Shipment started successfully!");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Shipment not found!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error with starting the shipment: " + e.getMessage());
        }
    }

    @GetMapping("/shipments/index")
    public ResponseEntity<?> indexOfShipment(@RequestParam Long shipmentId) {
        try {
            Optional<Shipment> optional_shipment = shipmentRepository.findById(shipmentId);

            if (optional_shipment.isPresent()) {
                Shipment shipment = optional_shipment.get();
                int index = shipment.getCurrentRouteIndex();

                Map<String, Integer> response = new HashMap<>();
                response.put("index", index);

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Shipment not found!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error with extracting the route index from the shipment: " + e.getMessage());
        }
    }

    private void endShipmentLogic(Long shipmentId) {
        try {
            Optional<Shipment> optional_shipment = shipmentRepository.findById(shipmentId);

            if (optional_shipment.isPresent()) {
                Shipment shipment = optional_shipment.get();

                // Signal the thread to stop
                shipmentThreadsRunning.put(shipmentId, false);
                
                String email_driver = shipment.getDriver().getEmail();
                double pickupLat = shipment.getPickupLat();
                double pickupLon = shipment.getPickupLon();
                double destinationLat = shipment.getDestinationLat();
                double destinationLon = shipment.getDestinationLon();
                String pickupLabel = shipment.getPickupLabel();
                String destinationLabel = shipment.getDestinationLabel();

                shipment.setShipmentStatus(shipmentStatus.DELIVERED);
                shipmentRepository.save(shipment);

                Driver driver = driverRepository.findByEmail(email_driver);
                long vehicle_id = driver.getVehicle().getId();
                driver.setAvailable(1);
                driver.setVehicle(null);
                driver.setCurrentPositionLongitude(destinationLon);
                driver.setCurrentPositionLatitude(destinationLat);
                driver.setAddress(destinationLabel);
                driverRepository.save(driver);

                Optional<Vehicle> optional_vehicle = vehicleRepository.findById(vehicle_id);
                Vehicle vehicle = optional_vehicle.get();
                vehicle.setCurrentPositionLongitude(destinationLon);
                vehicle.setCurrentPositionLatitude(destinationLat);
                vehicle.setAddress(destinationLabel);
                vehicle.setAvailable(1);
                vehicleRepository.save(vehicle);
            }
        } catch (Exception e) {
            System.err.println("Error with ending the shipment: " + e.getMessage());
        }
    }

    @GetMapping("/shipments/end")
    public ResponseEntity<?> endShipment(@RequestParam Long shipmentId) {
        try {
            endShipmentLogic(shipmentId);
            return ResponseEntity.ok("Shipment ended successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error with ending the shipment: " + e.getMessage());
        }
    }
}