package com.example.afterend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BorrowResult {
    private Long bookId;
    private String bookName;
    private Boolean success;
    private Long borrowId;
    private String borrowNo;
    private String reason;
}
