package com.myapp.myapp.Repository;

import com.myapp.myapp.models.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, String> {
    List<Book> findAllById(Iterable<String> ids); // Fetch multiple books by their IDs
}
