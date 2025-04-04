package com.myapp.myapp.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.myapp.myapp.enums.EmailType;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    // ✅ Method to send different types of emails
    public void sendEmailForPurpose(String email, EmailType type, Map<String, String> placeholders) {
        String subject = "";
        String body = "";

        switch (type) {
            case REGISTRATION:
                subject = "🎉 Welcome to Smart Knowledge Hub – Your Learning Journey Begins!";
                body = """
                        We are delighted to have you as part of our Smart Knowledge Hub community! 🎉

                        With your new account, you can:
                        ✅ Borrow books online & offline
                        ✅ Track your borrowed & returned books effortlessly
                        ✅ Explore a vast collection of books, journals, and research materials
                        ✅ Stay updated on new arrivals and exciting library events

                        Your learning adventure starts now! 🚀

                        📖 Happy Reading!
                        **Smart Knowledge Hub Team**
                        """;

                break;

            case PASSWORD_RESET:
                subject = "Reset Your Password";
                body = "Hello,\n\nClick the link below to reset your password:\n" + placeholders.get("resetLink");
                break;

            case ORDER_CONFIRMATION:
                subject = "Order Confirmation";
                body = "Hello " + placeholders.get("name") + ",\n\nYour order #" + placeholders.get("orderId")
                        + " has been confirmed!";
                break;
            case BORROWED_BOOK:
                subject = placeholders.get("title") + String.format(
                        """
                                         Book Has Been Successfully Borrowed";
                                body = "Dear [User's Name],

                                                        We are pleased to inform you that the book \"XYZ\" has been successfully borrowed under your account.

                                                        Here are the details of your borrow:

                                                        Book Title: XYZ

                                                        Borrowed By: [User's Name]

                                                        Borrowed On:

                                                        Due Date: [Due Date]

                                                        Please make sure to return the book on or before the due date to avoid any late fees. If you have any questions or need further assistance, feel free to reach out to us.

                                                        Thank you for using our service!""",
                        placeholders.get("firstname") + placeholders.get("lastname"), placeholders.get("title"),
                        placeholders.get("title"), placeholders.get("firstname") +
                                placeholders.get("lastname"),
                        placeholders.get("borrowdate"), placeholders.get("duedate"));
                break;
            case NOTIFY_10:
                subject = "Reminder: Return Your Book in 10 Days";
                body = String.format(
                        """
                                Dear %s,

                                This is a friendly reminder that you have borrowed the book "%s" from %s. You have *10 days* left to return the book.

                                - *Book Title*: %s
                                - *Due Date*: %s

                                Please ensure to return the book on or before the due date to avoid any late fees.

                                Thank you for using our service!

                                Best regards,
                                %s
                                %s
                                """,
                        placeholders.get("userName"), placeholders.get("bookTitle"), placeholders.get("libraryName"),
                        placeholders.get("bookTitle"), placeholders.get("dueDate"), placeholders.get("libraryName"),
                        placeholders.get("contactInfo"));
                break;

            case NOTIFY_5:
                subject = "Reminder: Return Your Book in 5 Days";
                body = String.format(
                        """
                                Dear %s,

                                This is a friendly reminder that you have borrowed the book "%s" from %s. You have *5 days* left to return the book.

                                - *Book Title*: %s
                                - *Due Date*: %s

                                Please ensure to return the book on or before the due date to avoid any late fees.

                                Thank you for using our service!

                                Best regards,
                                %s
                                %s
                                """,
                        placeholders.get("userName"), placeholders.get("bookTitle"), placeholders.get("libraryName"),
                        placeholders.get("bookTitle"), placeholders.get("dueDate"), placeholders.get("libraryName"),
                        placeholders.get("contactInfo"));
                break;

            case NOTIFY_2:
                subject = "Reminder: Return Your Book in 2 Days";
                body = String.format(
                        """
                                Dear %s,

                                This is a friendly reminder that you have borrowed the book "%s" from %s. You have *2 days* left to return the book.

                                - *Book Title*: %s
                                - *Due Date*: %s

                                Please ensure to return the book on or before the due date to avoid any late fees.

                                Thank you for using our service!

                                Best regards,
                                %s
                                %s
                                """,
                        placeholders.get("userName"), placeholders.get("bookTitle"), placeholders.get("libraryName"),
                        placeholders.get("bookTitle"), placeholders.get("dueDate"), placeholders.get("libraryName"),
                        placeholders.get("contactInfo"));
                break;

            case NOTIFY_1:
                subject = "Reminder: Return Your Book in 1 Day";
                body = String.format(
                        """
                                Dear %s,

                                This is a final reminder that you have borrowed the book "%s" from %s. You have *1 day* left to return the book.

                                - *Book Title*: %s
                                - *Due Date*: %s

                                Please ensure to return the book by tomorrow to avoid any late fees.

                                Thank you for using our service!

                                Best regards,
                                %s
                                %s
                                """,
                        placeholders.get("userName"), placeholders.get("bookTitle"), placeholders.get("libraryName"),
                        placeholders.get("bookTitle"), placeholders.get("dueDate"), placeholders.get("libraryName"),
                        placeholders.get("contactInfo"));
                break;

            case NOTIFY_OVERDUED:
                subject = "Overdue: Return Your Book Immediately";
                body = String.format(
                        """
                                Dear %s,

                                We noticed that the book "%s" you borrowed from %s is now *overdue*. The due date for returning the book was %s.

                                - *Book Title*: %s
                                - *Due Date*: %s

                                Please return the book as soon as possible to avoid additional late fees. If you need assistance, feel free to reach out to us.

                                Thank you for your immediate attention to this matter!

                                Best regards,
                                %s
                                %s
                                """,
                        placeholders.get("userName"), placeholders.get("bookTitle"), placeholders.get("libraryName"),
                        placeholders.get("dueDate"), placeholders.get("bookTitle"), placeholders.get("dueDate"),
                        placeholders.get("libraryName"), placeholders.get("contactInfo"));
                break;

            default:
                subject = "Notification";
                body = "Hello,\n\nThis is a system-generated email.";
        }

        sendEmail(email, subject, body);
    }
}
