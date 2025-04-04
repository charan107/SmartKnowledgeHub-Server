package com.myapp.myapp.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapp.myapp.Repository.UserDashboardRepository;
import com.myapp.myapp.models.Borrowing;
import com.myapp.myapp.models.UserDashboard;

@Service
public class BorrowingService {
    @Autowired
    private UserDashboardRepository userDashboardRepository;
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
    
            // Set completedReadingId if it's null
            if (user.getCurrentlyReadingId() == null || user.getCurrentlyReadingId().isEmpty()) {
                user.setCurrentlyReadingId(bookId);
            }
    
            userDashboardRepository.save(user);
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
            } else {
                throw new IllegalArgumentException("No borrowings found for user with ID " + userId);
            }
        } else {
            throw new IllegalArgumentException("User with ID " + userId + " not found.");
        }
    }
}
