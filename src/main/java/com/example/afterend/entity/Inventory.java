package com.example.afterend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("tb_inventory")
public class Inventory {
    @TableId(type = IdType.AUTO)
    private Long inventoryId;
    private Long bookId;
    private Integer totalStock;
    private Integer actualStock;
    private String status;
    private LocalDateTime lastCheckTime;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
