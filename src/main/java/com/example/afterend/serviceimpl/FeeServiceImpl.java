package com.example.afterend.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.afterend.dto.FeeApplyDTO;
import com.example.afterend.entity.Fee;
import com.example.afterend.exception.BusinessException;
import com.example.afterend.mapper.FeeMapper;
import com.example.afterend.service.FeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeeServiceImpl implements FeeService {

    private final FeeMapper feeMapper;

    @Override
    public Page<Fee> pageFees(Integer pageNum, Integer pageSize, Long userId, String status) {
        LambdaQueryWrapper<Fee> qw = new LambdaQueryWrapper<>();
        if (userId != null) qw.eq(Fee::getUserId, userId);
        if (status != null) qw.eq(Fee::getStatus, status);
        qw.orderByDesc(Fee::getCreateTime);
        return feeMapper.selectPage(new Page<>(pageNum, pageSize), qw);
    }

    @Override
    public void pay(Long feeId) {
        Fee fee = feeMapper.selectById(feeId);
        if (fee == null) throw new BusinessException("费用记录不存在");
        if ("paid".equals(fee.getStatus())) throw new BusinessException("该费用已缴纳");
        fee.setStatus("paid");
        fee.setPayTime(LocalDateTime.now());
        feeMapper.updateById(fee);
    }

    @Override
    public Long apply(FeeApplyDTO dto) {
        Fee fee = new Fee();
        fee.setUserId(dto.getUserId());
        fee.setFeeType(dto.getFeeType());
        fee.setAmount(dto.getAmount());
        fee.setReason(dto.getReason());
        fee.setStatus("unpaid");
        feeMapper.insert(fee);
        return fee.getFeeId();
    }
}
