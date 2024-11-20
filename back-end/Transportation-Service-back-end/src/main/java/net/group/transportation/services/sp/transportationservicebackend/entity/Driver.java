package net.group.transportation.services.sp.transportationservicebackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

@Entity
@Table(name = "driver")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "driverNumber", nullable = false, updatable = false, unique = true)
    private Long driverIdNumber;
    @Column(name = "available", nullable = false)
    private boolean available;
    @Column(name = "firstName", nullable = false, updatable = false)
    private String firstName;
    @Column(name = "lastName", nullable = false, updatable = true)
    private String lastName;
    @Column(name = "insuranceNumber", nullable = false, updatable = false)
    private String insuranceNumber;
    @Email
    @Column(name = "email", nullable = false, updatable = true, unique = true)
    private String email;
    @Column(name = "phoneNumber", nullable = false, updatable = true, unique = true)
    private String phoneNumber;
    @OneToOne
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", nullable = true, unique = true)
    Vehicle vehicle;


}
