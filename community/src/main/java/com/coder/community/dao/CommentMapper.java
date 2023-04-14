package com.coder.community.dao;

import com.coder.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

// SpringBoot整合Mybatis要加这个注解
@Mapper
public interface CommentMapper {

    // 根据类型查询评论
    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    // 根据类型查询评论数
    int selectCountByEntity(int entityType, int entityId);

    //增加评论
    int insertComment(Comment comment);

    // 根据评论id查询评论
    Comment selectCommentById(int id);

    // 分页查询用户你有关回复
    List<Comment> selectCommentsByUser(int userId, int offset, int limit);

    // 查询用户的回复数量
    int selectCountByUser(int userId);

}
