package com.example.afterend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("tb_operation_log")
public class OperationLog {
    @TableId(type = IdType.AUTO)
    private Long logId;
    private String module;
    private String action;
    private Long operatorId;
    private String operatorName;
    private String ip;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
