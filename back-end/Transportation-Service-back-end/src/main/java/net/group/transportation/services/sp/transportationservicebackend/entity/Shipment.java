package net.group.transportation.services.sp.transportationservicebackend.entity;

import jakarta.persistence.*;
import lombok.*;
import net.group.transportation.services.sp.transportationservicebackend.enums.shipmentStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "shipment")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //@Column(name = "shipmentNumber", nullable = false, updatable = false, unique = true)
    //private Long shipmentNumber;
    @Enumerated(EnumType.STRING)
    @Column(name = "shipmentStatus", nullable = false, updatable = true)
    private shipmentStatus shipmentStatus;
    @ManyToOne
    @JoinColumn(name = "createdBy", referencedColumnName = "email", nullable = false, updatable = false)
    private User createdBy;
    @OneToOne
    @JoinColumn(name = "driver_email", referencedColumnName = "email", nullable = false, updatable = false)
    private User driver;
    //@CreationTimestamp
    //@Column(name = "created_On", nullable = false, updatable = false)
    //private LocalDateTime createdOn;
    //@UpdateTimestamp
    //@Column(name = "updated_On", nullable = false, updatable = true)
    //private LocalDateTime updatedOn;
    @Column(name = "pickupLat", nullable = true, updatable = true)
    private Double pickupLat;
    @Column(name = "pickupLon", nullable = true, updatable = true)
    private Double pickupLon;
    @Column(name = "destinationLat", nullable = true, updatable = true)
    private Double destinationLat;
    @Column(name = "destinationLon", nullable = true, updatable = true)
    private Double destinationLon;
    @Column(name = "pickupLabel", nullable = true, updatable = true)
    private String pickupLabel;
    @Column(name = "destinationLabel", nullable = true, updatable = true)
    private String destinationLabel;
    @Column(name = "price", nullable = false, updatable = true)
    private Double price;
    

}
