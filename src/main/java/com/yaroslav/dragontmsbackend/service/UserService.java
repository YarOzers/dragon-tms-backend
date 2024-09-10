package com.yaroslav.dragontmsbackend.service;

import com.yaroslav.dragontmsbackend.model.user.UserDTO;
import com.yaroslav.dragontmsbackend.model.user.User;
import com.yaroslav.dragontmsbackend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(UserDTO userDTO) {
        return userRepository.findByEmail(userDTO.getEmail()).orElseGet(()->{
            User user = new User();
            user.setId(userDTO.getId());
            user.setName(userDTO.getName());
            user.setRoles(userDTO.getRoles());
            user.setEmail(userDTO.getEmail());
            return userRepository.save(user);
        });
    }

    public User findById(Long authorId) {
        return this.userRepository.findById(authorId).orElse(null);
    }
}
