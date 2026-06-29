package com.example.afterend.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.afterend.dto.*;
import com.example.afterend.entity.*;
import com.example.afterend.exception.BusinessException;
import com.example.afterend.mapper.*;
import com.example.afterend.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final BookMapper bookMapper;
    private final InventoryMapper inventoryMapper;
    private final FeeMapper feeMapper;

    @Override
    @Transactional
    public void stockIn(StockInDTO dto) {
        Book book = bookMapper.selectById(dto.getBookId());
        if (book == null) throw new BusinessException("图书不存在");
        book.setTotalStock(book.getTotalStock() + dto.getQuantity());
        book.setCurrentStock(book.getCurrentStock() + dto.getQuantity());
        bookMapper.updateById(book);

        Inventory inv = inventoryMapper.selectOne(new LambdaQueryWrapper<Inventory>().eq(Inventory::getBookId, dto.getBookId()));
        if (inv != null) {
            inv.setTotalStock(book.getTotalStock());
            inventoryMapper.updateById(inv);
        }
    }

    @Override
    @Transactional
    public void scrap(StockScrapDTO dto) {
        Book book = bookMapper.selectById(dto.getBookId());
        if (book == null) throw new BusinessException("图书不存在");
        if (book.getCurrentStock() < dto.getQuantity()) throw new BusinessException("当前库存不足");
        book.setTotalStock(book.getTotalStock() - dto.getQuantity());
        book.setCurrentStock(book.getCurrentStock() - dto.getQuantity());
        book.setScrapStatus("scrapped");
        bookMapper.updateById(book);

        Inventory inv = inventoryMapper.selectOne(new LambdaQueryWrapper<Inventory>().eq(Inventory::getBookId, dto.getBookId()));
        if (inv != null) {
            inv.setTotalStock(book.getTotalStock());
            inventoryMapper.updateById(inv);
        }
    }

    @Override
    @Transactional
    public void lost(StockLostDTO dto) {
        Book book = bookMapper.selectById(dto.getBookId());
        if (book == null) throw new BusinessException("图书不存在");
        if (book.getCurrentStock() <= 0) throw new BusinessException("库存不足，无法登记遗失");
        book.setCurrentStock(book.getCurrentStock() - 1);
        bookMapper.updateById(book);

        // 生成赔款费用
        Fee fee = new Fee();
        fee.setUserId(dto.getUserId());
        fee.setBookId(dto.getBookId());
        fee.setFeeType("lost");
        fee.setAmount(dto.getCompensateAmount());
        fee.setReason(dto.getReason());
        fee.setStatus("unpaid");
        feeMapper.insert(fee);
    }

    @Override
    public List<Inventory> inventory(Long bookId, String category) {
        LambdaQueryWrapper<Inventory> qw = new LambdaQueryWrapper<>();
        if (bookId != null) qw.eq(Inventory::getBookId, bookId);
        return inventoryMapper.selectList(qw);
    }

    @Override
    public List<InventoryCheckResult> check(List<InventoryCheckDTO> checks) {
        List<InventoryCheckResult> results = new ArrayList<>();
        for (InventoryCheckDTO dto : checks) {
            Book book = bookMapper.selectById(dto.getBookId());
            Inventory inv = inventoryMapper.selectOne(new LambdaQueryWrapper<Inventory>().eq(Inventory::getBookId, dto.getBookId()));
            InventoryCheckResult result = new InventoryCheckResult();
            result.setBookId(dto.getBookId());
            result.setBookName(book != null ? book.getBookName() : "未知");
            result.setSystemStock(inv != null ? inv.getTotalStock() : 0);
            result.setActualStock(dto.getActualStock());
            int diff = dto.getActualStock() - (inv != null ? inv.getTotalStock() : 0);
            result.setDiff(diff);
            result.setStatus(diff == 0 ? "normal" : (diff > 0 ? "surplus" : "shortage"));

            if (inv != null) {
                inv.setActualStock(dto.getActualStock());
                inv.setLastCheckTime(LocalDateTime.now());
                inv.setStatus(result.getStatus());
                inventoryMapper.updateById(inv);
            }
            results.add(result);
        }
        return results;
    }

    @Override
    public Page<Inventory> diffReport(Integer pageNum, Integer pageSize, String status) {
        LambdaQueryWrapper<Inventory> qw = new LambdaQueryWrapper<>();
        if (status != null && !status.isEmpty()) qw.eq(Inventory::getStatus, status);
        qw.ne(Inventory::getStatus, "normal");
        qw.orderByDesc(Inventory::getLastCheckTime);
        return inventoryMapper.selectPage(new Page<>(pageNum, pageSize), qw);
    }
}
