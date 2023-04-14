package com.coder.community.util;

public class RedisKeyUtil {

    private static final String SPLIT = ":";
    // 收藏
    private static final String PREFIX_ARTICLE_COLLECT = "collect:post";
    private static final String PREFIX_ARTICLE_USER = "collect:user";
    // 赞
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    private static final String PREFIX_USER_LIKE = "like:user";
    // 关注
    private static final String PREFIX_FOLLOWEE = "followee";
    private static final String PREFIX_FOLLOWER = "follower";
    // 验证码
    private static final String PREFIX_KAPTCHA = "kaptcha";
    // 登入凭证
    private static final String PREFIX_TICKET = "ticket";
    // 用户信息
    private static final String PREFIX_USER = "user";
    // 独立访客
    private static final String PREFIX_UV = "uv";
    // 日活跃用户
    private static final String PREFIX_DAU = "dau";

    private static final String PREFIX_POST = "post";


    // 某个文章的收藏 (对应的数据是userId，统计userId的数量就是这篇文章的收藏量)
    public static String getArticleCollectKey(int postId) {
        return PREFIX_ARTICLE_COLLECT + SPLIT + postId;
    }
    // 某个用户的收藏 (对应的数据是postId，统计postId的数量就是用户的收藏量)
    public static String getUserCollectKey(int userId) {
        return PREFIX_ARTICLE_USER + SPLIT + userId;
    }

    // 某个实体的赞
    // like:entity:entityType:entityId -> set(userId)  (统计点赞者的id数量即为点赞数)
    // entityType: 评论2 帖子1
    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    // 某个用户的赞
    // like:user:userId -> int
    public static String getUserLikeKey(int userId) {
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    // 某个用户关注的实体，zset有序集合，当前时间作为排序依据
    // followee:userId:entityType -> zset(entityId,now)
    public static String getFolloweeKey(int userId, int entityType) {
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    // 某个实体拥有的粉丝（用户或者是帖子，抽象出来方便扩张）
    // follower:entityType:entityId -> zset(userId,now)
    public static String getFollowerKey(int entityType, int entityId) {
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

    // 登录验证码 这时候用户还没有登入，所有没有userId
    // owner 用户的临时凭证 通过Cookie发送给用户，标识一下用户
    public static String getKaptchaKey(String owner) {
        return PREFIX_KAPTCHA + SPLIT + owner;
    }

    // 登录的凭证
    public static String getTicketKey(String ticket) {
        return PREFIX_TICKET + SPLIT + ticket;
    }

    // 用户
    public static String getUserKey(int userId) {
        return PREFIX_USER + SPLIT + userId;
    }

    // 单日UV
    public static String getUVKey(String date) {
        return PREFIX_UV + SPLIT + date;
    }

    // 区间UV
    public static String getUVKey(String startDate, String endDate) {
        return PREFIX_UV + SPLIT + startDate + SPLIT + endDate;
    }

    // 单日活跃用户
    public static String getDAUKey(String date) {
        return PREFIX_DAU + SPLIT + date;
    }

    // 区间活跃用户
    public static String getDAUKey(String startDate, String endDate) {
        return PREFIX_DAU + SPLIT + startDate + SPLIT + endDate;
    }

    // 帖子分数
    public static String getPostScoreKey() {
        return PREFIX_POST + SPLIT + "score";
    }

}
