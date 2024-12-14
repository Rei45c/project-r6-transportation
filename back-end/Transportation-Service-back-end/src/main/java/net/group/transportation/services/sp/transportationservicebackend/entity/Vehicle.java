package net.group.transportation.services.sp.transportationservicebackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "vehicle")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //@Column(name = "plateNumber", nullable = true, unique = true, updatable = true)
    //private String plateNumber;
    @OneToOne(mappedBy = "vehicle", cascade = CascadeType.ALL)
    Driver driver;
    @Column(name = "positionLongitude", nullable = false, updatable = true)
    private Double currentPositionLongitude;
    @Column(name = "positionLatitude", nullable = false, updatable = true)
    private Double currentPositionLatitude;
    @Column(name = "address", nullable = false, updatable = true)
    private String address;
    //@OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    //List<Parcel> parcelsLoaded;
    @Column(name = "vehicleType", nullable = false, updatable = true)
    private String vehicleType;
    @Column(name = "maxWeight", nullable = false, updatable = true)
    private Double maxWeight;
    @Column(name = "maxVolume", nullable = false, updatable = true)
    private Double maxVolume;
    @Column(name = "availableVehicle", nullable = false, updatable=true)
    private int available;
}

