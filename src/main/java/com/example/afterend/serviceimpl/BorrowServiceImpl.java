package com.example.afterend.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.afterend.constant.BorrowRule;
import com.example.afterend.dto.*;
import com.example.afterend.entity.*;
import com.example.afterend.exception.BusinessException;
import com.example.afterend.mapper.*;
import com.example.afterend.service.BorrowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BorrowServiceImpl implements BorrowService {

    private final BorrowMapper borrowMapper;
    private final BookMapper bookMapper;
    private final UserMapper userMapper;
    private final FeeMapper feeMapper;
    private final ReservationMapper reservationMapper;
    private final InventoryMapper inventoryMapper;

    @Override
    @Transactional
    public Borrow borrow(BorrowDTO dto) {
        User user = userMapper.selectById(dto.getUserId());
        if (user == null) throw new BusinessException("用户不存在");
        Book book = bookMapper.selectById(dto.getBookId());
        if (book == null) throw new BusinessException("图书不存在");
        if (book.getCurrentStock() <= 0) throw new BusinessException("该图书暂无库存");

        // 借阅限制
        LambdaQueryWrapper<Borrow> countQw = new LambdaQueryWrapper<>();
        countQw.eq(Borrow::getUserId, dto.getUserId()).eq(Borrow::getStatus, "borrowing");
        long currentBorrows = borrowMapper.selectCount(countQw);
        int maxBorrow = user.getMaxBorrow() != null ? user.getMaxBorrow() : BorrowRule.STUDENT_MAX;
        if (currentBorrows >= maxBorrow) throw new BusinessException("借阅数量已达上限(" + maxBorrow + "本)");

        int days = dto.getDays() != null ? dto.getDays() : (user.getBorrowDays() != null ? user.getBorrowDays() : 30);
        Borrow borrow = new Borrow();
        borrow.setBorrowNo("B" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        borrow.setBookId(dto.getBookId());
        borrow.setUserId(dto.getUserId());
        borrow.setBorrowTime(LocalDateTime.now());
        borrow.setDueTime(LocalDateTime.now().plusDays(days));
        borrow.setStatus("borrowing");
        borrow.setRenewCount(0);
        borrow.setOverdueDays(0);
        borrowMapper.insert(borrow);

        book.setCurrentStock(book.getCurrentStock() - 1);
        bookMapper.updateById(book);

        return borrow;
    }

    @Override
    @Transactional
    public List<BorrowResult> batchBorrow(BatchBorrowDTO dto) {
        List<BorrowResult> results = new ArrayList<>();
        for (Long bookId : dto.getBookIds()) {
            try {
                BorrowDTO borrowDTO = new BorrowDTO();
                borrowDTO.setBookId(bookId);
                borrowDTO.setUserId(dto.getUserId());
                Borrow b = borrow(borrowDTO);
                Book book = bookMapper.selectById(bookId);
                results.add(new BorrowResult(bookId, book != null ? book.getBookName() : "", true, b.getBorrowId(), b.getBorrowNo(), null));
            } catch (BusinessException e) {
                Book book = bookMapper.selectById(bookId);
                results.add(new BorrowResult(bookId, book != null ? book.getBookName() : "", false, null, null, e.getMessage()));
            }
        }
        return results;
    }

    @Override
    @Transactional
    public void renew(Long borrowId, Long userId) {
        Borrow borrow = borrowMapper.selectById(borrowId);
        if (borrow == null) throw new BusinessException("借阅记录不存在");
        if (!borrow.getUserId().equals(userId)) throw new BusinessException(403, "无权操作");
        if (!"borrowing".equals(borrow.getStatus())) throw new BusinessException("该记录不可续借");
        if (borrow.getRenewCount() >= BorrowRule.MAX_RENEW_COUNT) throw new BusinessException("续借次数已达上限(3次)");
        User user = userMapper.selectById(userId);
        int days = user != null && user.getBorrowDays() != null ? user.getBorrowDays() : 30;
        borrow.setDueTime(borrow.getDueTime().plusDays(days));
        borrow.setRenewCount(borrow.getRenewCount() + 1);
        borrowMapper.updateById(borrow);
    }

    @Override
    @Transactional
    public void returnBook(Long borrowId) {
        Borrow borrow = borrowMapper.selectById(borrowId);
        if (borrow == null) throw new BusinessException("借阅记录不存在");
        if (!"borrowing".equals(borrow.getStatus()) && !"overdue".equals(borrow.getStatus()))
            throw new BusinessException("该记录已归还");

        borrow.setActualReturnTime(LocalDateTime.now());
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(borrow.getDueTime())) {
            long days = ChronoUnit.DAYS.between(borrow.getDueTime(), now);
            borrow.setOverdueDays((int) days);
            // 生成逾期费用
            Fee fee = new Fee();
            fee.setUserId(borrow.getUserId());
            fee.setBorrowId(borrow.getBorrowId());
            fee.setBookId(borrow.getBookId());
            fee.setFeeType("overdue");
            fee.setAmount(BigDecimal.valueOf(days * BorrowRule.FINE_PER_DAY));
            fee.setReason("逾期" + days + "天");
            fee.setStatus("unpaid");
            feeMapper.insert(fee);
        }
        borrow.setStatus("returned");
        borrowMapper.updateById(borrow);

        // 恢复库存
        Book book = bookMapper.selectById(borrow.getBookId());
        if (book != null) {
            book.setCurrentStock(book.getCurrentStock() + 1);
            bookMapper.updateById(book);
        }

        // 检查预约队列
        LambdaQueryWrapper<Reservation> rqw = new LambdaQueryWrapper<>();
        rqw.eq(Reservation::getBookId, borrow.getBookId()).eq(Reservation::getStatus, "waiting")
          .orderByAsc(Reservation::getQueueNo);
        Reservation firstReserve = reservationMapper.selectOne(rqw, false);
        if (firstReserve != null) {
            firstReserve.setStatus("ready");
            reservationMapper.updateById(firstReserve);
        }
    }

    @Override
    public Page<BorrowVO> pageBorrows(Integer pageNum, Integer pageSize, Long userId, String status) {
        LambdaQueryWrapper<Borrow> qw = new LambdaQueryWrapper<>();
        if (userId != null) qw.eq(Borrow::getUserId, userId);
        if (status != null) {
            if ("active".equals(status)) {
                qw.in(Borrow::getStatus, "borrowing", "overdue");
            } else {
                qw.eq(Borrow::getStatus, status);
            }
        }
        qw.orderByDesc(Borrow::getBorrowTime);
        Page<Borrow> rawPage = borrowMapper.selectPage(new Page<>(pageNum, pageSize), qw);

        List<BorrowVO> voList = rawPage.getRecords().stream().map(b -> {
            BorrowVO vo = new BorrowVO();
            vo.setBorrowId(b.getBorrowId());
            vo.setBorrowNo(b.getBorrowNo());
            vo.setBookId(b.getBookId());
            Book book = bookMapper.selectById(b.getBookId());
            vo.setBookName(book != null ? book.getBookName() : "未知");
            vo.setUserId(b.getUserId());
            User user = userMapper.selectById(b.getUserId());
            vo.setUserName(user != null ? user.getRealName() : "未知");
            vo.setBorrowTime(b.getBorrowTime());
            vo.setDueTime(b.getDueTime());
            vo.setActualReturnTime(b.getActualReturnTime());
            vo.setStatus(b.getStatus());
            vo.setRenewCount(b.getRenewCount());
            vo.setOverdueDays(b.getOverdueDays());
            vo.setRemark(b.getRemark());
            vo.setCreateTime(b.getCreateTime());
            return vo;
        }).toList();

        Page<BorrowVO> voPage = new Page<>(pageNum, pageSize, rawPage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public Page<OverdueVO> pageOverdue(Integer pageNum, Integer pageSize, String keyword) {
        // 逾期 = dueTime < now AND status = 'borrowing'
        LambdaQueryWrapper<Borrow> qw = new LambdaQueryWrapper<>();
        qw.lt(Borrow::getDueTime, LocalDateTime.now())
          .eq(Borrow::getStatus, "borrowing")
          .orderByAsc(Borrow::getDueTime);
        List<Borrow> overdueList = borrowMapper.selectList(qw);
        for (Borrow b : overdueList) {
            long days = ChronoUnit.DAYS.between(b.getDueTime(), LocalDateTime.now());
            b.setOverdueDays((int) days);
            b.setStatus("overdue");
            borrowMapper.updateById(b);
        }

        LambdaQueryWrapper<Borrow> pageQw = new LambdaQueryWrapper<>();
        pageQw.eq(Borrow::getStatus, "overdue");
        if (keyword != null && !keyword.isEmpty()) {
            pageQw.and(w -> w.like(Borrow::getBorrowNo, keyword));
        }
        pageQw.orderByAsc(Borrow::getDueTime);
        Page<Borrow> rawPage = borrowMapper.selectPage(new Page<>(pageNum, pageSize), pageQw);

        List<OverdueVO> voList = rawPage.getRecords().stream().map(b -> {
            OverdueVO vo = new OverdueVO();
            vo.setBorrowId(b.getBorrowId());
            vo.setBorrowNo(b.getBorrowNo());
            vo.setBookId(b.getBookId());
            Book book = bookMapper.selectById(b.getBookId());
            vo.setBookName(book != null ? book.getBookName() : "未知");
            vo.setUserId(b.getUserId());
            User user = userMapper.selectById(b.getUserId());
            vo.setUserName(user != null ? user.getRealName() : "未知");
            vo.setDueTime(b.getDueTime());
            vo.setOverdueDays(b.getOverdueDays());
            vo.setStatus(b.getStatus());
            // 逾期罚款：每天0.5元
            vo.setFineAmount(BigDecimal.valueOf(b.getOverdueDays() * 0.5));
            return vo;
        }).toList();

        Page<OverdueVO> voPage = new Page<>(pageNum, pageSize, rawPage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }
}
