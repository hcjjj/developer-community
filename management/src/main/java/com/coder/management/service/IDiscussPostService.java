package com.coder.management.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coder.management.entity.DiscussPost;

public interface IDiscussPostService extends IService<DiscussPost> {

    IPage<DiscussPost> getPage(int currentPage, int pageSize);

    // 方法重载 articleType为查询的条件
    IPage<DiscussPost> getPage(int currentPage, int pageSize, DiscussPost DiscussPost);

}
