package com.example.afterend.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.afterend.dto.ReserveDTO;
import com.example.afterend.dto.ReserveVO;
import com.example.afterend.entity.Book;
import com.example.afterend.entity.Reservation;
import com.example.afterend.exception.BusinessException;
import com.example.afterend.mapper.BookMapper;
import com.example.afterend.mapper.ReservationMapper;
import com.example.afterend.service.ReserveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReserveServiceImpl implements ReserveService {

    private final ReservationMapper reservationMapper;
    private final BookMapper bookMapper;

    @Override
    @Transactional
    public Reservation reserve(ReserveDTO dto) {
        Book book = bookMapper.selectById(dto.getBookId());
        if (book == null) throw new BusinessException("图书不存在");

        // 计算排队号
        LambdaQueryWrapper<Reservation> qw = new LambdaQueryWrapper<>();
        qw.eq(Reservation::getBookId, dto.getBookId()).eq(Reservation::getStatus, "waiting");
        long count = reservationMapper.selectCount(qw);
        int queueNo = (int) count + 1;

        Reservation reservation = new Reservation();
        reservation.setBookId(dto.getBookId());
        reservation.setUserId(dto.getUserId());
        reservation.setQueueNo(queueNo);
        reservation.setStatus("waiting");
        reservationMapper.insert(reservation);
        return reservation;
    }

    @Override
    public Page<ReserveVO> pageReserves(Integer pageNum, Integer pageSize, Long userId, String status) {
        LambdaQueryWrapper<Reservation> qw = new LambdaQueryWrapper<>();
        if (userId != null) qw.eq(Reservation::getUserId, userId);
        if (status != null) qw.eq(Reservation::getStatus, status);
        qw.orderByDesc(Reservation::getReserveTime);
        Page<Reservation> rawPage = reservationMapper.selectPage(new Page<>(pageNum, pageSize), qw);

        List<ReserveVO> voList = rawPage.getRecords().stream().map(r -> {
            ReserveVO vo = new ReserveVO();
            vo.setReserveId(r.getReserveId());
            vo.setBookId(r.getBookId());
            Book book = bookMapper.selectById(r.getBookId());
            vo.setBookName(book != null ? book.getBookName() : "未知");
            vo.setUserId(r.getUserId());
            vo.setReserveTime(r.getReserveTime());
            vo.setQueueNo(r.getQueueNo());
            vo.setStatus(r.getStatus());
            vo.setCreateTime(r.getCreateTime());
            return vo;
        }).toList();

        Page<ReserveVO> voPage = new Page<>(pageNum, pageSize, rawPage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    @Transactional
    public void cancel(Long reserveId, Long userId) {
        Reservation r = reservationMapper.selectById(reserveId);
        if (r == null) throw new BusinessException("预约记录不存在");
        if (!r.getUserId().equals(userId)) throw new BusinessException(403, "无权取消他人预约");
        r.setStatus("cancelled");
        reservationMapper.updateById(r);
    }
}
