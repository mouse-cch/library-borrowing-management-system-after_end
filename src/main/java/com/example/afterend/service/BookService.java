package com.example.afterend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.afterend.entity.Book;
import java.util.List;

public interface BookService {
    Page<Book> pageBooks(Integer pageNum, Integer pageSize, String keyword, String category, String status);
    Book getById(Long id);
    Long add(Book book);
    void update(Book book);
    void delete(Long id);
    List<String> categories();
}
