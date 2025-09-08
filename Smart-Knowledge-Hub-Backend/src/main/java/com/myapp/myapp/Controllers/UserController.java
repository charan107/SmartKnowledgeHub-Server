package com.myapp.myapp.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import com.myapp.myapp.Service.*;
import com.myapp.myapp.Exceptions.EmailAlreadyExistsException;
import com.myapp.myapp.Exceptions.IncorrectPasswordException;
import com.myapp.myapp.Exceptions.UserNotFoundException;
import com.myapp.myapp.Exceptions.UsernameAlreadyExistsException;
import com.myapp.myapp.Repository.UserRepository;
import com.myapp.myapp.models.*;
import com.myapp.myapp.security.JwtUtil;


import com.myapp.myapp.dto.*;
import com.myapp.myapp.enums.EmailType;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private UserService userService;

    // @Autowired
    // private EmailService emailService;

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository, JwtUtil jwtUtil) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            userService.registerUser(user);

            // Send welcome email
            //   Map<String, String> placeholders = new HashMap<>();
            // placeholders.put("name", user.getUsername());
            // emailService.sendEmailForPurpose(user.getEmail(), EmailType.REGISTRATION, placeholders);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("status", 201, "message", "✅ User registered successfully! Welcome email sent. 🎉"));

        } catch (UsernameAlreadyExistsException | EmailAlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error",
                            "An unexpected error occurred while registering the user."));
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request", "Username and password are required"));
            }
    
            String token = userService.validateUserLogin(loginRequest.getUsername(), loginRequest.getPassword());
    
            // ✅ Send the token in the response instead of cookies
            return ResponseEntity.ok(Map.of(
                    "message", "Login successful",
                    "username", loginRequest.getUsername(),
                    "token", token // 🔥 Return token directly
            ));
    
        } catch (UserNotFoundException | IncorrectPasswordException e) {    
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Unauthorized", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", "An unexpected error occurred"));
        }
    }
    
    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Boolean>> checkUsernameAvailability(@RequestParam String username) {
        boolean available = !userService.usernameExists(username);
        return ResponseEntity.ok(Map.of("available", available));
    }

    // @GetMapping("/check-auth")
    // public ResponseEntity<Map<String, User>> authenticateUser(
    //         @CookieValue(value = "jwt", required = false) String token) throws UserNotFoundException {
    //     User user = userService.getUserFromToken(token);
    //     return ResponseEntity.ok(Map.of("user", user));
    // }
    @GetMapping("/{username}")
public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
    // ✅ Fetch user details without requiring Authorization
    Optional<User> user = userRepository.findByUsername(username);

    if (user.isPresent()) {
        return ResponseEntity.ok(user.get()); // ✅ Return user directly
    } else {
        return ResponseEntity.status(404).body(Map.of("error", "User not found"));
    }
}

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody UserUpdateRequest request, @RequestParam String username) {
        User updatedUser = userService.updateUserDetails(request,username);
        return ResponseEntity.ok(updatedUser);
    }
}
