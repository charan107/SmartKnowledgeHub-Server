package com.myapp.myapp.Service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.myapp.myapp.Repository.UserDashboardRepository;
import com.myapp.myapp.models.UserDashboard;

@Service
public class WishListService {
    @Autowired
    private UserDashboardRepository userDashboardRepository;
    public ResponseEntity<?> addToWishlist(String userId,String bookId){
        Optional<UserDashboard> user1 = userDashboardRepository.findByUserId(userId);
        if(user1.isPresent()){
            UserDashboard user = user1.get();
            List<String> wishlist = user.getWishlist();
            if(wishlist==null){
                wishlist = new ArrayList<>();
            }
            for(String item:user.getWishlist()){
                if(item.equals(bookId)){
                    return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of( "message", "Already added to Wishlist"));
                }
            }
            wishlist.add(bookId);
            user.setWishlist(wishlist);
            userDashboardRepository.save(user);
        }
        else{
            throw new IllegalArgumentException("User with ID"+userId + "is not found");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
        .body(Map.of("message", "Added to Wishlist"));
    }
    public ResponseEntity<?> removeFromWishlist(String userId,String bookId){
        Optional<UserDashboard> user1 = userDashboardRepository.findByUserId(userId);
        if(user1.isPresent()){
            UserDashboard user = user1.get();
            List<String> wishlist = user.getWishlist();
            if(wishlist==null || wishlist.isEmpty()){
                return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "No items in Wishlist"));
            }
            if(wishlist.contains(bookId)){
                wishlist.remove(bookId);
                user.setWishlist(wishlist);
                userDashboardRepository.save(user);
                return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Removed from Wishlist"));
            }
            else{
                return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Book not found in Wishlist"));
            }
        }
        else{
            throw new IllegalArgumentException("User with ID"+userId + "is not found");
        }
    }
}
