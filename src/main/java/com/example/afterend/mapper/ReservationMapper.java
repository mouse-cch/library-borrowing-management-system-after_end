package com.example.afterend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.afterend.entity.Reservation;
import org.apache.ibatis.annotations.Mapper;

/**
 * Reservation Mapper接口
 * @author cch
 * @since 2026-06-29
 */
@Mapper
public interface ReservationMapper extends BaseMapper<Reservation> {
}
