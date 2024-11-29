# Short description of the milestone #

A basic prototype was created exchanging some info between a basic front end, back end and database.

A login form and signup form was set up to keep track of users, which can be customers or drivers. During sign up, the email and password
of the customer are passed to the backend, which creates a new entry in the user table with role=customer.

Here are the tables that were created:
User[id, email, password, user_role(CUSTOMER, DRIVER)]

Driver[id, name, email(FK), status, current_location]

Vehicle[id, type, max_weight, max_volume, status, current_location, driver_id (FK)]

Order[id, user_id (FK), vehicle_id(FK), driver_id(FK), status]

During log in, the email is searched in the table "user" and if found, the password is compared to verify the user. If he is verfied and has the role 'customer', he is redirected to the customer site (specifying the details of the shipment), if it is a driver, we plan to redirect him to a driver site, which will contain information about shipments that are assigned to that driver. 

The user then specifies the pick up and destination address (autocomplete of addresses via OpenStreetMap: https://nominatim.org/release-docs/develop/api/Overview/). Then he specifies the size and weight of the product and clicks submit. After that the details (pickup and destination addresses, weight, size of the product) will be further processed in the backend to calculate the costs and display an offer for the customer in the next milestone.

## Plan for next milestone
Calculate the costs and display an offer to the user. Assign a driver. Add some sort of navigation. 