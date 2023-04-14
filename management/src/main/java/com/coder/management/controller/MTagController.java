package com.coder.management.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.coder.management.controller.utils.R;
import com.coder.management.entity.Tag;
import com.coder.management.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/management/tags")
public class MTagController {

    @Autowired
    private ITagService tagService;

    @GetMapping
    public R getAll() {
        return new R(true, tagService.list());
    }

    // 请求体参数
    @PostMapping
    public R save(@RequestBody Tag tag) throws IOException {
//        if (true) throw new IOException();
        boolean flag = tagService.save(tag);
        return new R(flag, flag ? "添加成功" : "添加失败");
    }

    @PutMapping
    public R update(@RequestBody Tag tag) {
        return new R(tagService.updateById(tag));
    }

    // 路径参数
    @DeleteMapping("{id}")
    public R delete(@PathVariable Integer id) {
        return new R(tagService.removeById(id));
    }

    @GetMapping("{id}")
    public R getById(@PathVariable Integer id) {
        return new R(true, tagService.getById(id));
    }

    // 不接查询的参数
//    @GetMapping("{currentPage}/{pageSize}")
//    public R getPage(@PathVariable int currentPage, @PathVariable int pageSize) {
//        IPage<tag> page = tagService.getPage(currentPage, pageSize);
//        // 解决最后一页删除最后一个数据显示空页面的bug,也可直接跳到第一页
//        if (currentPage > page.getPages()) {
//            page = tagService.getPage((int) page.getPages(), pageSize);
//        }
//        return new R(true, page);
////        return new R(true, tagService.getPage(currentPage, pageSize));
//    }

    // 带查询的参数, tag 会自动封装着前端传递过来的参数
    @GetMapping("{currentPage}/{pageSize}")
    public R getPage(@PathVariable int currentPage, @PathVariable int pageSize,Tag tag) {


        IPage<Tag> page = tagService.getPage(currentPage, pageSize, tag);
        // 解决最后一页删除最后一个数据显示空页面的bug,也可直接跳到第一页
        if (currentPage > page.getPages()) {
            page = tagService.getPage((int) page.getPages(), pageSize, tag);
        }
        return new R(true, page);
//        return new R(true, tagService.getPage(currentPage, pageSize));
    }



}
