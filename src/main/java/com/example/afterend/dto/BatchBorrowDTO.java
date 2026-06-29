package com.example.afterend.dto;

import lombok.Data;
import java.util.List;

@Data
public class BatchBorrowDTO {
    private Long userId;
    private List<Long> bookIds;
}
