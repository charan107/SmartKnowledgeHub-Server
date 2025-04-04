package com.myapp.myapp.models;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "userdashboard")

public class UserDashboard {
    @Id
    private String id;
    private String userId;
    private List<String> wishlist;
    private String currentlyReadingId;
    private List<CompletedBooks> completedBooks;
    private List<Borrowing> borrowings; 
}
