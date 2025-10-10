package com.sicuro.solution.userproject.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor 
public class AddUserResponse {
 private String status;
 private Integer userId;
 private String message; 
 
 public AddUserResponse(String status, Integer userId) {
     this.status = status;
     this.userId = userId;
     this.message = null;
 }
}