package com.coder.community;

import com.coder.community.dao.DiscussPostMapper;
import com.coder.community.entity.ArticleType;
import com.coder.community.entity.DiscussPost;
import com.coder.community.entity.Tag;
import com.coder.community.entity.User;
import com.coder.community.service.DiscussPostService;
import com.coder.community.service.LikeService;
import com.coder.community.service.UserService;
import com.coder.community.util.RedisKeyUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class DiscussPostTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private DiscussPostMapper discussPostMapper;


    @Test
    public void testTag() {
        System.out.println(discussPostMapper.selectArticleTags(0));
    }

    @Test
    public void discuuPostServiceTest() {

        Tag tag = discussPostMapper.selectTagById(188);
        System.out.println(tag.toString());

//        int rows = discussPostService.findArticleRowByTagId(188);
//        List<DiscussPost> discussPosts = discussPostService.findArticleByTagId(188,0,10);
//        System.out.println(rows);
//        for (int i = 0; i < discussPosts.size(); i++) {
//            System.out.println(discussPosts.get(i));
//        }

//        List<Integer> tagsId = new LinkedList<>();
//        tagsId.add(188);
//        tagsId.add(189);
//        tagsId.add(190);
//        discussPostService.addTagsRefCount(tagsId);
//        discussPostService.reduceTagsRefCount(tagsId);

//        discussPostService.deletePostTags(250);


//        List<Integer> tagsId = new LinkedList<>();
//        tagsId.add(1);
//        tagsId.add(2);
//        tagsId.add(3);
//        tagsId.add(4);
//        tagsId.add(5);
//        discussPostService.addPostTags(319, tagsId);


//        List<Tag> articleTagIds = discussPostMapper.selectArticleTagsById(303);
//        List<Tag> articleTags = discussPostMapper.selectTagGroupNames();
//        List<Tag> articleTags = discussPostMapper.selectTagsByGroupName("开发语言");
//        for (int i = 0; i < articleTags.size(); i++) {
//            System.out.println(articleTags.get(i));
//        }


//        List<ArticleType> articleTypes = discussPostService.findArticleType();
//        for (int i = 0; i < articleTypes.size(); i++) {
//            System.out.println(articleTypes.get(i).getName() + " " + articleTypes.get(i).getRefCount());
//        }

//        ArticleType articleType = discussPostService.findArticleTyepById(4);
//        System.out.println(articleType.getName());

//        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(0, 0, 10, 0, 3);
//        for (DiscussPost post : list) {
//            System.out.println(post);
//        }

//        int rows = discussPostMapper.selectDiscussPostRows(0);
//        System.out.println(rows);


    }


    @Test
    public void Test() {
        // 获取该用户的关注者id
        int ENTITY_TYPE_USER = 3;
        int ENTITY_TYPE_POST = 1;
        String followeeKey = RedisKeyUtil.getFolloweeKey(153, ENTITY_TYPE_USER);
        // Zset 有序集合，倒序范围查询 reverseRange  0 到 -1表示所有
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followeeKey, 0, -1);
        List<Integer> userIdList = new ArrayList<>(targetIds);
        System.out.println(userIdList);

        // 通过关注者id 分页获取的帖子信息 post
        List<DiscussPost> list = discussPostService.findPartDiscussPosts(userIdList, 0, 10, 0);
        System.out.println(list);

        // post.getUserId() 获取关注者的信息
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        // 遍历list中的 userId，将对应的 user 信息 组装到 discussPosts
        if (list != null) {
            for (DiscussPost post : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);
                User user = userService.findUserById(post.getUserId());
                map.put("user", user);
                // 置入帖子赞的数量
                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
                map.put("likeCount", likeCount);
                discussPosts.add(map);
            }
        }
        System.out.println(discussPosts);
    }
}
