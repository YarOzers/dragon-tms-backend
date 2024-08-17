package com.example.dragontmsbackend.service;

import com.example.dragontmsbackend.dto.UserDTO;
import com.example.dragontmsbackend.model.user.User;
import com.example.dragontmsbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setRole(userDTO.getRole());
        user.setRights(userDTO.getRights());

        return userRepository.save(user);
    }
}
