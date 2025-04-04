package com.myapp.myapp.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapp.myapp.Repository.BookRepository;
import com.myapp.myapp.Repository.UserDashboardRepository;
import com.myapp.myapp.dto.BookOverView;
import com.myapp.myapp.models.Book;
import com.myapp.myapp.models.Borrowing;
import com.myapp.myapp.models.UserDashboard;

@Service
public class QuickViewService {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserDashboardRepository userDashboardRepository;

    public BookOverView getBookDetails(String userId, String bookId) {
        Optional<Book> book1 = bookRepository.findById(bookId);
        if (!book1.isPresent()) {
            // Return a default or error response if the book is not found
            return new BookOverView(null, "Book not found", null, null, null, null, false);
        }

        Book book = book1.get();
        Optional<UserDashboard> userDashboardOpt = userDashboardRepository.findByUserId(userId);
        if (!userDashboardOpt.isPresent()) {
            // Return a response indicating the user dashboard is not found
            return new BookOverView(book.getId(), book.getTitle(), book.getAuthors(), book.getDescription(),
                    book.getCategories(), book.getImage(), false);
        }

        UserDashboard userDashboard = userDashboardOpt.get();
        if (userDashboard.getBorrowings() == null || userDashboard.getBorrowings().isEmpty()) {
            // Handle case where the user has no borrowings
            return new BookOverView(book.getId(), book.getTitle(), book.getAuthors(), book.getDescription(),
                    book.getCategories(), book.getImage(), false);
        }

        for (Borrowing borrowing : userDashboard.getBorrowings()) {
            if (borrowing.getBookId().equals(book.getId())) {
                return new BookOverView(book.getId(), book.getTitle(), book.getAuthors(), book.getDescription(),
                        book.getCategories(), book.getImage(), true);
            }
        }

        // If the book is not found in the user's borrowings
        return new BookOverView(book.getId(), book.getTitle(), book.getAuthors(), book.getDescription(),
                book.getCategories(), book.getImage(), false);
    }
}
