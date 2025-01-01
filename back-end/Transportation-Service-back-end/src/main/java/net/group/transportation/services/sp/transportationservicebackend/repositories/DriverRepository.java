package net.group.transportation.services.sp.transportationservicebackend.repositories;

import net.group.transportation.services.sp.transportationservicebackend.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    Driver findByEmail(String email);
}
