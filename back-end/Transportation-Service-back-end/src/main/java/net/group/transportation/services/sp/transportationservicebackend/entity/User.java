package net.group.transportation.services.sp.transportationservicebackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import net.group.transportation.services.sp.transportationservicebackend.enums.userRole;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "user")
@Getter
@Setter
@SuperBuilder // to provide inheritance
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED) //Separate tables for the base class and subclasses, joined by the primary key
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name", nullable = false, updatable = false)
    private String name;
    @Column(name = "password", nullable = false, updatable = true)
    private String password;
    @Email
    @Column(name = "email", updatable = true, nullable = false, unique = false)
    private String email;
    @Enumerated(EnumType.STRING)
    @Column(name = "userRole", nullable = false, updatable = true)
    private userRole role;
}
