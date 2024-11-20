package net.group.transportation.services.sp.transportationservicebackend.repositories;

import net.group.transportation.services.sp.transportationservicebackend.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
