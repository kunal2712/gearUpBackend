package com.gearup.gearupbackend.service;

import com.gearup.gearupbackend.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public interface UserService {

    User registerUser(User user);
    Optional<User> getUserById(long id);

    Optional<User> getUserByUsername(String username);

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
