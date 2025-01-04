# Short description of the milestone #

## Update of the method to calculate the distance and duration
In order to calculate an exact road distance and duration for the shipment, the method to calculate the distance between 2 addresses was implemented in a new way (not using Haversine formula, since this formula calculates the airline distance between 2 addresses, not the road distance). Instead, we use OSRM (Open Source Routing Machine) API to get the exact distance and duration that it takes for a road vehicle to get from a place to another.

A new class RouteInfo is created that contains the instance variables distance and duration. So everytime the distance or duration between 2 addresses needs to be calculated, the method getRouteInfo is called that returns an object of type RouteInfo. Then the distance and the duration are extracted from that object.

Since finding a suitable vehicle, driver or calculating the distance between pickup and destination takes more time with this method than using the Haversine formula (as there is a GET request whenever getRouteInfo method is called), we implemented a loading sign after the user has given all details and an offer is being generated.

The total duration that is displayed at the user is calcaulated in a more precise manner, considering that the driver needs to go to the vehicle first (suppose by car), then he has to drive to the pickup address to get the good, and then he has to drive to the destination. Moreover, the driver needs a break during the shipment, whose duration depends on the overall distance that he has to drive. A 30 min extra is added to the total duration to handle other latency issues.

## Storing the shipment in the database
After the user clicks the Accept button, the shipment is stored in the Shipment table with the corresponding coordinates and labels for pickup and destination address, the price, driver's email and customer's email. Moreover shipment status is set to WAITING_FOR_PICKUP while the current_route_index (helps with maintaining the actual driver's position actual in the map) is set to 0. Moreover the availability flags for the selected driver and vehicle are set to 0 (not available)

## Driver's page
A driver's page was added where the corresponding driver can see the details of the assigned shipment. He can click Start to start the shipment and End when he arrives at the destination.

When clicking the Start button, the route is fetched from OSRM API and duration and distance is extracted to calculate an interval of a thread, that is going to update the current driver's position in the front-end. A thread is started to update the current_route_index of the corresponding shipment in the Shipment table. When the customer navigates to Mybookings component in the front end, it is fetched from the Shipment table to get the current_route_index, and the map will show the current position of the shipment together with other shipment details.

When the driver clicks the End button or when the Thread finishes by itsself (no need to press End button in this case), the status of the shipment is set to DELIVERED, the driver and the vehicle are set again as available, the address of the vehicle and driver is set to the destination address of the shipment.

## Plan for next milestone

- Testing and debugging

- Organizing the components and methods

- (support of the fact that the customer can book more than 1 shipment at the same time (see 1 map for each ON_THE_WAY shipment)?)