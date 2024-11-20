package net.group.transportation.services.sp.transportationservicebackend.mapper;

import net.group.transportation.services.sp.transportationservicebackend.dto.VehicleDTO;
import net.group.transportation.services.sp.transportationservicebackend.entity.Vehicle;
import org.mapstruct.Mapper;

@Mapper
public interface VehicleMapper {

    VehicleDTO vehicleToVehicleDTO(Vehicle vehicle);
    Vehicle vehicleDTOToVehicle(VehicleDTO vehicleDTO);



}
