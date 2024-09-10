package com.yaroslav.dragontmsbackend.model.user;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDTO toDTO(User user){
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setRoles(user.getRoles());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return  dto;
    }

    public User toEntity(UserDTO userDTO){
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setRoles(userDTO.getRoles());
        user.setEmail(userDTO.getEmail());
        return user;
    }
}
