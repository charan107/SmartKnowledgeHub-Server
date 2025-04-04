package com.myapp.myapp.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.myapp.Service.WishListService;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/wishlist")
public class WishListController {
    @Autowired
    private WishListService wishListService;

    @PostMapping("/{userId}/add/{bookId}")
    public ResponseEntity<?> addToWishList(@PathVariable String userId,@PathVariable String bookId){
        return wishListService.addToWishlist(userId, bookId);
    }
    @PostMapping("/{userId}/remove/{bookId}")
    public ResponseEntity<?> removeFromWishList(@PathVariable String userId,@PathVariable String bookId){
        return wishListService.removeFromWishlist(userId, bookId);
    }
}
