package com.example.afterend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.afterend.common.PageResult;
import com.example.afterend.common.R;
import com.example.afterend.entity.Book;
import com.example.afterend.service.BookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "图书模块")
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @Operation(summary ="分页查询图书")
    @GetMapping("/page")
    public R<PageResult<Book>> pageBooks(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
                                          @RequestParam(required = false) String keyword,
                                          @RequestParam(required = false) String category,
                                          @RequestParam(required = false) String status) {
        Page<Book> page = bookService.pageBooks(pageNum, pageSize, keyword, category, status);
        return R.ok(new PageResult<>(page.getTotal(), page.getRecords(), pageNum, pageSize, (int) page.getPages()));
    }

    @Operation(summary ="图书详情")
    @GetMapping("/{id}")
    public R<Book> getById(@PathVariable Long id) {
        return R.ok(bookService.getById(id));
    }

    @Operation(summary ="新增图书")
    @PostMapping
    public R<Long> add(@RequestBody Book book) {
        return R.ok("新增成功", bookService.add(book));
    }

    @Operation(summary ="修改图书")
    @PutMapping("/{id}")
    public R<?> update(@PathVariable Long id, @RequestBody Book book) {
        book.setBookId(id);
        bookService.update(book);
        return R.ok("修改成功", null);
    }

    @Operation(summary ="删除图书")
    @DeleteMapping("/{id}")
    public R<?> delete(@PathVariable Long id) {
        bookService.delete(id);
        return R.ok("删除成功", null);
    }

    @Operation(summary ="获取图书分类列表")
    @GetMapping("/categories")
    public R<List<String>> categories() {
        return R.ok(bookService.categories());
    }
}
