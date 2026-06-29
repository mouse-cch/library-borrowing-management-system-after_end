package com.example.afterend.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class StockLostDTO {
    private Long bookId;
    private Long userId;
    private String reason;
    private BigDecimal compensateAmount;
}
