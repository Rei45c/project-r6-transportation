package net.group.transportation.services.sp.transportationservicebackend.controllers;

public class RouteInfo {
    private double distance;
    private double duration;

    public RouteInfo(double distance, double duration) {
        this.distance = distance;
        this.duration = duration;
    }

    public double getDistance() {
        return distance;
    }

    public double getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "Distance: " + distance + " km, Duration: " + duration + " hours";
    }
}