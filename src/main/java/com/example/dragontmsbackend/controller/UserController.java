package com.example.dragontmsbackend.controller;


import com.example.dragontmsbackend.model.user.UserDTO;
import com.example.dragontmsbackend.model.user.User;
import com.example.dragontmsbackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO){
        User createdUser = userService.createUser(userDTO);
        return ResponseEntity.ok(createdUser);
    }
}
