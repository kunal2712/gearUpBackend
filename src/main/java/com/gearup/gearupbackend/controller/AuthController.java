package com.gearup.gearupbackend.controller;

import com.gearup.gearupbackend.model.JwtAuthResponse;
import com.gearup.gearupbackend.model.LoginRequest;
import com.gearup.gearupbackend.model.User;
import com.gearup.gearupbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/gearup/auth")
public class AuthController {

    @Autowired
    private final UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user){
        if(user.getUsername() == null || user.getPassword() == null || user.getEmail() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User registeredUser = userService.registerUser(user);
        return new ResponseEntity<>(registeredUser  , HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> loginUser(@RequestBody LoginRequest request) {
        try {
            // 1. Get the token from your service
            String token = userService.login(request);

            User user = userService.getUserByUsername(request.getUsername())
                    .orElseThrow(()-> new RuntimeException("User not found."));

            JwtAuthResponse response = new JwtAuthResponse();
            response.setAccessToken(token);
            response.setTokenType("Bearer");
            response.setId(user.getId());

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // Improved error response: body is more helpful than just null headers
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
