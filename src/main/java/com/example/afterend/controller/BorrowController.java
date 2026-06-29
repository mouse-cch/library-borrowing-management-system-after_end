package com.example.afterend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.afterend.common.PageResult;
import com.example.afterend.common.R;
import com.example.afterend.dto.*;
import com.example.afterend.entity.Borrow;
import com.example.afterend.service.BorrowService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "借阅模块")
@RestController
@RequestMapping("/api/borrows")
@RequiredArgsConstructor
public class BorrowController {

    private final BorrowService borrowService;

    @Operation(summary ="借阅登记")
    @PostMapping
    public R<Borrow> borrow(@RequestBody BorrowDTO dto) {
        return R.ok("借阅成功", borrowService.borrow(dto));
    }

    @Operation(summary ="批量借阅")
    @PostMapping("/batch")
    public R<List<BorrowResult>> batchBorrow(@RequestBody BatchBorrowDTO dto) {
        return R.ok(borrowService.batchBorrow(dto));
    }

    @Operation(summary ="续借")
    @PostMapping("/{borrowId}/renew")
    public R<?> renew(@PathVariable Long borrowId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        borrowService.renew(borrowId, userId);
        return R.ok("续借成功", null);
    }

    @Operation(summary ="归还登记")
    @PostMapping("/{borrowId}/return")
    public R<?> returnBook(@PathVariable Long borrowId) {
        borrowService.returnBook(borrowId);
        return R.ok("归还成功", null);
    }

    @Operation(summary ="分页查询借阅记录")
    @GetMapping("/page")
    public R<PageResult<BorrowVO>> pageBorrows(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
                                              @RequestParam(required = false) Long userId,
                                              @RequestParam(required = false) String status) {
        Page<BorrowVO> page = borrowService.pageBorrows(pageNum, pageSize, userId, status);
        return R.ok(new PageResult<>(page.getTotal(), page.getRecords(), pageNum, pageSize, (int) page.getPages()));
    }

    @Operation(summary ="逾期记录查询")
    @GetMapping("/overdue")
    public R<PageResult<OverdueVO>> overdue(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
                                          @RequestParam(required = false) String keyword) {
        Page<OverdueVO> page = borrowService.pageOverdue(pageNum, pageSize, keyword);
        return R.ok(new PageResult<>(page.getTotal(), page.getRecords(), pageNum, pageSize, (int) page.getPages()));
    }
}
