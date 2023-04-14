package com.coder.management.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coder.management.entity.ArticleType;

public interface IArticleTypeService extends IService<ArticleType> {

    IPage<ArticleType> getPage(int currentPage, int pageSize);

    // 方法重载 articleType为查询的条件
    IPage<ArticleType> getPage(int currentPage, int pageSize, ArticleType articleType);

}
