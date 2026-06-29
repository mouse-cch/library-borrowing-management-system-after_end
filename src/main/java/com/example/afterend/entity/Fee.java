package com.example.afterend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("tb_fee")
public class Fee {
    @TableId(type = IdType.AUTO)
    private Long feeId;
    private Long userId;
    private Long borrowId;
    private Long bookId;
    private String feeType;
    private BigDecimal amount;
    private String reason;
    private String status;
    private LocalDateTime payTime;
    @TableLogic
    private Integer isDeleted;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
