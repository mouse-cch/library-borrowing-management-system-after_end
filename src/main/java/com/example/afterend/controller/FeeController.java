package com.example.afterend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.afterend.common.PageResult;
import com.example.afterend.common.R;
import com.example.afterend.dto.FeeApplyDTO;
import com.example.afterend.entity.Fee;
import com.example.afterend.service.FeeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "费用模块")
@RestController
@RequestMapping("/api/fees")
@RequiredArgsConstructor
public class FeeController {

    private final FeeService feeService;

    @Operation(summary ="分页查询费用")
    @GetMapping("/page")
    public R<PageResult<Fee>> pageFees(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
                                        @RequestParam(required = false) Long userId,
                                        @RequestParam(required = false) String status) {
        Page<Fee> page = feeService.pageFees(pageNum, pageSize, userId, status);
        return R.ok(new PageResult<>(page.getTotal(), page.getRecords(), pageNum, pageSize, (int) page.getPages()));
    }

    @Operation(summary ="缴纳费用")
    @PostMapping("/{feeId}/pay")
    public R<?> pay(@PathVariable Long feeId) {
        feeService.pay(feeId);
        return R.ok("缴费成功", null);
    }

    @Operation(summary ="申请费用")
    @PostMapping("/apply")
    public R<Long> apply(@RequestBody FeeApplyDTO dto) {
        return R.ok("费用申请成功", feeService.apply(dto));
    }
}
