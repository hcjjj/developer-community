package com.coder.management.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coder.management.entity.Comment;

public interface ICommentService extends IService<Comment> {

    IPage<Comment> getPage(int currentPage, int pageSize);

    // 方法重载 articleType为查询的条件
    IPage<Comment> getPage(int currentPage, int pageSize, Comment Comment);

}
