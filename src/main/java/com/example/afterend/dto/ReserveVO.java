package com.example.afterend.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 预约记录VO — 包含关联的图书名
 */
@Data
public class ReserveVO {
    private Long reserveId;
    private Long bookId;
    private String bookName;
    private Long userId;
    private String userName;
    private LocalDateTime reserveTime;
    private Integer queueNo;
    private String status;
    private LocalDateTime createTime;
}
