package com.example.afterend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.afterend.entity.Role;
import org.apache.ibatis.annotations.Mapper;

/**
 * Role Mapper接口
 * @author cch
 * @since 2026-06-29
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
}
