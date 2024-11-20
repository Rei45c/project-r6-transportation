package net.group.transportation.services.sp.transportationservicebackend.entity;

import jakarta.persistence.*;
import lombok.*;

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
    @Column(name = "plateNumber", nullable = true, unique = true, updatable = true)
    private String plateNumber;
    @OneToOne(mappedBy = "vehicle", cascade = CascadeType.ALL)
    Driver driver;
    @Column(name = "positionLongitude", nullable = false, updatable = true)
    private Double currentPositionLongitude;
    @Column(name = "positionLatitude", nullable = false, updatable = true)
    private Double currentPositionLatitude;



}
