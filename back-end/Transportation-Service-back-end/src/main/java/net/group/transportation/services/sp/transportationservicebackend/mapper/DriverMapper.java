package net.group.transportation.services.sp.transportationservicebackend.mapper;

import net.group.transportation.services.sp.transportationservicebackend.dto.DriverDTO;
import net.group.transportation.services.sp.transportationservicebackend.entity.Driver;
import org.mapstruct.Mapper;

@Mapper
public interface DriverMapper {

    DriverDTO driverToDriverDTO(Driver driver);
    Driver driverDTOToDriver(DriverDTO driverDTO);


}
