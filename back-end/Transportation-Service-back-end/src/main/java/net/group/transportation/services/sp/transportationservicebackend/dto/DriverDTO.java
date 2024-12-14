package net.group.transportation.services.sp.transportationservicebackend.dto;

import net.group.transportation.services.sp.transportationservicebackend.entity.Vehicle;
import lombok.*;

@Getter
@Setter
public class DriverDTO {

    //private Long id;
    private String email;
    private String name;
    //private String insuranceNumber;
    //private String phoneNumber;
    //private Vehicle vehicle;
    //private boolean available;
    //private Long driverIdNumber;
    private String password;
    private String address;
    private Double positionLongitude;
    private Double positionLatitude;
    private int available;
}
