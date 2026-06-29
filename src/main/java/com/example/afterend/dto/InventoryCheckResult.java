package com.example.afterend.dto;

import lombok.Data;

@Data
public class InventoryCheckResult {
    private Long bookId;
    private String bookName;
    private Integer systemStock;
    private Integer actualStock;
    private Integer diff;
    private String status;
}
