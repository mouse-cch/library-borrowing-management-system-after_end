package com.example.afterend.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 借阅记录VO — 包含关联的图书名和用户名
 */
@Data
public class BorrowVO {
    private Long borrowId;
    private String borrowNo;
    private Long bookId;
    private String bookName;
    private Long userId;
    private String userName;
    private LocalDateTime borrowTime;
    private LocalDateTime dueTime;
    private LocalDateTime actualReturnTime;
    private String status;
    private Integer renewCount;
    private Integer overdueDays;
    private String remark;
    private LocalDateTime createTime;
}
