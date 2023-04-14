package com.coder.community.service;

import com.coder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CollectService {

    @Autowired
    private RedisTemplate redisTemplate;

    // 收藏
    public void collect(int userId, int postId) {
        // 对应集合里面是postId
        String userCollectKey = RedisKeyUtil.getUserCollectKey(userId);
        // 对应集合里面是userId
        String articleCollectKey = RedisKeyUtil.getArticleCollectKey(postId);
        // 判断该用户在不在集合里面来确定该用户收藏没收藏过这篇文章
        boolean isMember = redisTemplate.opsForSet().isMember(articleCollectKey, userId);
        // 如果已经收藏过了
        if (isMember) {
            // 取消收藏
            redisTemplate.opsForSet().remove(articleCollectKey, userId);
            redisTemplate.opsForSet().remove(userCollectKey, postId);
        } else {
            // 收藏
            redisTemplate.opsForSet().add(articleCollectKey, userId);
            redisTemplate.opsForSet().add(userCollectKey, postId);
        }
    }

    // 查询文章被收藏数量
    public long findArticleCollectCount(int postId) {
        String articleCollectKey = RedisKeyUtil.getArticleCollectKey(postId);
        return redisTemplate.opsForSet().size(articleCollectKey);
    }

    // 查询用户收藏数量
    public long findUserCollectCount(int userId, int postId) {
        String userCollectKey = RedisKeyUtil.getUserCollectKey(userId);
        return redisTemplate.opsForSet().size(userCollectKey);
    }

    // 查询某用户对文章的收藏状态
    public int findArticleCollectStatus(int userId, int postId) {
        String articleCollectKey = RedisKeyUtil.getArticleCollectKey(postId);
        return redisTemplate.opsForSet().isMember(articleCollectKey, userId) ? 1 : 0;
    }


}









