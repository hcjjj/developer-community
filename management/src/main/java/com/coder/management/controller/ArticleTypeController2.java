package com.coder.management.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.coder.management.entity.ArticleType;
import com.coder.management.service.IArticleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RestController
@Deprecated
@RequestMapping("/management/types")
public class ArticleTypeController2 {

    @Autowired
    private IArticleTypeService articleTypeService;

    @GetMapping
    public List<ArticleType> getAll() {
        return articleTypeService.list();
    }

    // 请求体参数
    @PostMapping
    public boolean save(@RequestBody ArticleType articleType) {
        return articleTypeService.save(articleType);
    }

    @PutMapping
    public boolean update(@RequestBody ArticleType articleType) {
        return articleTypeService.updateById(articleType);
    }

    // 路径参数
    @DeleteMapping("{id}")
    public boolean delete(@PathVariable Integer id) {
        return articleTypeService.removeById(id);
    }

    @GetMapping("{id}")
    public ArticleType getById(@PathVariable Integer id) {
        return articleTypeService.getById(id);
    }

    @GetMapping("{currentPage}/{pageSize}")
    public IPage<ArticleType> getPage(@PathVariable int currentPage, @PathVariable int pageSize) {
        return articleTypeService.getPage(currentPage, pageSize);

    }


}
