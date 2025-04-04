package com.myapp.myapp.models;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Borrowing {
    private String bookId;
    private LocalDate borrowdate;
    private LocalDate duedate;
    private String status;
    private double percentage;
}