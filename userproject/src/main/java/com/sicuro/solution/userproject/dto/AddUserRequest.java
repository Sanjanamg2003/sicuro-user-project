package com.sicuro.solution.userproject.dto;

import lombok.Data;

@Data 
public class AddUserRequest {
 private String username;
 private String firstName;
 private String lastName;
 private String email;
 private String password;
}
