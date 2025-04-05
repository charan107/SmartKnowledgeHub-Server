package com.myapp.myapp.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapp.myapp.Repository.BookRepository;
import com.myapp.myapp.Repository.UserDashboardRepository;
import com.myapp.myapp.Repository.UserRepository;
import com.myapp.myapp.enums.EmailType;
import com.myapp.myapp.models.Book;
import com.myapp.myapp.models.Borrowing;
import com.myapp.myapp.models.User;
import com.myapp.myapp.models.UserDashboard;
import java.time.format.DateTimeFormatter; // Add this import

@Service
public class BorrowingService {
    @Autowired
    private UserDashboardRepository userDashboardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService; // Inject EmailService

    @Autowired
    private BookRepository bookRepository;

    public void addBorrowing(String userId, String bookId) {
        Optional<UserDashboard> user1 = userDashboardRepository.findByUserId(userId);
        if (user1.isPresent()) {
            UserDashboard user = user1.get();

            Borrowing borrowing = new Borrowing();
            borrowing.setBookId(bookId);
            borrowing.setBorrowdate(LocalDate.now());
            borrowing.setDuedate(LocalDate.now().plusDays(30));
            borrowing.setStatus("Pending");
            borrowing.setPercentage(0);

            List<Borrowing> borrowings = user.getBorrowings();
            if (borrowings == null) {
                borrowings = new ArrayList<>();
            }

            borrowings.add(borrowing);
            user.setBorrowings(borrowings);

            if (user.getCurrentlyReadingId() == null || user.getCurrentlyReadingId().isEmpty()) {
                user.setCurrentlyReadingId(bookId);
            }

            userDashboardRepository.save(user);

            // 🎯 Fetch email from User table
            // ...existing code...

            // 🎯 Fetch email from User table
            User userEntity = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found."));
            String email = userEntity.getEmail();
            if (email == null || email.isEmpty()) {
                throw new IllegalArgumentException("Email not found for user with ID " + userId);
            }

            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new IllegalArgumentException("Book with ID " + bookId + " not found."));
            String bookTitle = book.getTitle();
            // Create a map containing placeholders for the email
            Map<String, String> emailPlaceholders = new HashMap<>();
            emailPlaceholders.put("firstname", userEntity.getFirstname());
            emailPlaceholders.put("lastname", userEntity.getLastname());
            emailPlaceholders.put("bookTitle", bookTitle); // Assuming Borrowing has a getBookTitle()
                                                           // method

            // ...existing code...

            // Create a DateTimeFormatter for formatting the dates
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // Convert LocalDate to String using the formatter
            emailPlaceholders.put("dueDate", borrowing.getDuedate().format(formatter));
            emailPlaceholders.put("borrowDate", borrowing.getBorrowdate().format(formatter));

            // ...existing code...

            // Send the email with placeholders
            emailService.sendEmailForPurpose(email, EmailType.BORROWED_BOOK, emailPlaceholders);

        } else {
            throw new IllegalArgumentException("User with ID " + userId + " not found.");
        }
    }

    public void removeBorrowing(String userId, String bookId) {
        Optional<UserDashboard> user1 = userDashboardRepository.findByUserId(userId);
        if (user1.isPresent()) {
            UserDashboard user = user1.get();
            List<Borrowing> borrowings = user.getBorrowings();
            if (borrowings != null && !borrowings.isEmpty()) {
                for (int i = 0; i < borrowings.size(); i++) {
                    if (borrowings.get(i).getBookId().equals(bookId)) {
                        borrowings.remove(i);
                        break;
                    }
                }
                user.setBorrowings(borrowings);

                // Check if the bookId matches currentlyReadingId and update it
                if (bookId.equals(user.getCurrentlyReadingId())) {
                    if (!borrowings.isEmpty()) {
                        user.setCurrentlyReadingId(borrowings.get(0).getBookId()); // Set to another book
                    } else {
                        user.setCurrentlyReadingId(null); // Set to null if no borrowings remain
                    }
                }

                userDashboardRepository.save(user);

                // Send email notification using EmailService

            } else {
                throw new IllegalArgumentException("No borrowings found for user with ID " + userId);
            }
        } else {
            throw new IllegalArgumentException("User with ID " + userId + " not found.");
        }
    }
}