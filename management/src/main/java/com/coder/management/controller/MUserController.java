package com.coder.management.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.coder.management.controller.utils.R;
import com.coder.management.entity.User;
import com.coder.management.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/management/users")
public class MUserController {

    @Autowired
    private IUserService userService;

    @GetMapping
    public R getAll() {
        return new R(true, userService.list());
    }

    // 请求体参数
    @PostMapping
    public R save(@RequestBody User user) throws IOException {
//        if (true) throw new IOException();
        boolean flag = userService.save(user);
        return new R(flag, flag ? "添加成功" : "添加失败");
    }

    @PutMapping
    public R update(@RequestBody User user) {
        return new R(userService.updateById(user));
    }

    // 路径参数
    @DeleteMapping("{id}")
    public R delete(@PathVariable Integer id) {
        return new R(userService.removeById(id));
    }

    @GetMapping("{id}")
    public R getById(@PathVariable Integer id) {
        return new R(true, userService.getById(id));
    }

    // 带查询的参数, user 会自动封装着前端传递过来的参数
    @GetMapping("{currentPage}/{pageSize}")
    public R getPage(@PathVariable int currentPage, @PathVariable int pageSize,User user) {


        IPage<User> page = userService.getPage(currentPage, pageSize, user);
        // 解决最后一页删除最后一个数据显示空页面的bug,也可直接跳到第一页
        if (currentPage > page.getPages()) {
            page = userService.getPage((int) page.getPages(), pageSize, user);
        }
        return new R(true, page);
    }



}
