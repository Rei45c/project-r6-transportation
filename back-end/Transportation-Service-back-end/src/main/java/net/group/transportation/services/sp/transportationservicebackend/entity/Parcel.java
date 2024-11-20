package net.group.transportation.services.sp.transportationservicebackend.entity;

import jakarta.persistence.*;

@Entity
public class Parcel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "length", nullable = false)
    private Double length;
    @Column(name = "width", nullable = false)
    private Double width;
    @Column(name = "weight", nullable = false)
    private Double weight;
    @ManyToOne
            @JoinColumn(name = "vehicle_id", nullable = true)
    Vehicle vehicle;

}
