package com.myapp.myapp.Repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.myapp.myapp.models.UserDashboard;

public interface UserDashboardRepository extends MongoRepository<UserDashboard,String> {
    Optional<UserDashboard> findByUserId(String userId);
}
