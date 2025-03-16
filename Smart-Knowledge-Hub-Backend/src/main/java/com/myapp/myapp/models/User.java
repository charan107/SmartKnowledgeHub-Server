package com.myapp.myapp.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;

@Document(collection = "user")  // MongoDB collection name
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
        @Id  // MongoDB uses String ID (ObjectId)
private ObjectId id;

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
