package com.obss.jsi.service;

import com.obss.jsi.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.util.Optional;

public interface UserService {


    Optional<User> findById(int id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    User loadUser(Authentication authentication);

    boolean isAdmin(Authentication auth);

    boolean isGoogleUser(Authentication auth);

}
