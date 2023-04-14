package com.coder.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coder.management.dao.ArticleTypeDao;
import com.coder.management.entity.ArticleType;
import com.coder.management.service.IArticleTypeService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleTypeImpl extends ServiceImpl<ArticleTypeDao, ArticleType> implements IArticleTypeService {

    @Autowired
    private ArticleTypeDao articleTypeDao;

    @Override
    public IPage<ArticleType> getPage(int currentPage, int pageSize) {
        IPage page = new Page(currentPage, pageSize);
        articleTypeDao.selectPage(page, null);
        return page;
    }

    @Override
    public IPage<ArticleType> getPage(int currentPage, int pageSize, ArticleType articleType) {

//        System.out.println(articleType.toString());

        // 查询条件
        LambdaQueryWrapper<ArticleType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(Strings.isNotEmpty(articleType.getName()), ArticleType::getName, articleType.getName());
        lambdaQueryWrapper.like(Strings.isNotEmpty(articleType.getDescription()), ArticleType::getDescription, articleType.getDescription());
        if (articleType.getIsDelete() == 0 || articleType.getIsDelete() == 1) {
            lambdaQueryWrapper.like(true, ArticleType::getIsDelete, articleType.getIsDelete());
        }
        IPage page = new Page(currentPage, pageSize);
        articleTypeDao.selectPage(page, lambdaQueryWrapper);
        return page;
    }
}
