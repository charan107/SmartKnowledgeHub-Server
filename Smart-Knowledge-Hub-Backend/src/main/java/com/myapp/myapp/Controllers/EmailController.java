package com.myapp.myapp.Controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.myapp.myapp.Service.*;
@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public String sendEmail(@RequestParam String to, 
                            @RequestParam String subject, 
                            @RequestParam String body) {
        emailService.sendEmail(to, subject, body);
        return "📧 Email Sent Successfully!";
    }
}
