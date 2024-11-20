package net.group.transportation.services.sp.transportationservicebackend.repositories;

import net.group.transportation.services.sp.transportationservicebackend.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
}
