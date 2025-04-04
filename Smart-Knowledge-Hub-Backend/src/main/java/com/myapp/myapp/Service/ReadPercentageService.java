package com.myapp.myapp.Service;

import java.util.List;
import java.util.Optional; // Correct import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapp.myapp.Repository.UserDashboardRepository;
import com.myapp.myapp.models.Borrowing;
import com.myapp.myapp.models.UserDashboard;

@Service
public class ReadPercentageService {
    
    @Autowired
    private UserDashboardRepository userDashboardRepository;
    

    public void addReadPercentage(String userId,String bookId,double percentage){
        Optional<UserDashboard> user1 = userDashboardRepository.findByUserId(userId);
        if (user1.isPresent()) {
            UserDashboard user = user1.get();
            List<Borrowing> borrowings = user.getBorrowings();
            if (borrowings != null) {
                for (Borrowing borrowing : borrowings) {
                    if (borrowing.getBookId().equals(bookId)) {
                        borrowing.setPercentage(percentage);
                        break;
                    }
                }
            }
            userDashboardRepository.save(user);
        } else {
            throw new IllegalArgumentException("User with ID " + userId + " not found.");
        }
    }
    public double getReadPercentage(String userId,String bookId){
        Optional<UserDashboard> user1 = userDashboardRepository.findByUserId(userId);
        if (user1.isPresent()) {
            UserDashboard user = user1.get();
            List<Borrowing> borrowings = user.getBorrowings();
            if (borrowings != null) {
                for (Borrowing borrowing : borrowings) {
                    if (borrowing.getBookId().equals(bookId)) {
                        return borrowing.getPercentage();
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("User with ID " + userId + " not found.");
        }
        return 0;
    }
}
