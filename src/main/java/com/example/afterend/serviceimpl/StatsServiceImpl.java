package com.example.afterend.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.afterend.dto.DashboardVO;
import com.example.afterend.dto.HotBookVO;
import com.example.afterend.entity.*;
import com.example.afterend.mapper.*;
import com.example.afterend.service.StatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final BookMapper bookMapper;
    private final UserMapper userMapper;
    private final BorrowMapper borrowMapper;

    @Override
    public DashboardVO dashboard() {
        int totalBooks = bookMapper.selectCount(null).intValue();
        int totalUsers = userMapper.selectCount(null).intValue();
        int borrowedBooks = borrowMapper.selectCount(
            new LambdaQueryWrapper<Borrow>().eq(Borrow::getStatus, "borrowing")).intValue();
        int overdueCount = borrowMapper.selectCount(
            new LambdaQueryWrapper<Borrow>().eq(Borrow::getStatus, "overdue")).intValue();
        int activeBorrows = borrowedBooks;
        int stockInCount = totalBooks;
        return new DashboardVO(totalBooks, totalUsers, borrowedBooks, stockInCount, overdueCount, activeBorrows);
    }

    @Override
    public List<HotBookVO> hotBooks(Integer topN) {
        if (topN == null) topN = 10;
        List<Borrow> allBorrows = borrowMapper.selectList(null);
        Map<Long, Long> countMap = allBorrows.stream()
                .collect(Collectors.groupingBy(Borrow::getBookId, Collectors.counting()));
        return countMap.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(topN)
                .map(e -> {
                    Book book = bookMapper.selectById(e.getKey());
                    return new HotBookVO(e.getKey(), book != null ? book.getBookName() : "未知", e.getValue().intValue());
                })
                .collect(Collectors.toList());
    }
}
