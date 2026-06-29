package com.example.afterend.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 逾期记录VO — 包含图书名、用户名、罚款金额
 */
@Data
public class OverdueVO {
    private Long borrowId;
    private String borrowNo;
    private Long bookId;
    private String bookName;
    private Long userId;
    private String userName;
    private LocalDateTime dueTime;
    private Integer overdueDays;
    private BigDecimal fineAmount;
    private String status;
}
