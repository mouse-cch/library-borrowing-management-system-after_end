package com.example.afterend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.afterend.common.PageResult;
import com.example.afterend.common.R;
import com.example.afterend.dto.*;
import com.example.afterend.entity.Inventory;
import com.example.afterend.service.StockService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "馆藏管理模块")
@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @Operation(summary ="图书入库")
    @PostMapping("/in")
    public R<?> stockIn(@RequestBody StockInDTO dto) {
        stockService.stockIn(dto);
        return R.ok("入库成功", null);
    }

    @Operation(summary ="图书报废")
    @PostMapping("/scrap")
    public R<?> scrap(@RequestBody StockScrapDTO dto) {
        stockService.scrap(dto);
        return R.ok("报废登记成功", null);
    }

    @Operation(summary ="遗失登记")
    @PostMapping("/lost")
    public R<?> lost(@RequestBody StockLostDTO dto) {
        stockService.lost(dto);
        return R.ok("遗失登记成功", null);
    }

    @Operation(summary ="查询库存")
    @GetMapping("/inventory")
    public R<List<Inventory>> inventory(@RequestParam(required = false) Long bookId,
                                         @RequestParam(required = false) String category) {
        return R.ok(stockService.inventory(bookId, category));
    }

    @Operation(summary ="盘点核对")
    @PostMapping("/inventory/check")
    public R<List<InventoryCheckResult>> check(@RequestBody List<InventoryCheckDTO> checks) {
        return R.ok(stockService.check(checks));
    }

    @Operation(summary ="馆藏差异报告")
    @GetMapping("/diff-report")
    public R<PageResult<Inventory>> diffReport(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
                                                @RequestParam(required = false) String status) {
        Page<Inventory> page = stockService.diffReport(pageNum, pageSize, status);
        return R.ok(new PageResult<>(page.getTotal(), page.getRecords(), pageNum, pageSize, (int) page.getPages()));
    }
}
