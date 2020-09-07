package com.obss.jsi.service;

import com.obss.jsi.model.User;
import com.obss.jsi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Override
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) { return userRepository.findByEmail(email); }

    @Override
    public User loadUser(Authentication authentication) {
        User user;
        if (isGoogleUser(authentication)){
            OAuth2AuthenticationToken oauthToken =
                    (OAuth2AuthenticationToken) authentication;

            String email = getEmail(oauthToken);
            Optional<User> byEmail = findByEmail(email);
            if (!byEmail.isPresent()){
                return null;
            }
            user = byEmail.get();
        } else {
            String username = authentication.getName();
            Optional<User> byUsername = findByUsername(username);
            if (!byUsername.isPresent()){
                return null;
            }
            user = byUsername.get();
        }
        return user;
    }

    @Override
    public boolean isAdmin(Authentication auth){
        return (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMINS")));
    }
    @Override
    public boolean isGoogleUser(Authentication auth) {
        return (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    private String getEmail(OAuth2AuthenticationToken authentication) {
        return authentication.getPrincipal().getAttributes().get("email").toString();
    }

}
