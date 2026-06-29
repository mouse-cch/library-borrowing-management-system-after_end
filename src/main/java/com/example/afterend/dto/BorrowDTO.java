package com.example.afterend.dto;

import lombok.Data;

@Data
public class BorrowDTO {
    private Long bookId;
    private Long userId;
    private Integer days;
}
