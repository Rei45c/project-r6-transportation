package net.group.transportation.services.sp.transportationservicebackend.repositories;

import net.group.transportation.services.sp.transportationservicebackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
