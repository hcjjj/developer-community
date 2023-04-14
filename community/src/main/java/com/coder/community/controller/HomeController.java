package com.coder.community.controller;

import com.coder.community.entity.*;
import com.coder.community.service.CollectService;
import com.coder.community.service.DiscussPostService;
import com.coder.community.service.LikeService;
import com.coder.community.service.UserService;
import com.coder.community.util.CommunityConstant;
import com.coder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class HomeController implements CommunityConstant {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private CollectService collectService;

    @Autowired
    private RedisTemplate redisTemplate;

    // SpringMVC支持8中提交方式 RequestMethod
    // @RequestBody @RequestParam @PathVariable
    // 区别
    // ■@RequestParam用于接收url地址传参或表单传参
    // ■@RequestBody用于接收json数据
    // ■@PathVariable用于接收路径参数,使用{参数名称)描述路径参数
    // 应用
    // ■后期开发中,发送请求参数超过1个时,以json格式为主,@RequestBody.应用较广
    // ■如果发送非json格式数据,选用@RequestParam接收请求参数
    // ■采用RESTful进行开发,当参数数量较少时,例如1个,可以采用@PathVariable接收请求路径变量,通常用于传递id值

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String root() {
        return "forward:/index";
    }

    // 主页 最新 最热
    @RequestMapping(path = "/index", method = RequestMethod.GET)
    // 访问首页 RequestParam 没传值 默认是0
    public String getIndexPage(Model model, Page page,
                               @RequestParam(name = "orderMode", defaultValue = "0") int orderMode) {
        // 方法调用前,SpringMVC会自动实例化Model和Page,并将Page注入Model.
        // 所以,在thymeleaf中可以直接访问Page对象中的数据.
        page.setRows(discussPostService.findDiscussPostRows(0));
        // orderMode 通过get请求地址的？传过来的
        page.setPath("/index?orderMode=" + orderMode);

        // orderMode 最新0 最热1
        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit(), orderMode, 0);
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        // 遍历list中的 userId，将对应的 user 信息 组装到 discussPosts
        if (list != null) {
            for (DiscussPost post : list) {
                Map<String, Object> map = new HashMap<>();
                // 置入文章的数据
                map.put("post", post);
                // 置入对应用户信息数据
                User user = userService.findUserById(post.getUserId());
                map.put("user", user);
                // 置入帖子赞的数量
                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
                map.put("likeCount", likeCount);

                // 置入帖子收藏的数量
                long collectCount = collectService.findArticleCollectCount(post.getId());
                map.put("collectCount", collectCount);

                // 置入文章的类型
                ArticleType articleType = discussPostService.findArticleTyepByPostId(post.getTypeId());
                map.put("type", articleType.getName());

                // 置入文章的标签
                List<Tag> articleTags = discussPostService.findArticleTagIdsById(post.getId());
                map.put("tags", articleTags);

                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
        model.addAttribute("orderMode", orderMode);

        // 文章类别数据
        List<ArticleType> articleTypes = discussPostService.findArticleType();
        model.addAttribute("articleTypes", articleTypes);

        // 文章标签数据
        List<Tag> articleTags = discussPostService.findArticleTags(1);
        model.addAttribute("articleTags", articleTags);

        // /index 标识从web应用根目录开始的绝对路径 是不加 / 是相对路径
        return "/index";
    }


    // 用户收藏的帖子
    @RequestMapping(path = "/index/collectPosts/{userId}", method = RequestMethod.GET)
    //    @PathVariable("userId") 表明这个参数是从路径中获取 路径参数（路径变量）
    public String getCollectPosts(Model model, Page page,
                                  @PathVariable("userId") int userId) {

        String userCollectKey = RedisKeyUtil.getUserCollectKey(userId);
        Set<Integer> targetIds = redisTemplate.opsForSet().members(userCollectKey);
        List<Integer> postIds = new ArrayList<>(targetIds);


        if (postIds.size() != 0) {
            // 获取关注者的帖子总数
            page.setRows(discussPostService.findPartDiscussPostRows(postIds, 1));
            System.out.println(page.getRows());

            if (page.getRows() != 0) {

                List<DiscussPost> list = discussPostService.findPartDiscussPosts(postIds, page.getOffset(), page.getLimit(), 1);

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
                        // 置入帖子收藏的数量
                        long collectCount = collectService.findArticleCollectCount(post.getId());
                        map.put("collectCount", collectCount);

                        discussPosts.add(map);

                        // 置入文章的类型
                        ArticleType articleType = discussPostService.findArticleTyepByPostId(post.getTypeId());
                        map.put("type", articleType.getName());

                        // 置入文章的标签
                        List<Tag> articleTags = discussPostService.findArticleTagIdsById(post.getId());
                        map.put("tags", articleTags);
                    }
                }
                model.addAttribute("discussPosts", discussPosts);
            }
        }

        // 文章类别数据
        List<ArticleType> articleTypes = discussPostService.findArticleType();
        model.addAttribute("articleTypes", articleTypes);

        model.addAttribute("orderMode", 4);

        // 文章标签数据
        List<Tag> articleTags = discussPostService.findArticleTags(1);
        model.addAttribute("articleTags", articleTags);

        return "index";
    }


    // 关注者的帖子
    @RequestMapping(path = "/index/followeesPosts/{userId}", method = RequestMethod.GET)
    //    @PathVariable("userId") 表明这个参数是从路径中获取 路径参数（路径变量）
    public String getfolloweesPosts(Model model, Page page,
                                    @PathVariable("userId") int userId) {

        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, ENTITY_TYPE_USER);
        // 从redis中获取该用户关注的所有人的id
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followeeKey, 0, -1);
        List<Integer> userIdList = new ArrayList<>(targetIds);

        if (userIdList.size() != 0) {
            // 获取关注者的帖子总数
            page.setRows(discussPostService.findPartDiscussPostRows(userIdList, 0));

            // orderMode 最新0 最热1
            if (page.getRows() != 0) {

                List<DiscussPost> list = discussPostService.findPartDiscussPosts(userIdList, page.getOffset(), page.getLimit(), 0);
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
                        // 置入帖子收藏的数量
                        long collectCount = collectService.findArticleCollectCount(post.getId());
                        map.put("collectCount", collectCount);

                        // 置入文章的类型
                        ArticleType articleType = discussPostService.findArticleTyepByPostId(post.getTypeId());
                        map.put("type", articleType.getName());

                        // 置入文章的标签
                        List<Tag> articleTags = discussPostService.findArticleTagIdsById(post.getId());
                        map.put("tags", articleTags);

                        discussPosts.add(map);
                    }
                }
                model.addAttribute("discussPosts", discussPosts);

            }

        }
        // 文章类别数据
        List<ArticleType> articleTypes = discussPostService.findArticleType();
        model.addAttribute("articleTypes", articleTypes);

        model.addAttribute("orderMode", 3);

        // 文章标签数据
        List<Tag> articleTags = discussPostService.findArticleTags(1);
        model.addAttribute("articleTags", articleTags);
        return "index";
    }

    // 获取错误页面
    @RequestMapping(path = "/error", method = RequestMethod.GET)
    public String getErrorPage() {
        return "/error/500";
    }


    // 无权限返回404页面
    @RequestMapping(path = "/denied", method = RequestMethod.GET)
    public String getDeniedPage() {
        return "/error/404";
    }

}
