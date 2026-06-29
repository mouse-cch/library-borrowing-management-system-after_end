package com.example.afterend.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.afterend.entity.Book;
import com.example.afterend.entity.Inventory;
import com.example.afterend.exception.BusinessException;
import com.example.afterend.mapper.BookMapper;
import com.example.afterend.mapper.InventoryMapper;
import com.example.afterend.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookMapper bookMapper;
    private final InventoryMapper inventoryMapper;

    @Override
    public Page<Book> pageBooks(Integer pageNum, Integer pageSize, String keyword, String category, String status) {
        LambdaQueryWrapper<Book> qw = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            qw.and(w -> w.like(Book::getBookName, keyword).or().like(Book::getAuthor, keyword).or().like(Book::getIsbn, keyword));
        }
        if (StringUtils.hasText(category)) qw.eq(Book::getCategory, category);
        if ("available".equals(status)) qw.gt(Book::getCurrentStock, 0);
        else if ("unavailable".equals(status)) qw.eq(Book::getCurrentStock, 0);
        qw.orderByDesc(Book::getCreateTime);
        return bookMapper.selectPage(new Page<>(pageNum, pageSize), qw);
    }

    @Override
    public Book getById(Long id) {
        return bookMapper.selectById(id);
    }

    @Override
    @Transactional
    public Long add(Book book) {
        book.setCurrentStock(book.getTotalStock());
        bookMapper.insert(book);
        Inventory inv = new Inventory();
        inv.setBookId(book.getBookId());
        inv.setTotalStock(book.getTotalStock());
        inv.setStatus("normal");
        inventoryMapper.insert(inv);
        return book.getBookId();
    }

    @Override
    public void update(Book book) {
        bookMapper.updateById(book);
    }

    @Override
    public void delete(Long id) {
        bookMapper.deleteById(id);
    }

    @Override
    public List<String> categories() {
        return bookMapper.selectList(null).stream()
                .map(Book::getCategory)
                .filter(StringUtils::hasText)
                .distinct()
                .sorted()
                .toList();
    }
}
