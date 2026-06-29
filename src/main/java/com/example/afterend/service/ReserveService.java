package com.example.afterend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.afterend.dto.ReserveDTO;
import com.example.afterend.dto.ReserveVO;
import com.example.afterend.entity.Reservation;

public interface ReserveService {
    Reservation reserve(ReserveDTO dto);
    Page<ReserveVO> pageReserves(Integer pageNum, Integer pageSize, Long userId, String status);
    void cancel(Long reserveId, Long userId);
}
