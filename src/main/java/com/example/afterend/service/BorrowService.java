package com.example.afterend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.afterend.dto.*;
import com.example.afterend.entity.Borrow;
import java.util.List;

public interface BorrowService {
    Borrow borrow(BorrowDTO dto);
    List<BorrowResult> batchBorrow(BatchBorrowDTO dto);
    void renew(Long borrowId, Long userId);
    void returnBook(Long borrowId);
    Page<BorrowVO> pageBorrows(Integer pageNum, Integer pageSize, Long userId, String status);
    Page<OverdueVO> pageOverdue(Integer pageNum, Integer pageSize, String keyword);
}
