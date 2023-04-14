package com.coder.management.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coder.management.entity.Tag;

public interface ITagService extends IService<Tag> {

    IPage<Tag> getPage(int currentPage, int pageSize);

    // 方法重载 articleType为查询的条件
    IPage<Tag> getPage(int currentPage, int pageSize, Tag tag);

}
