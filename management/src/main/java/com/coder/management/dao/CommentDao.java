package com.coder.management.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coder.management.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentDao extends BaseMapper<Comment> {

}
