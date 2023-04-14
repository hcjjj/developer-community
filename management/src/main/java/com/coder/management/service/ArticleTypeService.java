package com.coder.management.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.coder.management.entity.ArticleType;

import java.util.List;


@Deprecated
public interface ArticleTypeService {
    Boolean save(ArticleType articleType);
    Boolean update(ArticleType articleType);
    Boolean delete(ArticleType articleType);
    ArticleType getById(Integer id);
    List<ArticleType> getAll();
    IPage<ArticleType> getPage(int currentPage, int pageSize);


}
