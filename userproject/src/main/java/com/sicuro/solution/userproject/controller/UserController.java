package com.sicuro.solution.userproject.controller;

import com.sicuro.solution.userproject.dto.AddUserRequest;
import com.sicuro.solution.userproject.dto.AddUserResponse;
import com.sicuro.solution.userproject.model.User;
import com.sicuro.solution.userproject.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.List;

@PostMapping("/add")
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

 private final UserRepository userRepository;
 private final PasswordEncoder passwordEncoder;

 public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
     this.userRepository = userRepository;
     this.passwordEncoder = passwordEncoder;
 }

 @PostMapping("/add")
 public ResponseEntity<AddUserResponse> addUser(@RequestBody AddUserRequest request) {
     
     if (userRepository.existsByUsername(request.getUsername())) {
         return new ResponseEntity<>(
             new AddUserResponse("Failure", 0, "Username already exists"), 
             HttpStatus.CONFLICT);
     }

     User newUser = new User();
     newUser.setUsername(request.getUsername());
     newUser.setFirstName(request.getFirstName());
     newUser.setLastName(request.getLastName());
     newUser.setEmail(request.getEmail());
     
     String encodedPassword = passwordEncoder.encode(request.getPassword());
     newUser.setPasswordHash(encodedPassword); 
     
     try {
         User savedUser = userRepository.save(newUser);
         
         return new ResponseEntity<>(
             new AddUserResponse("User added successfully", savedUser.getId()), 
             HttpStatus.CREATED); 
         
     } catch (Exception e) {
         return new ResponseEntity<>(
             new AddUserResponse("Failure", 0, "Could not save user to database"), 
             HttpStatus.INTERNAL_SERVER_ERROR); // 500
     }
 }
 @PostMapping("/query")
 public ResponseEntity<List<Map<String, Object>>> queryUsers(@RequestBody Map<String, String> searchCriteria) {
     
     List<User> allUsers = userRepository.findAll();
     
     List<Map<String, Object>> filteredUsers = allUsers.stream()
         .filter(user -> matchesCriteria(user, searchCriteria))
         .map(user -> {
             // Map to JSON format, excluding the password hash
             Map<String, Object> userMap = new HashMap<>();
             userMap.put("id", user.getId());
             userMap.put("username", user.getUsername());
             userMap.put("firstName", user.getFirstName());
             userMap.put("lastName", user.getLastName());
             userMap.put("email", user.getEmail());
             return userMap;
         })
         .collect(Collectors.toList());

     return ResponseEntity.ok(filteredUsers);
 }
 
 private boolean matchesCriteria(User user, Map<String, String> criteria) {
     if (criteria == null || criteria.isEmpty()) {
         return true; 
     }

     String id = criteria.get("id");
     String firstName = criteria.get("firstName");
     String lastName = criteria.get("lastName");
     String email = criteria.get("email");
     String username = criteria.get("username");
     
     if (id != null && !user.getId().toString().equals(id)) return false;
     if (firstName != null && !user.getFirstName().toLowerCase().contains(firstName.toLowerCase())) return false;
     if (lastName != null && !user.getLastName().toLowerCase().contains(lastName.toLowerCase())) return false;
     if (email != null && !user.getEmail().toLowerCase().contains(email.toLowerCase())) return false;
     if (username != null && !user.getUsername().toLowerCase().contains(username.toLowerCase())) return false;
     
     return true;
 }
}