package com.myapp.myapp.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.myapp.Service.UserDashboardService;
import com.myapp.myapp.dto.CompletedModel;
import com.myapp.myapp.dto.DashboardBookModel;
import com.myapp.myapp.dto.WishListModel;
@CrossOrigin(origins = "http://localhost:5173") 
@RestController
@RequestMapping("/dashboard")
public class UserDashboardController {
    @Autowired
    private UserDashboardService userDashboardService;

    @GetMapping("/get/{userId}")
    public List<DashboardBookModel> getUserDashboardDetails(@PathVariable String userId){
        return userDashboardService.getUserDashboardDetails(userId);
    }
    @GetMapping("/get/wishlist/{userId}")
    public List<WishListModel> getWishList(@PathVariable String userId){
        return userDashboardService.getWishList(userId);
    }
    @GetMapping("/get/completed/{userId}")
    public List<CompletedModel> getCompleted(@PathVariable String userId){
        return userDashboardService.getCompleted(userId);
    }
    
}
