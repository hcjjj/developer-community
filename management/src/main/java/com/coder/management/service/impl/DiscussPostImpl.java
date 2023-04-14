package com.coder.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coder.management.dao.DiscussPostDao;
import com.coder.management.entity.DiscussPost;
import com.coder.management.service.IDiscussPostService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiscussPostImpl extends ServiceImpl<DiscussPostDao, DiscussPost> implements IDiscussPostService {

    @Autowired
    private DiscussPostDao discussPostDao;

    @Override
    public IPage<DiscussPost> getPage(int currentPage, int pageSize) {
        IPage page = new Page(currentPage, pageSize);
        discussPostDao.selectPage(page, null);
        return page;
    }

    @Override
    public IPage<DiscussPost> getPage(int currentPage, int pageSize, DiscussPost discussPost) {

        System.out.println(discussPost.toString());

        // 查询条件
        LambdaQueryWrapper<DiscussPost> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 不查询文章内容（实体类那边已经做处理了
//        lambdaQueryWrapper.select(DiscussPost.class, tableFieldInfo -> !tableFieldInfo.getColumn().equals("content"));

        // 搜索条件
        lambdaQueryWrapper.like(Strings.isNotEmpty(discussPost.getTitle()), DiscussPost::getTitle, discussPost.getTitle());
//        lambdaQueryWrapper.like(Strings.isNotEmpty(discussPost.getEmail()), DiscussPost::getEmail, discussPost.getEmail());
        if (discussPost.getType() != -1) {
            lambdaQueryWrapper.like(true, DiscussPost::getType, discussPost.getType());
        }
        if (discussPost.getStatus() != -1) {
            lambdaQueryWrapper.like(true, DiscussPost::getStatus, discussPost.getStatus());
        }

        // 按照热度降序
        if (discussPost.getSortByScore() == 1) {
            lambdaQueryWrapper.orderByDesc(true, DiscussPost::getScore);
        }

        // 按照热度升序
        if (discussPost.getSortByScore() == -1) {
            lambdaQueryWrapper.orderByAsc(true, DiscussPost::getScore);
        }

        // 按时间度降序
        if (discussPost.getSortByTime() == 1) {
            lambdaQueryWrapper.orderByDesc(true, DiscussPost::getCreateTime);
        }

        // 按照时间度升序
        if (discussPost.getSortByTime() == -1) {
            lambdaQueryWrapper.orderByAsc(true, DiscussPost::getCreateTime);
        }


        IPage page = new Page(currentPage, pageSize);
        discussPostDao.selectPage(page, lambdaQueryWrapper);
        return page;
    }
}
