package net.group.transportation.services.sp.transportationservicebackend.dto;

import net.group.transportation.services.sp.transportationservicebackend.entity.Shipment;
import net.group.transportation.services.sp.transportationservicebackend.enums.userRole;

import java.time.LocalDateTime;
import java.util.List;

public class UserDTO {


    private Long id;
    private String password;
    private String email;
    private String username;
    private List<Shipment> shipments;
    private LocalDateTime createdOn;
    private boolean emailVerified;
    private String phoneNumber;
    private userRole role;



}
