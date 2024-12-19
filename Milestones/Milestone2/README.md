# Short description of the milestone #

Firstly, a manager page was added, which is responsible for adding (or removing) vehicles and drivers of the company with some characteristics for each of them, for example maxWeight, maxVolume, type for vehicles, and name, email for drivers. Also the manager gives the current address of the vehicle or driver using the autocomplete help, and the coordinates and plain name of the address together with other specifications are stored in the corresponding tables in the database. A Driver is a subclass of User, so when a driver is created and inserted in Driver table, it is also inserted in the User table. The manager is created automatically during running the maven backend via the query in the file "\back-end\Transportation-Service-back-end\src\main\resources\data.sql" so its credintials give access to manager page.

Cost calculation: after the user specifies the pickup and destination address (each of them is transmetted as an object with a label, a latitude and longitude coordinate), weight, dimensions of the good, they are sent to the backend.

The distance between pickip and destination address \(d\) is calculated using [Haversine formula](https://en.wikipedia.org/wiki/Haversine_formula):

d = 2r * arcsin(sqrt( (1 - cos(Δφ) + cos(φ1) * cos(φ2) * (1 - cos(Δλ))) / 2 ))

Where:
- r: Earth's radius (approx. 6371 km)
- φ1, φ2: Latitudes of the two points (in radians)
- λ1, λ2: Longitudes of the two points (in radians)
- Δφ: φ2 - φ1
- Δλ: λ2 - λ1

Chosement of the vehicle: firstly it is searched within a fixed distance of 50 km from pickup address, to find the vehicle with the smallest volume among other vehicles in this zone (weight, volume and availabilty constraints should of course be considered), in order to avoid the fact that very large vehicles may be assigned to very small goods. If no proper vehicle is found in this zone, then it is searched for all possible vehicles (even outside the zone) and the vehicle which is the nearest to pickup location which satisfies the weight, volume and availability constraints is chosen.

Chosement of the driver: the driver which is nearest to the chosen vehicle is chosen, if he is also available.

The cost calculation is done simply by multiplying the total distance (distance_between_vehicle_and_pickup + distance_between_pickup_and_destination) by 0.7, the weight by 0.05 and the volume by 0.4; and at the end adding all together to habe the total cost of the shipment. The duration is also calculated by dividing the total distance by the average speed (80 km/h), also +2 hours considering the time the driver needs to get to the vehicle and other latency reasons during the shipment.

Email of the driver, cost and duration are passed in the front end to the final offer.

## Plan for next milestone
- (support for user payment via card details?)

- (support for user specifying more packets with different weight or volume, so a vehicle can have more packets?)

- after the customer click Accept button, store the offer details in the Shipment table, update the status of the vehicle and driver

- add some sort of map integration, for example by creating a driver site, where he can click "start" when he starts to drive, "end" when he arrives and integrate with database accordingly