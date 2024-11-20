package net.group.transportation.services.sp.transportationservicebackend.mapper;

import net.group.transportation.services.sp.transportationservicebackend.dto.UserDTO;
import net.group.transportation.services.sp.transportationservicebackend.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    User userDTOToUser(UserDTO userDTO);
    UserDTO userToUserDTO(User user);

}
