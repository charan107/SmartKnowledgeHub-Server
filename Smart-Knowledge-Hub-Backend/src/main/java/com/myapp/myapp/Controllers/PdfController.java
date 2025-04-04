package com.myapp.myapp.Controllers;

import com.myapp.myapp.models.Pdf;
import com.myapp.myapp.models.UserDashboard;
import com.myapp.myapp.Repository.PdfRepository;
import com.myapp.myapp.Repository.UserDashboardRepository;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/books")
public class PdfController {

    private static final Logger logger = LoggerFactory.getLogger(PdfController.class);

    @Autowired
    private PdfRepository pdfRepository;

    @Autowired
    private UserDashboardRepository userDashboardRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    // 📌 Fetch PDF by Book ID and update user's currentlyReadingId
    @GetMapping("/{bookId}/{userId}pdf")
    public ResponseEntity<?> getPdfByBookId(
            @PathVariable String bookId,
            @PathVariable String userId
    ) {
        try {
            // 🔍 Step 1: Fetch the PDF metadata using bookId
            Optional<Pdf> pdfOptional = pdfRepository.findByBookId(bookId);
            if (!pdfOptional.isPresent()) {
                return ResponseEntity.status(404).body("❌ No PDF found for this book");
            }

            Pdf pdf = pdfOptional.get();
            String fileId = pdf.getFileId();
            logger.info("Fetching file with fileId: {}", fileId);

            // 🔁 Step 2: Update user's currentlyReadingId
            Optional<UserDashboard> userOptional = userDashboardRepository.findByUserId(userId);
            if (userOptional.isPresent()) {
                UserDashboard user = userOptional.get();
                user.setCurrentlyReadingId(bookId);
                userDashboardRepository.save(user);
                logger.info("✅ Updated currentlyReadingId for user {} to {}", userId, bookId);
            } else {
                logger.warn("⚠️ User with id {} not found. Skipping dashboard update.", userId);
            }

            // 🔓 Step 3: Convert fileId to ObjectId
            ObjectId objectId;
            try {
                objectId = new ObjectId(fileId);
            } catch (IllegalArgumentException e) {
                logger.error("Invalid fileId format: {}", fileId);
                return ResponseEntity.status(400).body("❌ Invalid fileId format");
            }

            // 🧲 Step 4: Fetch PDF from GridFS
            GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(objectId)));
            if (gridFSFile == null) {
                logger.warn("No file found in GridFS for fileId: {}", fileId);
                return ResponseEntity.status(404).body("❌ PDF file not found in GridFS");
            }

            GridFsResource resource = gridFsTemplate.getResource(gridFSFile);

            // 🎯 Step 5: Return the PDF
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + pdf.getFilename() + "\"")
                    .body(new InputStreamResource(resource.getInputStream()));

        } catch (Exception e) {
            logger.error("Error retrieving PDF: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("❌ Error retrieving PDF: " + e.getMessage());
        }
    }
}
