package com.example.afterend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.afterend.entity.Inventory;
import org.apache.ibatis.annotations.Mapper;

/**
 * Inventory Mapper接口
 * @author cch
 * @since 2026-06-29
 */
@Mapper
public interface InventoryMapper extends BaseMapper<Inventory> {
}
