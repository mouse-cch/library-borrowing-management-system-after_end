package com.example.afterend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("tb_book")
public class Book {
    @TableId(type = IdType.AUTO)
    private Long bookId;
    private String bookName;
    private String isbn;
    private String author;
    private String publisher;
    private LocalDate publishDate;
    private String category;
    private String location;
    private Integer totalStock;
    private Integer currentStock;
    private String damageStatus;
    private String scrapStatus;
    @TableLogic
    private Integer isDeleted;
    private String remark;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
