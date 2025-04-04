package com.myapp.myapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String number;
    private String dob;
    private String password; 
}
