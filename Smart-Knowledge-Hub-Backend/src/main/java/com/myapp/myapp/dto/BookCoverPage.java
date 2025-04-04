package com.myapp.myapp.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookCoverPage {
    private String bookId;
    private String title;
    private String coverImageUrl;
}