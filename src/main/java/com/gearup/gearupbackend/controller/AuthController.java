package com.gearup.gearupbackend.controller;

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
    public ResponseEntity<?> loginUser(@RequestParam String username) { // Changed to ? to allow User or String
        try {
            Optional<User> userOptional = userService.getUserByUsername(username);

            if (userOptional.isPresent()) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
                    // RETURN THE USER OBJECT INSTEAD OF A STRING
                    return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Authentication failed", HttpStatus.UNAUTHORIZED);
                }
            } else {
                return new ResponseEntity<>("User not found", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
