package com.example.afterend.dto;

import lombok.Data;

@Data
public class InventoryCheckDTO {
    private Long bookId;
    private Integer actualStock;
}
