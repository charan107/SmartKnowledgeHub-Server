package com.myapp.myapp.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.myapp.Service.ReadPercentageService;
import com.myapp.myapp.dto.ReadPercent;

@RestController
@RequestMapping("/read-percentage")
@CrossOrigin(origins = "http://localhost:5173")
public class readPercentageController {
    
    @Autowired
    private  ReadPercentageService readPercentageService;
    @PostMapping("/{userId}/add")
    public ResponseEntity<?> addReadPercentage(@PathVariable String userId, @RequestBody ReadPercent readPercentage) {
        double percentage = readPercentage.getPercentage();
        String bookId = readPercentage.getId();
        readPercentageService.addReadPercentage(userId, bookId, percentage);
        return ResponseEntity.ok("Read percentage added successfully");
    }
    @GetMapping("/{userId}/{bookId}/getpercentage")
    public ReadPercent getReadPercentage(@PathVariable String userId,@PathVariable String bookId) {
        double percentage = readPercentageService.getReadPercentage(userId, bookId);
        ReadPercent readPercent = new ReadPercent();
        readPercent.setId(bookId);
        readPercent.setPercentage(percentage);
        return readPercent;
    }
}