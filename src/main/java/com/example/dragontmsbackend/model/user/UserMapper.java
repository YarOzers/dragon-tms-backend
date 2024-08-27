package com.example.dragontmsbackend.model.user;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDTO toDTO(User user){
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setRole(user.getRole());
        dto.setRights(user.getRights());
        dto.setName(user.getName());
        return  dto;
    }

    public User toEntity(UserDTO userDTO){
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setRole(userDTO.getRole());
        user.setRights(userDTO.getRights());
        return user;
    }
}
