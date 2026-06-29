package com.example.afterend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.afterend.common.PageResult;
import com.example.afterend.common.R;
import com.example.afterend.dto.ReserveDTO;
import com.example.afterend.dto.ReserveVO;
import com.example.afterend.entity.Reservation;
import com.example.afterend.service.ReserveService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "预约模块")
@RestController
@RequestMapping("/api/reserves")
@RequiredArgsConstructor
public class ReserveController {

    private final ReserveService reserveService;

    @Operation(summary ="预约图书")
    @PostMapping
    public R<Reservation> reserve(@RequestBody ReserveDTO dto) {
        return R.ok("预约成功", reserveService.reserve(dto));
    }

    @Operation(summary ="分页查询预约记录")
    @GetMapping("/page")
    public R<PageResult<ReserveVO>> pageReserves(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
                                                    @RequestParam(required = false) Long userId,
                                                    @RequestParam(required = false) String status) {
        Page<ReserveVO> page = reserveService.pageReserves(pageNum, pageSize, userId, status);
        return R.ok(new PageResult<>(page.getTotal(), page.getRecords(), pageNum, pageSize, (int) page.getPages()));
    }

    @Operation(summary ="取消预约")
    @DeleteMapping("/{reserveId}")
    public R<?> cancel(@PathVariable Long reserveId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        reserveService.cancel(reserveId, userId);
        return R.ok("预约已取消", null);
    }
}
