package com.myapp.myapp.Service;

import com.myapp.myapp.models.User;
import com.myapp.myapp.models.UserDashboard;
import com.myapp.myapp.security.JwtUtil;

import io.jsonwebtoken.lang.Collections;

import com.myapp.myapp.Repository.UserDashboardRepository;
import com.myapp.myapp.Repository.UserRepository;
import com.myapp.myapp.dto.UserUpdateRequest;
import com.myapp.myapp.Exceptions.UsernameAlreadyExistsException;
import com.myapp.myapp.Exceptions.EmailAlreadyExistsException;
import com.myapp.myapp.Exceptions.UserNotFoundException;
import com.myapp.myapp.Exceptions.IncorrectPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserDashboardRepository userDashboardRepository;
    @Autowired  
    public UserService(UserRepository userRepository,UserDashboardRepository userDashboardRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();  // Secure password encoding
        this.userDashboardRepository = userDashboardRepository;
        this.jwtUtil = jwtUtil;
    }

    // ✅ Register user with hashed password
    public User registerUser(User user) throws UsernameAlreadyExistsException, EmailAlreadyExistsException {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UsernameAlreadyExistsException("Username '" + user.getUsername() + "' already exists.");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("Email '" + user.getEmail() + "' is already registered.");
        }

        // Hash password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user); // First, save the user
        
        UserDashboard newDashboard = new UserDashboard(
            null,  // MongoDB will auto-generate `_id`
            savedUser.getId(),  // Use the saved user's ID
            Collections.emptyList(),
            null,
            Collections.emptyList(),
            Collections.emptyList()
        );
        
        userDashboardRepository.save(newDashboard); // Now save the dashboard
        return savedUser;
        
    }

    // ✅ Validate user login with password hashing check

    public String validateUserLogin(String username, String password) 
            throws UserNotFoundException, IncorrectPasswordException {
        
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User with username '" + username + "' not found.");
        }

        User user = userOptional.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IncorrectPasswordException("Incorrect password for user '" + username + "'.");
        }

        return  jwtUtil.generateToken(username);
    }


    // ✅ Check if username exists
    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    // public User getUserFromToken(String token) throws UserNotFoundException {
    //     if (token == null || token.isEmpty()) {
    //         throw new UserNotFoundException("No authentication token found.");
    //     }

    //     // 1. Validate the token
    //     Optional<String> extractedUsername = jwtUtil.extractUsername(token);
    //     if (extractedUsername.isEmpty()) {
    //         throw new UserNotFoundException("Invalid or expired token.");
    //     }

    //     // 2. Fetch user from the database
    //     User user = userRepository.findByUsername(extractedUsername.get())
    //             .orElseThrow(() -> new UserNotFoundException("User not found for token."));

    //     // 3. Revalidate token against extracted username (extra security)
    //     if (!jwtUtil.validateToken(token, user.getUsername())) {
    //         throw new UserNotFoundException("Invalid token for this user.");
    //     }

    //     return user;
    // }
    public User updateUserDetails(UserUpdateRequest request,String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(request.getUsername());
        user.setFirstname(request.getFirstName());
        user.setLastname(request.getLastName());
        user.setEmail(request.getEmail());
        user.setNumber(request.getNumber());
        user.setDob(request.getDob());
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        return userRepository.save(user);
    }
}
