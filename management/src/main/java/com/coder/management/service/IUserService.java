package com.coder.management.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coder.management.entity.User;
import com.coder.management.entity.User;

public interface IUserService extends IService<User> {

    IPage<User> getPage(int currentPage, int pageSize);

    // 方法重载 articleType为查询的条件
    IPage<User> getPage(int currentPage, int pageSize, User User);

}
