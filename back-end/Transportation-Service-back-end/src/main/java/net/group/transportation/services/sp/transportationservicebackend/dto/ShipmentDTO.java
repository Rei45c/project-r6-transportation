package net.group.transportation.services.sp.transportationservicebackend.dto;

import net.group.transportation.services.sp.transportationservicebackend.entity.User;
import net.group.transportation.services.sp.transportationservicebackend.enums.shipmentStatus;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

public class ShipmentDTO {

    private Long id;
    private Long shipmentNumber;
    private shipmentStatus shipmentStatus;
    private LocalDateTime updatedOn;
    private LocalDateTime createdOn;
    private User createdBy;
    private Double longitude;
    private Double latitude;

}
