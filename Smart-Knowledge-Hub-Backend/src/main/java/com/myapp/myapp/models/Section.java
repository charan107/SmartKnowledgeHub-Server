package com.myapp.myapp.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;

import java.util.List;
import java.time.LocalDateTime;

@Document(collection = "sections") // MongoDB Collection Name
@Data  // Lombok annotation to generate getters, setters, equals, hashCode, and toString
@NoArgsConstructor  // Generates a no-args constructor
@AllArgsConstructor // Generates a constructor with all fields
public class Section {
    @Id
    private String id;
    private String name;
    private List<String> books;   
    private LocalDateTime updatedAt = LocalDateTime.now(); // Automatically set timestamp  

    private List<ObjectId> bookIds;
    private List<Book> bookObjects; 
    public List<String> getBooksId(){
        return books;
    }
}
