package com.example.afterend.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页查询结果
 * @author cch
 * @since 2026-06-29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    private Long total;
    private List<T> list;
    private Integer pageNum;
    private Integer pageSize;
    private Integer pages;
}
