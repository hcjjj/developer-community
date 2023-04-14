package com.coder.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coder.management.dao.CommentDao;
import com.coder.management.entity.Comment;
import com.coder.management.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentImpl extends ServiceImpl<CommentDao, Comment> implements ICommentService {

    @Autowired
    private CommentDao commentDao;

    @Override
    public IPage<Comment> getPage(int currentPage, int pageSize) {
        IPage page = new Page(currentPage, pageSize);
        commentDao.selectPage(page, null);
        return page;
    }

    @Override
    public IPage<Comment> getPage(int currentPage, int pageSize, Comment comment) {

        System.out.println(comment.toString());

        // 查询条件
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.like(Strings.isNotEmpty(comment.getCommentname()), Comment::getCommentname, comment.getCommentname());
//        lambdaQueryWrapper.like(Strings.isNotEmpty(comment.getEmail()), Comment::getEmail, comment.getEmail());
//        if (comment.getType() != -1) {
//            lambdaQueryWrapper.like(true, Comment::getType, comment.getType());
//        }
//
//        if (comment.getStatus() != -1) {
//            lambdaQueryWrapper.like(true, Comment::getStatus, comment.getStatus());
//        }

        IPage page = new Page(currentPage, pageSize);
        commentDao.selectPage(page, lambdaQueryWrapper);
        return page;
    }
}
