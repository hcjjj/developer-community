package com.coder.management.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.coder.management.controller.utils.R;
import com.coder.management.entity.Comment;
import com.coder.management.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/management/comments")
public class MCommentController {

    @Autowired
    private ICommentService commentService;

    @GetMapping
    public R getAll() {
        return new R(true, commentService.list());
    }

    // 请求体参数
    @PostMapping
    public R save(@RequestBody Comment comment) throws IOException {
//        if (true) throw new IOException();
        boolean flag = commentService.save(comment);
        return new R(flag, flag ? "添加成功" : "添加失败");
    }

    @PutMapping
    public R update(@RequestBody Comment comment) {
        return new R(commentService.updateById(comment));
    }

    // 路径参数
    @DeleteMapping("{id}")
    public R delete(@PathVariable Integer id) {
        return new R(commentService.removeById(id));
    }

    @GetMapping("{id}")
    public R getById(@PathVariable Integer id) {
        return new R(true, commentService.getById(id));
    }

    // 带查询的参数, comment 会自动封装着前端传递过来的参数
    @GetMapping("{currentPage}/{pageSize}")
    public R getPage(@PathVariable int currentPage, @PathVariable int pageSize,Comment comment) {


        IPage<Comment> page = commentService.getPage(currentPage, pageSize, comment);
        // 解决最后一页删除最后一个数据显示空页面的bug,也可直接跳到第一页
        if (currentPage > page.getPages()) {
            page = commentService.getPage((int) page.getPages(), pageSize, comment);
        }
        return new R(true, page);
    }



}
