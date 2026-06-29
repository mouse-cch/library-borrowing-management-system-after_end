package com.example.afterend.service;

import com.example.afterend.dto.DashboardVO;
import com.example.afterend.dto.HotBookVO;
import java.util.List;

public interface StatsService {
    DashboardVO dashboard();
    List<HotBookVO> hotBooks(Integer topN);
}
