package com.coder.community.dao;

import com.coder.community.entity.ArticleType;
import com.coder.community.entity.DiscussPost;
import com.coder.community.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    // 分页查询，返回的是多条数据，用一个集合来接
    // userId 用于个人用户中的"我的帖子"，可为空（动态SQL语句）
    // offset 每一页起始行行号，通过计算而来
    // limit 每一页最多显示多少条数据
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit, int orderMode, int typeId);


    // 获取关注者/收藏的帖子
    List<DiscussPost> selectPartDiscussPosts(List<Integer> list, int offset, int limit, int mode);

    // 获取关注者的帖子数量
    int selectPartDiscussPostRows(List<Integer> list, int mode);


    // @Param注解用于给参数取别名,
    // 如果只有一个参数,并且在<if>里(动态SQL)使用,则必须加别名.
    //        where status != 2
    //        <if test="userId!=0">
    //            and user_id = #{userId}
    //        </if>
    // 查询帖子的总数量
    int selectDiscussPostRows(@Param("userId") int userId);

    // 增加帖子方法声明
    int insertDiscussPost(DiscussPost discussPost);

    // 根据id查询帖子数据
    DiscussPost selectDiscussPostById(int id);

    // 修改帖子评论数量
    int updateCommentCount(int id, int commentCount);

    // 修改帖子状态(置顶)
    int updateType(int id, int type);

    // 修改帖子状态(加精、删除)
    int updateStatus(int id, int status);

    // 修改帖子分数
    int updateScore(int id, double score);

    // 查询所有分类的名称
    List<ArticleType> selectArticleType();

    // 根据帖子id查询对应的类别
    ArticleType selectArticleTypeByPostId(int postId);

    // 根据类型id查询对应类型
    ArticleType selectArticleTypeById(int typeId);

    // 修改分类的引用数量
    int addTypeRefCount(int typeId);

    int reduceTypeRefCount(int typeId);

    // 分页查询某个分类的帖子 可以直接复用之前的分页查询
    //    List<DiscussPost> selectDiscussPostsByTypeId(int userId, int offset, int limit, int orderMode);

    // 查询标签数据 (1 表示前30）
    List<Tag> selectArticleTags(int mode);

    // 根据文章id查询对应的标签
    List<Tag> selectArticleTagsById(int postId);

    // 查询所有的标签分组
    @Select({"SELECT DISTINCT group_name from tag"})
    List<Tag> selectTagGroupNames();

    // 根据标签id查询对应的标签
    Tag selectTagById(int tagId);

    // 根据分组名查询对应的标签
    List<Tag> selectTagsByGroupName(@Param("groupName") String groupName);

    // 添加文章对应的标签
    int insertPostTags(int postId, @Param("list") List<Integer> tagsId);

    // 增加标签的引用数量
    int addTagsRefCount(@Param("list") List<Integer> tagsId);

    // 减少标签的引用数量
    int reduceTagsRefCount(@Param("list") List<Integer> tagsId);

    // 删除文章对应的标签
    int deletePostTags(int postId);

    // 根据标签查询相关的所有文章
    List<DiscussPost> selectPostsByTagId(int tagId, int offset, int limit);
    // 根据标签id查询对应文章的数量
    int selectPostsByTagIdRows(int tagId);


}
