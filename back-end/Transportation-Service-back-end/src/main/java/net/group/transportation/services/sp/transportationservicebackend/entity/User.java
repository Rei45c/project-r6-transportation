package net.group.transportation.services.sp.transportationservicebackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import net.group.transportation.services.sp.transportationservicebackend.enums.userRole;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    // @Column(name = "username", nullable = false, unique = true, length = 100, updatable = false)
    // private String username;
    @Column(name = "password", nullable = false, updatable = true)
    private String password;
    // @CreationTimestamp
    // @Column(name = "createdOn")
    // private LocalDateTime createdOn;
    // @Column(name = "emailVerified", nullable = false)
    // private boolean emailVerified;
    @Email
    @Column(name = "email", updatable = true, nullable = false, unique = true)
    private String email;
    // @Column(name = "phoneNumber", nullable = true, updatable = true, unique = true)
    // private String phoneNumber;
    @Enumerated(EnumType.STRING)
    @Column(name = "userRole", nullable = false, updatable = true)
    private userRole role;
    // @OneToMany(mappedBy = "createdBy")
    // List<Shipment> shipments;
}
