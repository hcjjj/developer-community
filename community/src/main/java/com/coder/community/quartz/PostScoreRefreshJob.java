package com.coder.community.quartz;

import com.coder.community.entity.DiscussPost;
import com.coder.community.service.CollectService;
import com.coder.community.service.DiscussPostService;
import com.coder.community.service.ElasticsearchService;
import com.coder.community.service.LikeService;
import com.coder.community.util.CommunityConstant;
import com.coder.community.util.RedisKeyUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PostScoreRefreshJob implements Job, CommunityConstant {

    // 记录日志
    private static final Logger logger = LoggerFactory.getLogger(PostScoreRefreshJob.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private CollectService collectService;

    @Autowired
    private ElasticsearchService elasticsearchService;

    // 纪元（日期）
    private static final Date epoch;

    static {
        try {
            epoch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-01-01 00:00:00");
        } catch (ParseException e) {
            throw new RuntimeException("初始化纪元失败!", e);
        }
    }

    // 定时任务
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 从redis中取值
        String redisKey = RedisKeyUtil.getPostScoreKey();
        BoundSetOperations operations = redisTemplate.boundSetOps(redisKey);

        if (operations.size() == 0) {
            logger.info("[任务取消] 没有需要刷新的文章!");
            return;
        }

        logger.info("[任务开始] 正在刷新文章分数: " + operations.size());
        while (operations.size() > 0) {
            // Object -> Integer
            this.refresh((Integer) operations.pop());
        }
        logger.info("[任务结束] 文章分数刷新完毕!");
    }

    private void refresh(int postId) {

        // postId -> post
        DiscussPost post = discussPostService.findDiscussPostById(postId);

        if (post == null) {
            // 如果被管理员删掉了
            logger.error("该文章不存在: id = " + postId);
            return;
        }

        // 是否精华
        boolean wonderful = post.getStatus() == 1;
        // 评论数量
        int commentCount = post.getCommentCount();
        // 点赞数量
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, postId);
        // 文章的收藏数量
        long collectCount = collectService.findArticleCollectCount(postId);


        // 计算公式： log( 精华分 + 评论数 * 5 + 点赞数 * 2 + 收藏数量 * 2) + (发布时间 - 纪元)
        // 计算权重
        double w = (wonderful ? 75 : 0) + commentCount * 5 + likeCount * 2 + collectCount * 2;
        // 分数 = 文章权重 + 距离天数
        // 求对数10为底，避免负数
        double score = Math.log10(Math.max(w, 1))
                + (post.getCreateTime().getTime() - epoch.getTime()) / (1000 * 3600 * 24);
        // 更新文章分数
        discussPostService.updateScore(postId, score);

        // 更新新的得分，同步搜索数据
        post.setScore(score);
        elasticsearchService.saveDiscussPost(post);
    }

}
