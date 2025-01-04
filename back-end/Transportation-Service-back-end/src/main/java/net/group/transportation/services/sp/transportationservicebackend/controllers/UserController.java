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
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private ShipmentRepository shipmentRepository;

    private final Map<Long, Boolean> shipmentThreadsRunning = new ConcurrentHashMap<>();

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.ok("Email already exists");
        }
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/registerDriver")
    public ResponseEntity<String> register_driver(@RequestBody DriverDTO driverDTO) {
        if (userRepository.findByEmail(driverDTO.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email of the driver already exists");
        }

        Driver driver = new Driver();
        driver.setName(driverDTO.getName());
        driver.setEmail(driverDTO.getEmail());
        driver.setPassword(driverDTO.getPassword());
        driver.setRole(userRole.DRIVER);
        driver.setVehicle(null); // no vehicle assigned initially
        driver.setCurrentPositionLongitude(driverDTO.getPositionLongitude());
        driver.setCurrentPositionLatitude(driverDTO.getPositionLatitude());
        driver.setAddress(driverDTO.getAddress());
        driver.setAvailable(driverDTO.getAvailable());
        //System.out.println("\n\n name:" +driver.getName() +"address:"+driver.getAddress()+"avail:"+driver.getAvailable()+" \n\n");

        driverRepository.save(driver);

        return ResponseEntity.ok("Driver registered successfully");
    }

    @PostMapping("/registerVehicle")
    public ResponseEntity<String> register_vehicle(@RequestBody Vehicle vehicle) {

        Vehicle driver = new Vehicle();
        vehicle.setDriver(null);

        vehicleRepository.save(vehicle);

        return ResponseEntity.ok("Vehicle registered successfully");
    }

    @DeleteMapping("/deleteDriver/{id}")
    public ResponseEntity<String> deleteDriver(@PathVariable Long id) {
        Optional<Driver> driverOptional = driverRepository.findById(id);

        if (driverOptional.isPresent()) {
            driverRepository.deleteById(id);
            return ResponseEntity.ok("Driver deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Driver not found");
        }
    }

    @DeleteMapping("/deleteVehicle/{id}")
    public ResponseEntity<String> deleteVehicle(@PathVariable Long id) {
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(id);

        if (vehicleOptional.isPresent()) {
            vehicleRepository.deleteById(id);
            return ResponseEntity.ok("Vehicle deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle not found");
        }
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

            RouteInfo route_pickup_destination = getRouteInfo(pickupLat, pickupLon, destinationLat, destinationLon);
            double distance_pickup_destination = route_pickup_destination.getDistance();
            double duration_pickup_destination = route_pickup_destination.getDuration();

            double volume = length * width * height;

            Vehicle selectedVehicle = findSuitableVehicle(pickupLat, pickupLon, weight, volume);
            Driver selectedDriver = findSuitableDriver(selectedVehicle.getCurrentPositionLatitude(), selectedVehicle.getCurrentPositionLongitude());
            if (selectedVehicle == null || selectedDriver == null) {
                return ResponseEntity.ok().body("No suitable vehicle or driver available.");
            }

            RouteInfo route_vehicle_pickup = getRouteInfo(selectedVehicle.getCurrentPositionLatitude(), selectedVehicle.getCurrentPositionLongitude(), pickupLat, pickupLon);
            double distance_vehicle_pickup = route_vehicle_pickup.getDistance();
            double duration_vehicle_pickup = route_vehicle_pickup.getDuration();

            RouteInfo route_driver_vehicle = getRouteInfo(selectedDriver.getCurrentPositionLatitude(), selectedDriver.getCurrentPositionLongitude(), selectedVehicle.getCurrentPositionLatitude(), selectedVehicle.getCurrentPositionLongitude());
            double duration_driver_vehicle = route_driver_vehicle.getDuration();

            double total_distance = distance_vehicle_pickup + distance_pickup_destination;
            double cost = Math.round(calculateCost(total_distance, weight, volume) * 100.0) / 100.0;
            //System.out.println("\n\n Vehicle-->addr:"+selectedVehicle.getCurrentPositionLatitude()+", type: "+selectedVehicle.getVehicleType()+", maxweight: "+selectedVehicle.getMaxWeight()+", maxVolume: "+selectedVehicle.getMaxVolume()+"\nDriver-->id: "+selectedDriver.getId()+", addr: "+selectedDriver.getAddress()+" \n\n");

            // duration_driver_vehicle --> time that it takes for the driver to go to the parking address of the vehicle (supposed the driver uses its own car)
            // duration_vehicle_pickup --> time for the driver to go with the company's vehicle from parking address to the pickup address
            // duration_pickup_destination --> time from pickup to destination address
            // 0.0031*total_distance --> break time considering how long the route is
            // +0.5 --> 30 min for other latency reasons
            double total_duration = duration_driver_vehicle + duration_vehicle_pickup + duration_pickup_destination + 0.0031*total_distance + 0.5;
            total_duration = Math.round(total_duration * 100.0) / 100.0;
            
            Map<String, Object> response = Map.of(
                "email_driver", selectedDriver.getEmail(),
                "address_vehicle", selectedVehicle.getAddress(),
                "vehicle_id", selectedVehicle.getId(),
                "cost", cost,
                "duration", total_duration
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // exceptions such as missing fields or invalid data
            return ResponseEntity.badRequest().body("Invalid request: " + e.getMessage());
        }
    }
    
    @PostMapping("/accepted")
    public ResponseEntity<?> accepted(@RequestBody Map<String, Object> info) {
        try {
            // Extract fields from the request
            String email_driver = (String) info.get("email_driver");
            String email_customer = (String) info.get("email_customer");
            int vehicle_id = (int) info.get("vehicle_id");
            Map<String, Object> pickup = (Map<String, Object>) info.get("pickup");
            Map<String, Object> destination = (Map<String, Object>) info.get("destination");
            double price = Double.parseDouble(info.get("price").toString());

            // Extract coordinates from pickup and destination
            String pickupLabel = (String) pickup.get("label").toString();
            double pickupLat = Double.parseDouble(pickup.get("lat").toString());
            double pickupLon = Double.parseDouble(pickup.get("lon").toString());
            String destinationLabel = (String) destination.get("label").toString();
            double destinationLat = Double.parseDouble(destination.get("lat").toString());
            double destinationLon = Double.parseDouble(destination.get("lon").toString());

            Driver driver = driverRepository.findByEmail(email_driver);
            Vehicle vehicle = vehicleRepository.findById(vehicle_id);
            if (driver != null && vehicle!=null) {
                driver.setAvailable(0);
                driver.setVehicle(vehicle);
                vehicle.setAvailable(0);
                driverRepository.save(driver);
                vehicleRepository.save(vehicle);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Driver not found with email: " + email_driver);
            }

            // insert into shipment
            Shipment shipment = new Shipment();
            shipment.setShipmentStatus(shipmentStatus.WAITING_FOR_PICKUP);

            User customer = userRepository.findByEmail(email_customer).get();
            shipment.setCreatedBy(customer);
            shipment.setDriver(driver);
            shipment.setPickupLat(pickupLat);
            shipment.setPickupLon(pickupLon);
            shipment.setDestinationLat(destinationLat);
            shipment.setDestinationLon(destinationLon);
            shipment.setPickupLabel(pickupLabel);
            shipment.setDestinationLabel(destinationLabel);
            shipment.setPrice(price);
            shipment.setCurrentRouteIndex(0);

            shipmentRepository.save(shipment);

            //System.out.println("\n\n email_driver:"+email_driver+", email_customer: "+email_customer+", price: "+price+"\npickupLat: "+pickupLat+", pickuplabel: "+pickupLabel+", destLabel: "+destinationLabel+" \n\n");
            //System.out.println("vehicle_id: "+vehicle_id+"\n\n");

            return ResponseEntity.ok("Offer accepted successfully!");

        } catch (Exception e) {
            // exceptions such as missing fields or invalid data
            return ResponseEntity.badRequest().body("Invalid request: " + e.getMessage());
        }
    }

    @GetMapping("/shipments")
    public ResponseEntity<?> getShipmentsByCustomerEmail(@RequestParam String email) {
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

                RestTemplate restTemplate = new RestTemplate();
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

    // (not used here) calculates the exact airline distance, using Haversine formula
    private double calculateDistance_haversine(double lat1, double lon1, double lat2, double lon2) {
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

    // calculates the exact road distance
    private RouteInfo getRouteInfo(double lat1, double lon1, double lat2, double lon2) {
        String pickupCoords = lon1 + "," + lat1;
        String destinationCoords = lon2 + "," + lat2;
        try{
            // Fetch the route data from the OSRM API
            String apiUrl = "https://router.project-osrm.org/route/v1/driving/" + pickupCoords + ";" + destinationCoords + "?overview=full&geometries=geojson";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET"); // send a GET request to the OSRM API
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine); // accumulate the JSON response from the API line by line into a StringBuffer.
            }
            in.close();

            // Parse the JSON response and extract the distance
            JSONObject jsonResponse = new JSONObject(response.toString());
            double distance = jsonResponse.getJSONArray("routes").getJSONObject(0).getDouble("distance");
            double duration = jsonResponse.getJSONArray("routes").getJSONObject(0).getDouble("duration");

            return new RouteInfo(distance / 1000, duration / 3600); // distance in km, duration in hours
        } catch (Exception e) {
            return null;
        }
    }

    private double calculateCost(double distance, double weight, double volume) {
        double baseRate = 0.7; // base rate per km
        double weightRate = 0.05; // weight rate per kg
        double volumeRate = 20; // volume rate per cubic m
        return (baseRate * distance) + (weightRate * weight) + (volumeRate * volume);
    }

    private Vehicle findSuitableVehicle(double pickupLat, double pickupLon, double weight, double volume) {
        double predefinedDistance = 50.0; // radius in kilometers
        List<Vehicle> allVehicles = vehicleRepository.findAll();

        List<Vehicle> nearbyVehicles = new ArrayList<>();
        Vehicle bestVehicle_within_zone = null;
        Vehicle bestVehicle_overall = null;

        double minDistance_overall = Double.MAX_VALUE;
        for (Vehicle vehicle : allVehicles) {
            RouteInfo route_vehicle_pickup = getRouteInfo(pickupLat, pickupLon, vehicle.getCurrentPositionLatitude(), vehicle.getCurrentPositionLongitude());
            double distance = route_vehicle_pickup.getDistance();
            // find all vehicles within the predefined distance
            if (distance <= predefinedDistance) {
                nearbyVehicles.add(vehicle);
            }
            // find the nearest vehicle within the range of volume and weight, without considering the nearby zone
            if (distance < minDistance_overall && vehicle.getMaxVolume() >= volume && vehicle.getMaxWeight() >= weight && vehicle.getAvailable()==1) {
                minDistance_overall = distance;
                bestVehicle_overall = vehicle;
            }
        }

        // from nearby vehicles, find the best vehicle meeting volume and weight conditions
        for (Vehicle vehicle : nearbyVehicles) {
            if (vehicle.getMaxVolume() >= volume && vehicle.getMaxWeight() >= weight && vehicle.getAvailable()==1) {
                if (bestVehicle_within_zone == null || vehicle.getMaxVolume() < bestVehicle_within_zone.getMaxVolume()) {
                    bestVehicle_within_zone = vehicle; // because it could be just a small product assigned to a very big vehicle
                }
            }
        }

        if (bestVehicle_within_zone == null)
            return bestVehicle_overall;
        else
            return bestVehicle_within_zone;
    }

    private Driver findSuitableDriver(double vehicleLat, double vehicleLon) {
        List<Driver> allDrivers = driverRepository.findAll();

        Driver nearest_driver = null;
        double minDistance = Double.MAX_VALUE;
        for (Driver driver: allDrivers) {
            RouteInfo route_driver_vehicle = getRouteInfo(vehicleLat, vehicleLon, driver.getCurrentPositionLatitude(), driver.getCurrentPositionLongitude());
            double distance = route_driver_vehicle.getDistance();

            if (distance < minDistance && driver.getAvailable()==1) {
                minDistance = distance;
                nearest_driver = driver;
            }
        }

        return nearest_driver;
    }
}