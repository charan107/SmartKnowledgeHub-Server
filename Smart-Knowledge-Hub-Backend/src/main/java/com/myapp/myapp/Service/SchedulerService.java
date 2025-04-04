package com.myapp.myapp.Service;

import com.myapp.myapp.Repository.UserDashboardRepository;
import com.myapp.myapp.models.Borrowing;
import com.myapp.myapp.models.UserDashboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SchedulerService {

    @Autowired
    private UserDashboardRepository userDashboardRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Runs every midnight
    public void checkOverdueBooks() {
        List<UserDashboard> users = userDashboardRepository.findAll(); // Get all users
        for (UserDashboard user : users) {
            List<Borrowing> borrowings = user.getBorrowings();
            if (borrowings != null) {
                for (Borrowing borrowing : borrowings) {
                    if (borrowing.getDuedate().isBefore(LocalDate.now())) { // If due date is passed
                        if (borrowing.getStatus().equals("Pending")) {
                            borrowing.setStatus("Overdue"); // Mark as overdue
                        }
                    }
                }
                userDashboardRepository.save(user); // Save updated data
            }
        }
    }
}
