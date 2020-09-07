package com.obss.jsi.repository;

import com.obss.jsi.model.Know;
import com.obss.jsi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
