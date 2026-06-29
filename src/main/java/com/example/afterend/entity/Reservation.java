package com.example.afterend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("tb_reservation")
public class Reservation {
    @TableId(type = IdType.AUTO)
    private Long reserveId;
    private Long bookId;
    private Long userId;
    private LocalDateTime reserveTime;
    private Integer queueNo;
    private String status;
    @TableLogic
    private Integer isDeleted;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
