package com.myapp.myapp.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pdfs")
public class Pdf {
    
    @Id
    private String id;
    private String bookId;  // Reference to Book
    private String fileId;  // GridFS file ID
    private String filename;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }

    public String getFileId() { return fileId; }
    public void setFileId(String fileId) { this.fileId = fileId; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }
}