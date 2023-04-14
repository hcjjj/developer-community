package com.coder.management.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.coder.management.controller.utils.R;
import com.coder.management.entity.ArticleType;
import com.coder.management.service.IArticleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/management/types")
public class MArticleTypeController {

    @Autowired
    private IArticleTypeService articleTypeService;

    @GetMapping
    public R getAll() {
        return new R(true, articleTypeService.list());
    }

    // 请求体参数
    @PostMapping
    public R save(@RequestBody ArticleType articleType) throws IOException {
//        if (true) throw new IOException();
        boolean flag = articleTypeService.save(articleType);
        return new R(flag, flag ? "添加成功" : "添加失败");
    }

    @PutMapping
    public R update(@RequestBody ArticleType articleType) {
        return new R(articleTypeService.updateById(articleType));
    }

    // 路径参数
    @DeleteMapping("{id}")
    public R delete(@PathVariable Integer id) {
        return new R(articleTypeService.removeById(id));
    }

    @GetMapping("{id}")
    public R getById(@PathVariable Integer id) {
        return new R(true, articleTypeService.getById(id));
    }

    // 不接查询的参数
//    @GetMapping("{currentPage}/{pageSize}")
//    public R getPage(@PathVariable int currentPage, @PathVariable int pageSize) {
//        IPage<ArticleType> page = articleTypeService.getPage(currentPage, pageSize);
//        // 解决最后一页删除最后一个数据显示空页面的bug,也可直接跳到第一页
//        if (currentPage > page.getPages()) {
//            page = articleTypeService.getPage((int) page.getPages(), pageSize);
//        }
//        return new R(true, page);
////        return new R(true, articleTypeService.getPage(currentPage, pageSize));
//    }

    // 带查询的参数, articleType 会自动封装着前端传递过来的参数
    @GetMapping("{currentPage}/{pageSize}")
    public R getPage(@PathVariable int currentPage, @PathVariable int pageSize,ArticleType articleType) {


        IPage<ArticleType> page = articleTypeService.getPage(currentPage, pageSize, articleType);
        // 解决最后一页删除最后一个数据显示空页面的bug,也可直接跳到第一页
        if (currentPage > page.getPages()) {
            page = articleTypeService.getPage((int) page.getPages(), pageSize, articleType);
        }
        return new R(true, page);
//        return new R(true, articleTypeService.getPage(currentPage, pageSize));
    }



}
