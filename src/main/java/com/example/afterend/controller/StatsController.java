package com.example.afterend.controller;

import com.example.afterend.common.R;
import com.example.afterend.dto.DashboardVO;
import com.example.afterend.dto.HotBookVO;
import com.example.afterend.service.StatsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "统计模块")
@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @Operation(summary ="仪表盘数据")
    @GetMapping("/dashboard")
    public R<DashboardVO> dashboard() {
        return R.ok(statsService.dashboard());
    }

    @Operation(summary ="热门图书排行")
    @GetMapping("/hot-books")
    public R<List<HotBookVO>> hotBooks(@RequestParam(required = false, defaultValue = "10") Integer topN) {
        return R.ok(statsService.hotBooks(topN));
    }
}
