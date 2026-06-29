package com.example.afterend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.afterend.dto.FeeApplyDTO;
import com.example.afterend.entity.Fee;

public interface FeeService {
    Page<Fee> pageFees(Integer pageNum, Integer pageSize, Long userId, String status);
    void pay(Long feeId);
    Long apply(FeeApplyDTO dto);
}
