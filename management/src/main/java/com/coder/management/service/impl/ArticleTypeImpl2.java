package com.coder.management.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coder.management.dao.ArticleTypeDao;
import com.coder.management.entity.ArticleType;
import com.coder.management.service.ArticleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Deprecated
public class ArticleTypeImpl2 implements ArticleTypeService {

    @Autowired
    private ArticleTypeDao articleTypeDao;

    @Override
    public Boolean save(ArticleType articleType) {
        return articleTypeDao.insert(articleType) > 0;
    }

    @Override
    public Boolean update(ArticleType articleType) {
        return articleTypeDao.updateById(articleType) > 0;
    }

    @Override
    public Boolean delete(ArticleType articleType) {
        return articleTypeDao.deleteById(articleType) > 0;
    }

    @Override
    public ArticleType getById(Integer id) {
        return articleTypeDao.selectById(id);
    }

    @Override
    public List<ArticleType> getAll() {
        return articleTypeDao.selectList(null);
    }

    @Override
    public IPage<ArticleType> getPage(int currentPage, int pageSize) {
        IPage page = new Page(currentPage, pageSize);
        articleTypeDao.selectPage(page, null);
        return page;
    }

}
