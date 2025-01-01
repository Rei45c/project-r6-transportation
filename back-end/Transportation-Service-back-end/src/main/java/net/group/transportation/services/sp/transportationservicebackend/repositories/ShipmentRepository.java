package net.group.transportation.services.sp.transportationservicebackend.repositories;

import net.group.transportation.services.sp.transportationservicebackend.entity.Shipment;
import net.group.transportation.services.sp.transportationservicebackend.entity.User;
import net.group.transportation.services.sp.transportationservicebackend.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    List<Shipment> findByCreatedBy(User createdBy); // 1 customer to * shipments 
    List<Shipment> findByDriver(Driver driver); // is always just one element, since 1 driver to 1 shipment
}
