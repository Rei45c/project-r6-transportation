package net.group.transportation.services.sp.transportationservicebackend.entity;
import net.group.transportation.services.sp.transportationservicebackend.enums.userRole;
import net.group.transportation.services.sp.transportationservicebackend.entity.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "driver")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@PrimaryKeyJoinColumn(name = "id") // Links `Driver` to the `User` table
public class Driver extends User{

    @Column(name = "availableDriver", nullable = false, updatable=true)
    private int available;
    @OneToOne
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", nullable = true, unique = true)
    Vehicle vehicle;
    @Column(name = "positionLongitude", nullable = false, updatable = true)
    private Double currentPositionLongitude;
    @Column(name = "positionLatitude", nullable = false, updatable = true)
    private Double currentPositionLatitude;
    @Column(name = "address", nullable = false, updatable = true)
    private String address;
}

// Driver[available, vehicle_id(1to1), positionLongitude, positionLatitude, address]
// User[id, name, email, password, userRole]
// Vehicle[id, driver_id(1to1,if null, means not available), positionLongitude, positionLatitude, address, vehicleType, maxWeight, maxVolume, available]
// Parcel[id, length, width, height, weight, vehicle_id(*to1))]
// Shipment[id, status, price, driver_id, createdBy(*to1), pickuplat, pickuplon, destlat, destlon]