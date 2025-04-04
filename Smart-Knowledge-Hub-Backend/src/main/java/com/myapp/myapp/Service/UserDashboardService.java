package com.myapp.myapp.Service;

import com.myapp.myapp.models.Book;
import com.myapp.myapp.models.UserDashboard;
import com.myapp.myapp.dto.CompletedModel;
import com.myapp.myapp.dto.DashboardBookModel;
import com.myapp.myapp.dto.WishListModel;
import com.myapp.myapp.Repository.BookRepository;
import com.myapp.myapp.Repository.UserDashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserDashboardService {

    @Autowired
    private UserDashboardRepository userDashboardRepository;

    @Autowired
    private BookRepository bookRepository;

    public List<DashboardBookModel> getUserDashboardDetails(String userId) {
        Optional<UserDashboard> userDashboardOpt = userDashboardRepository.findByUserId(userId);
        List<DashboardBookModel> dashboardBooks = new ArrayList<>();
        
        if (userDashboardOpt.isPresent()) {
            UserDashboard userDashboard = userDashboardOpt.get();
            String currentlyReadingId = userDashboard.getCurrentlyReadingId();
            
            // Fetch currently reading book details
            if (currentlyReadingId != null) {
                Optional<Book> bookOpt = bookRepository.findById(currentlyReadingId);
                if (bookOpt.isPresent()) {
                    Book book = bookOpt.get();
                    DashboardBookModel currentlyReading = new DashboardBookModel();
                    currentlyReading.setId(book.getId());
                    currentlyReading.setTitle(book.getTitle());
                    currentlyReading.setImage(book.getImage());
                    currentlyReading.setStatus("Reading");
                    currentlyReading.setCurrentlyReading(true);
                    
                    // Fetch borrowing details
                    userDashboard.getBorrowings().stream()
                        .filter(borrowing -> borrowing.getBookId().equals(currentlyReadingId))
                        .findFirst()
                        .ifPresent(borrowing -> {

                            currentlyReading.setPercentage(borrowing.getPercentage());
                            currentlyReading.setBorrowDate(borrowing.getBorrowdate());
                            currentlyReading.setDueDate(borrowing.getDuedate());
                        });
                    
                    dashboardBooks.add(currentlyReading);
                }
            }
            
            // Fetch borrowed books details, excluding the currently reading book
            userDashboard.getBorrowings().stream()
                .filter(borrowing -> !borrowing.getBookId().equals(currentlyReadingId))
                .forEach(borrowing -> {
                    Optional<Book> bookOpt = bookRepository.findById(borrowing.getBookId());
                    if (bookOpt.isPresent()) {
                        Book book = bookOpt.get();
                        DashboardBookModel bookModel = new DashboardBookModel();
                        bookModel.setId(book.getId());
                        bookModel.setTitle(book.getTitle());
                        bookModel.setBorrowDate(borrowing.getBorrowdate());
                        bookModel.setDueDate(borrowing.getDuedate());
                        bookModel.setPercentage(borrowing.getPercentage());
                        bookModel.setImage(book.getImage());
                        bookModel.setStatus(borrowing.getStatus());
                        bookModel.setCurrentlyReading(false);
                        dashboardBooks.add(bookModel);
                    }
                });
        }
        return dashboardBooks;
    }
    public List<WishListModel> getWishList(String userId){
        Optional<UserDashboard> userdashboardOpt = userDashboardRepository.findByUserId(userId);
        List<WishListModel> wishlists = new ArrayList<>();
        if(userdashboardOpt.isPresent()){
            UserDashboard userdashboard = userdashboardOpt.get();
            userdashboard.getWishlist().stream()
            .forEach(item->{
                Optional<Book> bookOpt = bookRepository.findById(item);
                if(bookOpt.isPresent()){
                    Book book = bookOpt.get();  
                    WishListModel wishListModel = new WishListModel();
                    wishListModel.setId(book.getId());
                    wishListModel.setImage(book.getImage());
                    wishListModel.setTitle(book.getTitle());
                    wishlists.add(wishListModel);
                }
            });
        }
        return wishlists;
    }
    public List<CompletedModel> getCompleted(String userId) {
    Optional<UserDashboard> userdashboardOpt = userDashboardRepository.findByUserId(userId);
    List<CompletedModel> completedBooks = new ArrayList<>();

    if (userdashboardOpt.isPresent()) {
        UserDashboard userdashboard = userdashboardOpt.get();

        // Extract book IDs from the completed book objects
        List<String> completedBookIds = userdashboard.getCompletedBooks().stream()
            .map(completed -> completed.getBookId()) // Extracting bookId
            .collect(Collectors.toList());

        // Fetch all books in one query
        List<Book> books = bookRepository.findAllById(completedBookIds);

        // Map books to CompletedModel
        for (Book book : books) {
            CompletedModel completedModel = new CompletedModel();
            completedModel.setImage(book.getImage());
            completedModel.setTitle(book.getTitle());

            // Find the corresponding completed object to get days
            userdashboard.getCompletedBooks().stream()
                .filter(completed -> completed.getBookId().equals(book.getId()))
                .findFirst()
                .ifPresent(completed -> completedModel.setDays(completed.getDays()));

            completedBooks.add(completedModel);
        }
    }
    return completedBooks;
}

}
