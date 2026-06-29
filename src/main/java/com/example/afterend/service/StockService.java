package com.example.afterend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.afterend.dto.*;
import com.example.afterend.entity.Inventory;
import java.util.List;

public interface StockService {
    void stockIn(StockInDTO dto);
    void scrap(StockScrapDTO dto);
    void lost(StockLostDTO dto);
    List<Inventory> inventory(Long bookId, String category);
    List<InventoryCheckResult> check(List<InventoryCheckDTO> checks);
    Page<Inventory> diffReport(Integer pageNum, Integer pageSize, String status);
}
