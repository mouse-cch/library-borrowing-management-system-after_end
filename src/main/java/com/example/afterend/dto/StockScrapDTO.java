package com.example.afterend.dto;

import lombok.Data;

@Data
public class StockScrapDTO {
    private Long bookId;
    private Integer quantity;
    private String reason;
}
