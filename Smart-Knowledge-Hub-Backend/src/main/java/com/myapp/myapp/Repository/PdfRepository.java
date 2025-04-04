package com.myapp.myapp.Repository;

import com.myapp.myapp.models.Pdf;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface PdfRepository extends MongoRepository<Pdf, String> {
    Optional<Pdf> findByBookId(String bookId);
}
