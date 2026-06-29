package com.example.afterend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardVO {
    private Integer totalBooks;
    private Integer totalUsers;
    private Integer borrowedBooks;
    private Integer stockInCount;
    private Integer overdueCount;
    private Integer activeBorrows;
}
