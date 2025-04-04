package com.myapp.myapp.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardBookModel {
    private String id;
    private String title;
    private double percentage;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private String status;
    private String image;
    private boolean currentlyReading;
}
