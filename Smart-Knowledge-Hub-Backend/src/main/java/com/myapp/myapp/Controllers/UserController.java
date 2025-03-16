package com.myapp.myapp.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.myapp.myapp.dto.*;;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    private final UserRepository userRepository; // Ensure it's final to prevent null issues

    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository; // Inject properly
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            // Register user (throws exception if username or email exists)
            userService.registerUser(user);

            // Send welcome email
            emailService.sendEmail(
                    user.getEmail(),
                    "📚 Welcome to Smart Knowledge Hub – Your Gateway to Knowledge!",
                    """
                            We are delighted to have you as part of our reading community.

                            With your new account, you can:
                            ✅ Borrow books online & offline
                            ✅ Track your borrowed & returned books
                            ✅ Explore a vast collection of books & journals
                            ✅ Receive updates on new arrivals and library events

                            To get started, visit our website and explore the world of books!

                            If you have any questions, feel free to contact our support team at [Library Email] or visit our help desk.

                            📖 Happy Reading!
                            **Smart Knowledge Hub Team**
                            """
            );

            // ✅ Return structured JSON response for success
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("status", 201, "message", "✅ User registered successfully! Welcome email sent. 🎉"));

        } catch (UsernameAlreadyExistsException | EmailAlreadyExistsException ex) {
            // ❌ Return structured JSON error response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request", ex.getMessage()));

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error",
                            "An unexpected error occurred while registering the user."));
        }
    }@PostMapping("/login")
public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
    try {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        if (username == null || password == null) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request", "Username and password are required"));
        }

        User loggedInUser = userService.validateUserLogin(username, password);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "user", Map.of(
                        "username", loggedInUser.getUsername(),
                        "firstName", loggedInUser.getFirstname() != null ? loggedInUser.getFirstname() : "",
                        "lastName", loggedInUser.getLastname() != null ? loggedInUser.getLastname() : "",
                        "email", loggedInUser.getEmail() != null ? loggedInUser.getEmail() : "",
                        "phone", loggedInUser.getNumber() != null ? loggedInUser.getNumber() : "",
                        "dob", loggedInUser.getDob() != null ? loggedInUser.getDob() : ""
                )
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
}
