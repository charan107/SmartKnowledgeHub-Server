package com.myapp.myapp.Service;
import com.myapp.myapp.models.Book;
import com.myapp.myapp.models.Section;
import com.myapp.myapp.Repository.BookRepository;
import com.myapp.myapp.Repository.SectionRepository;
import com.myapp.myapp.dto.BookCoverPage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SectionService {
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private BookRepository bookRepository;

    public List<BookCoverPage> getSectionByName(String name) {
        Optional<Section> section =  sectionRepository.findByName(name);
        Section sec = section.get();
        List<String> bookids = sec.getBooksId();
        List<Book> books = bookRepository.findAllById(bookids);
        List<BookCoverPage> bookCoverPages = new ArrayList<>();
        for (Book book : books) {
            BookCoverPage bookCoverPage = new BookCoverPage(book.getId(), book.getTitle(), book.getImage());
            bookCoverPages.add(bookCoverPage);
        }
        return bookCoverPages;
    }

    public Section addSection(Section section) {
        return sectionRepository.save(section);
    }

    public void deleteSection(String id) {
        sectionRepository.deleteById(id);
    }
}
