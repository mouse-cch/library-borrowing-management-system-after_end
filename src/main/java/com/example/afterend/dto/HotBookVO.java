package com.example.afterend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HotBookVO {
    private Long bookId;
    private String bookName;
    private Integer borrowCount;
}
