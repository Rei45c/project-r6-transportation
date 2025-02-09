package net.group.transportation.services.sp.transportationservicebackend.dto;

import jakarta.persistence.OneToOne;
import lombok.*;
import net.group.transportation.services.sp.transportationservicebackend.entity.Driver;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDTO {

    private Long id;
    private String plateNumber;
    private Driver driver;
    private Double currentPositionLatitude;
    private Double currentPositionLongitude;

}
