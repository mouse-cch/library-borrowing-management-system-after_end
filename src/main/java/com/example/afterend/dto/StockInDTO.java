package com.example.afterend.dto;

import lombok.Data;

@Data
public class StockInDTO {
    private Long bookId;
    private Integer quantity;
    private String source;
    private Long operatorId;
}
