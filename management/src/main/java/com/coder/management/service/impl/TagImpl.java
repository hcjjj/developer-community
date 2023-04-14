package com.coder.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coder.management.dao.TagDao;
import com.coder.management.entity.Tag;
import com.coder.management.service.ITagService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagImpl extends ServiceImpl<TagDao, Tag> implements ITagService {

    @Autowired
    private TagDao tagDao;

    @Override
    public IPage<Tag> getPage(int currentPage, int pageSize) {
        IPage page = new Page(currentPage, pageSize);
        tagDao.selectPage(page, null);
        return page;
    }

    @Override
    public IPage<Tag> getPage(int currentPage, int pageSize, Tag tag) {

//        System.out.println(tag.toString());

        // 查询条件
        LambdaQueryWrapper<Tag> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(Strings.isNotEmpty(tag.getName()), Tag::getName, tag.getName());
        lambdaQueryWrapper.like(Strings.isNotEmpty(tag.getGroupName()), Tag::getGroupName, tag.getGroupName());
        lambdaQueryWrapper.like(Strings.isNotEmpty(tag.getDescription()), Tag::getDescription, tag.getDescription());
        if (tag.getIsDelete() != -1) {
            lambdaQueryWrapper.like(true, Tag::getIsDelete, tag.getIsDelete());
        }

        IPage page = new Page(currentPage, pageSize);
        tagDao.selectPage(page, lambdaQueryWrapper);
        return page;
    }
}
