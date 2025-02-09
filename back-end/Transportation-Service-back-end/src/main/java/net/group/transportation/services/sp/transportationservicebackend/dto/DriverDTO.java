package net.group.transportation.services.sp.transportationservicebackend.dto;

import net.group.transportation.services.sp.transportationservicebackend.entity.Vehicle;
import lombok.*;

@Getter
@Setter
public class DriverDTO {

    private String email;
    private String name;
    private String password;
    private String address;
    private Double positionLongitude;
    private Double positionLatitude;
    private int available;
}
