package com.myapp.myapp.Repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.myapp.myapp.models.Section;
@Repository

public interface SectionRepository extends MongoRepository<Section, String> {
    Optional<Section> findByName(String name);
}
 