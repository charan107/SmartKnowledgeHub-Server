package com.myapp.myapp.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookOverView {
    private String bookId;
    private String title;
    private List<String> authors;
    private String description;
    private List<String> category;
    private String image;
    private Boolean borrowed;
}
