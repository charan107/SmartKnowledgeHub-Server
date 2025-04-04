package com.myapp.myapp.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.myapp.Service.QuickViewService;
import com.myapp.myapp.dto.BookOverView;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/quick-view")
public class QuickViewController {
    @Autowired
    private QuickViewService quickViewService;
    @GetMapping("/{userId}/get/{bookId}")
    public BookOverView getBookDetails(@PathVariable String userId,@PathVariable String bookId){
        return quickViewService.getBookDetails(userId,bookId);
    }
}
