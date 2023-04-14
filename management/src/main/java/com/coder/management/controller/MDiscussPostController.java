package com.coder.management.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.coder.management.controller.utils.R;
import com.coder.management.entity.DiscussPost;
import com.coder.management.service.IDiscussPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/management/discussPosts")
public class MDiscussPostController {

    @Autowired
    private IDiscussPostService discussPostService;

    @GetMapping
    public R getAll() {
        return new R(true, discussPostService.list());
    }

    // 请求体参数
    @PostMapping
    public R save(@RequestBody DiscussPost discussPost) throws IOException {
//        if (true) throw new IOException();
        boolean flag = discussPostService.save(discussPost);
        return new R(flag, flag ? "添加成功" : "添加失败");
    }

    @PutMapping
    public R update(@RequestBody DiscussPost discussPost) {
        return new R(discussPostService.updateById(discussPost));
    }

    // 路径参数
    @DeleteMapping("{id}")
    public R delete(@PathVariable Integer id) {
        return new R(discussPostService.removeById(id));
    }

    @GetMapping("{id}")
    public R getById(@PathVariable Integer id) {
        return new R(true, discussPostService.getById(id));
    }

    // 带查询的参数, discussPost 会自动封装着前端传递过来的参数
    @GetMapping("{currentPage}/{pageSize}")
    public R getPage(@PathVariable int currentPage, @PathVariable int pageSize, DiscussPost discussPost) {


        IPage<DiscussPost> page = discussPostService.getPage(currentPage, pageSize, discussPost);
        // 解决最后一页删除最后一个数据显示空页面的bug,也可直接跳到第一页
        if (currentPage > page.getPages()) {
            page = discussPostService.getPage((int) page.getPages(), pageSize, discussPost);
        }
        return new R(true, page);
    }


}
