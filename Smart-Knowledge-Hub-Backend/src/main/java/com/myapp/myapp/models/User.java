package com.myapp.myapp.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;

@Document(collection = "user")  
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id  
    private String id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String number;
    private String dob;
    private String password;
    private String confirmpassword;

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
    
}
