package com.example.afterend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("tb_borrow")
public class Borrow {
    @TableId(type = IdType.AUTO)
    private Long borrowId;
    private String borrowNo;
    private Long bookId;
    private Long userId;
    private LocalDateTime borrowTime;
    private LocalDateTime dueTime;
    private LocalDateTime actualReturnTime;
    private String status;
    private Integer renewCount;
    private Integer overdueDays;
    private String remark;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
