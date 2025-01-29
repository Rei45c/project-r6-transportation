[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/yAgQY5gh)
# Milestone repository for Software Praktikum project #

At least the milestone report and the plan for the next milestone should be placed in the milestone folder **before the milestone meeting**.

This project is about a logistics web application, allowing cutomers to make bookings to transport goods from one place to another.

## Installations

The following installation are necessary in the local git folder (front-end/transportation-services-front-end):

-npm install @react-google-maps/api

-npm install react-select react-select-async-paginate

-npm install react-router-dom

-npm install react-hook-form

-npm install axios

-npm install react-leaflet@4.2.1 leaflet

-npm install leaflet.awesome-markers

-npm install socket.io-client

## Creating the database

Create a MySQL schema called "transportation" with the command "CREATE SCHEMA transportation;" and then "use transportation;".
After that, change the database connection properties fin the file ".\back-end\Transportation-Service-back-end\src\main\resources\application.properties".

## Starting the app

Navigate to front-end/transportation-services-front-end to start the front end by "npm start".

Navigate to back-end/Transportation-Service-back-end to start the server by "mvn spring-boot:run".

## Description

The file ".\back-end\Transportation-Service-back-end\src\main\resources\data.sql" is executed to insert a manager in the User table after Hibernate schema creation is performed, as well as insert some drivers and vehicles in some places of Austria.

Use the following credentials to log in as Manager and be able to add new drivers and vehicles:

Manager's email: m.m@gmail.com

Manager's password: m

You can login as a driver or customer based on the email provided. If you login as a customer, you can specify the pickup and destination address, weight and size of the good to be transported. After this an offer will be generated and you as a customer can accept it or not. If accepted, the shipment will start when the corresponding driver click the button "Start" at his webpage when he is ready to start the shipment.

The shipment ends when the average road transportation time ends, or when the driver has arrived before that time in the destination and has pressed the "End" button.