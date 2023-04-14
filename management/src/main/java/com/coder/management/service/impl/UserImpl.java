package com.coder.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coder.management.dao.UserDao;
import com.coder.management.entity.User;
import com.coder.management.service.IUserService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserImpl extends ServiceImpl<UserDao, User> implements IUserService {

    @Autowired
    private UserDao userDao;

    @Override
    public IPage<User> getPage(int currentPage, int pageSize) {
        IPage page = new Page(currentPage, pageSize);
        userDao.selectPage(page, null);
        return page;
    }

    @Override
    public IPage<User> getPage(int currentPage, int pageSize, User user) {

        System.out.println(user.toString());

        // 查询条件
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(Strings.isNotEmpty(user.getUsername()), User::getUsername, user.getUsername());
        lambdaQueryWrapper.like(Strings.isNotEmpty(user.getEmail()), User::getEmail, user.getEmail());
        if (user.getType() != -1) {
            lambdaQueryWrapper.like(true, User::getType, user.getType());
        }

        if (user.getStatus() != -1) {
            lambdaQueryWrapper.like(true, User::getStatus, user.getStatus());
        }

        IPage page = new Page(currentPage, pageSize);
        userDao.selectPage(page, lambdaQueryWrapper);
        return page;
    }
}
