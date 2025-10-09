package com.sicuro.solution.userproject.repository;

import com.sicuro.solution.userproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    //username validation
    boolean existsByUsername(String username); 
    //unique email validation
    boolean existsByEmail(String email);
}
