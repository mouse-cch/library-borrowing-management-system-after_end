package com.example.afterend.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class FeeApplyDTO {
    private Long userId;
    private String feeType;
    private BigDecimal amount;
    private String reason;
}
