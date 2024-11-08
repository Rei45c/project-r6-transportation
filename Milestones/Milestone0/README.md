# Short description of the milestone #

## Background
There is a web app to be developed (client-server architecture), which provides transportation services for goods in a geographical area (similar to 
a logistics company). The company has a fleet of vehicles with various characteristics and park in different places. 
A transportation service means moving goods from one place to another. A customer requests the service,
the server calculates the price and generates automatically an offer to the customer.
After the customer confirms an offer, the server issues moving orders to vehicles and drivers.

## Requirements
The app has 3 types of users: driver, customer, manager.

The manager panel is used to register the drivers and vehicles of the company. Drivers and vehicles are stored in two tables
in the database as Driver[name, email, address, availability] and Vehicle[type, max_volume, max_weight, park_address, availability]. 

The driver panel is used to login as a driver. In this panel the driver must update his data constantly (such as current address or
availability). The driver can see all his assigned bookings that he is responsible for. He has some buttons there with which he can
update the status of the transportation (start of transportation, destination reached), which will be used to update in real time the 
tracking map in the customer panel.

The cusotmer panel is used to make booking from customers. The customers logins with his email and password (registers if it is the first time,
and his data is stored in the database under Customer[email, password]). Then he choses the exact pickup and destination address through linking via Google Maps APIs. 
He further choses the type of goods to be transported (furniture, general 
articles, building materials, pharmaceuticals, food, vehicle equipment, explosive substances...) and in this category that he chose, he
can order more than 1 package (to allow different features for each package, for example size, weight, quantity). If the product is 
not already packaged, the user can chose one from different types of packaging. He can also chose if the product needs special
handling requirements such as fragile items, cooled temperature etc. The cost will be calculated taking the weight, volume, special requirements,
the distance between pickup and delievery address. Then the customer choses an offer received
from the server, gives contact information of the receiver in the delievery point and also bank details for the 
payment. After that the booking is confirmed and a driver will be assigned to the vehicle, whose park location(city, not exact park location is stored in the database)
is nearer to the pickup location city than other available vehicles.

For a specific look, see this mockup: https://lun-eu.icons8.com/d/7g5OgMXDDkS5I20Q48KoIA?page=7w5OgMXDDkS5I20Q48KoIA&vp=-2453,-2600,7774,3972 

Tools that will be used:

-front end: React

-back end: Spring framework

-database: PostgreSQL

## Plan for next milestone
Have a basic prototype working and exchanging info between a basic front end, back end and database