package com.coder.community.service;

import com.coder.community.entity.ArticleType;
import com.coder.community.entity.Tag;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.coder.community.dao.DiscussPostMapper;
import com.coder.community.entity.DiscussPost;
import com.coder.community.util.SensitiveFilter;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class DiscussPostService {

    private static final Logger logger = LoggerFactory.getLogger(DiscussPostService.class);

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Value("${caffeine.posts.max-size}")
    private int maxSize;

    @Value("${caffeine.posts.expire-seconds}")
    private int expireSeconds;


    // Caffeine核心接口: Cache, LoadingCache, AsyncLoadingCache
    // 帖子列表缓存  按照key缓存value
    private LoadingCache<String, List<DiscussPost>> postListCache;

    // 帖子总数缓存
    private LoadingCache<Integer, Integer> postRowsCache;

    @PostConstruct
    public void init() {
        // 初始化帖子列表缓存
        postListCache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                .build(new CacheLoader<String, List<DiscussPost>>() {
                    @Nullable
                    @Override
                    // load：如何从数据中获取数据
                    public List<DiscussPost> load(@NonNull String key) throws Exception {
                        if (key == null || key.length() == 0) {
                            throw new IllegalArgumentException("参数错误!");
                        }

                        String[] params = key.split(":");
                        if (params == null || params.length != 2) {
                            throw new IllegalArgumentException("参数错误!");
                        }

                        // key 是由 offset 和 limit 组成的
                        int offset = Integer.valueOf(params[0]);
                        int limit = Integer.valueOf(params[1]);

                        // 这里可以加一个 二级缓存: Redis -> mysql

                        // 这里直接访问数据库了
                        logger.debug("load post list from DB.");
                        return discussPostMapper.selectDiscussPosts(0, offset, limit, 1, 0);
                    }
                });
        // 初始化帖子总数缓存
        postRowsCache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                .build(new CacheLoader<Integer, Integer>() {
                    @Nullable
                    @Override
                    public Integer load(@NonNull Integer key) throws Exception {
                        // 从数据库中获取数据到缓存
                        logger.debug("load post rows from DB.");
                        return discussPostMapper.selectDiscussPostRows(key);
                    }
                });
    }


    // 分页查询帖子数据  用调用缓存数据
    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit, int orderMode, int typeId) {

        // userId == 0 查询所有帖子  orderMode == 1热门页面
        if (userId == 0 && orderMode == 1) {
            return postListCache.get(offset + ":" + limit);
        }

        // 如果不是则访问DB
        logger.debug("load post list from DB.");
        return discussPostMapper.selectDiscussPosts(userId, offset, limit, orderMode, typeId);
    }

    // 获取 关注者/收藏 的帖子数据
    public List<DiscussPost> findPartDiscussPosts(List<Integer> list, int offset, int limit, int mode) {
        return discussPostMapper.selectPartDiscussPosts(list, offset, limit, mode);
    }

    // 获取 关注者/收藏 的帖子数量
    public int findPartDiscussPostRows(List<Integer> list, int mode) {
        return discussPostMapper.selectPartDiscussPostRows(list, mode);
    }


    // 根据用户id查询帖子数量
    public int findDiscussPostRows(int userId) {

        // 访问缓存
        if (userId == 0) {
            return postRowsCache.get(userId);
        }

        // 访问数据库
        logger.debug("load post rows from DB.");
        return discussPostMapper.selectDiscussPostRows(userId);
    }

    // 添加帖子
    public int addDiscussPost(DiscussPost post) {
        if (post == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }
        // 转义HTML标记（若内容有html标签，防止浏览器识别）
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        // 过滤敏感词
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));

        return discussPostMapper.insertDiscussPost(post);
    }

    // 根据id查询帖子内容
    public DiscussPost findDiscussPostById(int id) {
        return discussPostMapper.selectDiscussPostById(id);
    }

    // 修改评论的数量
    public int updateCommentCount(int id, int commentCount) {
        return discussPostMapper.updateCommentCount(id, commentCount);
    }

    public int updateType(int id, int type) {
        return discussPostMapper.updateType(id, type);
    }

    public int updateStatus(int id, int status) {
        return discussPostMapper.updateStatus(id, status);
    }

    public int updateScore(int id, double score) {
        return discussPostMapper.updateScore(id, score);
    }

    // 查询所有文章的分类
    public List<ArticleType> findArticleType() {
        return discussPostMapper.selectArticleType();
    }

    // 根据文章id查询对应的类型
    public ArticleType findArticleTyepByPostId(int postId) {
        return discussPostMapper.selectArticleTypeByPostId(postId);
    }

    // 根据类型id查询对应的类型
    public ArticleType findArticleTyepById(int typeId) {
        return discussPostMapper.selectArticleTypeById(typeId);
    }

    // 类型对应的文章数量+1
    public int addTypeRefCount(int typeId) {
        return discussPostMapper.addTypeRefCount(typeId);
    }

    // 类型对应的文章数量-1
    public int reduceTypeRefCount(int typeId) {
        return discussPostMapper.reduceTypeRefCount(typeId);
    }

    // 查询所有标签的数据
    public List<Tag> findAllArticleTags() {
        return discussPostMapper.selectArticleTags(0);
    }

    // mode:1 查询前30热门标签  0 则所有
    public List<Tag> findArticleTags(int mode) {
        return discussPostMapper.selectArticleTags(mode);
    }

    // 查询文章的标签ids
    public List<Tag> findArticleTagIdsById(int postId) {
        return discussPostMapper.selectArticleTagsById(postId);
    }

    //查询标签的分组名
    public List<Tag> findTagGroupNames() {
        return discussPostMapper.selectTagGroupNames();
    }

    // 查询分组名对应的标签
    public List<Tag> findTagsByGroupName(String groupName) {
        return discussPostMapper.selectTagsByGroupName(groupName);
    }

    // 添加文章的标签
    public int addPostTags(int postId, List<Integer> tagsId) {
        return discussPostMapper.insertPostTags(postId, tagsId);
    }

    // 删除文章标签
    public int deletePostTags(int postId) {
        return discussPostMapper.deletePostTags(postId);
    }

    // 增加标签引用
    public int addTagsRefCount(List<Integer> tagsId) {
        return discussPostMapper.addTagsRefCount(tagsId);
    }

    // 减少标签引用
    public int reduceTagsRefCount(List<Integer> tagsId) {
        return discussPostMapper.reduceTagsRefCount(tagsId);
    }

    // 根据标签id查询对应的标签
    public Tag findTagById(int tagId) {
        return discussPostMapper.selectTagById(tagId);
    }

    // 根据标签id查询对应的文章数量
    public int findArticleRowByTagId(int tagId) {
        return discussPostMapper.selectPostsByTagIdRows(tagId);
    }

    // 根据标签id查询文章
    public List<DiscussPost> findArticleByTagId(int tagId, int offset, int limit) {
        return discussPostMapper.selectPostsByTagId(tagId, offset, limit);
    }

}
