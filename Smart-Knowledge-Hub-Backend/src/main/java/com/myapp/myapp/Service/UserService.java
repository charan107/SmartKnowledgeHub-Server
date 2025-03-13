package com.myapp.myapp.Service;
import com.myapp.myapp.models.User;
import com.myapp.myapp.Repository.UserRepository;
import com.myapp.myapp.Exceptions.UsernameAlreadyExistsException;
import com.myapp.myapp.Exceptions.EmailAlreadyExistsException;
import com.myapp.myapp.Exceptions.UserNotFoundException;
import com.myapp.myapp.Exceptions.IncorrectPasswordException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    private final UserRepository userRepository;

    @Autowired  
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ✅ Check if username or email exists
   

    // ✅ Register user with exception handling
    public User registerUser(User user) throws UsernameAlreadyExistsException, EmailAlreadyExistsException {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UsernameAlreadyExistsException("Username '" + user.getUsername() + "' already exists.");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("Email '" + user.getEmail() + "' is already registered.");
        }
        return userRepository.save(user);   
    }

    // ✅ Validate user login with checked exceptions
    public User validateUserLogin(String username, String password) throws UserNotFoundException, IncorrectPasswordException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User with username '" + username + "' not found.");
        }
    
        User user = userOptional.get();
        if (!user.getPassword().equals(password)) {
            throw new IncorrectPasswordException("Incorrect password for user '" + username + "'.");
        }
    
        return user; // ✅ Return user object instead of boolean
    }
    

    // ✅ Fetch all users
    public boolean usernameExists(String username) {
        // Example taken usernames
        return userRepository.existsByUsername(username);
    }

}
