# Short description of the milestone #

## Update of the method to calculate the distance and duration
In order to calculate an exact road distance and duration for the shipment, the method to calculate the distance between 2 addresses was implemented in a new way (not using Haversine formula, since this formula calculates the airline distance between 2 addresses, not the road distance). Instead, we use OSRM (Open Source Routing Machine) API to get the exact distance and duration that it takes for a road vehicle to get from a place to another.

A new class RouteInfo is created that contains the instance variables distance and duration. So everytime the distance or duration between 2 addresses needs to be calculated, the method getRouteInfo is called that returns an object of type RouteInfo. Then the distance and the duration are extracted from that object.

Since finding a suitable vehicle, driver or calculating the distance between pickup and destination takes more time with this method than using the Haversine formula (as there is a GET request whenever getRouteInfo method is called), we implemented a loading sign after the user has given all details and an offer is being generated.

The total duration that is displayed at the user is calcaulated in a more precise manner, considering that the driver needs to go to the vehicle first (suppose by car), then he has to drive to the pickup address to get the good, and then he has to drive to the destination. Moreover, the driver needs a break during the shipment, whose duration depends on the overall distance that he has to drive. A 30 min extra is added to the total duration to handle other latency issues.

