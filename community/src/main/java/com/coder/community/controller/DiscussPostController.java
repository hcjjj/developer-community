package com.coder.community.controller;

import com.coder.community.entity.*;
import com.coder.community.event.EventProducer;
import com.coder.community.service.*;
import com.coder.community.util.CommunityConstant;
import com.coder.community.util.CommunityUtil;
import com.coder.community.util.HostHolder;
import com.coder.community.util.RedisKeyUtil;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {

    //  @Autowired 自动装配
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private CollectService collectService;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private RedisTemplate redisTemplate;

    // 读取配置文件里定义的数据
    @Value("${qiniu.key.access}")
    private String accessKey;

    @Value("${qiniu.key.secret}")
    private String secretKey;

    @Value("${qiniu.bucket.header.name}")
    private String bucketName;

    @Value("${quniu.bucket.header.url}")
    private String bucketUrl;

    // 网站域名
    @Value("${community.path.domain}")
    private String domain;

    // 项目名(访问路径)
    @Value("${server.servlet.context-path}")
    private String contextPath;

    // editorMd 图片上传地址
    @Value("${community.path.editormdUploadPath}")
    private String editormdUploadPath;

    // 增加帖子
    @RequestMapping(path = "/add", method = RequestMethod.POST)
//    简化写法：@PostMapping("/add")
    @ResponseBody
    public String addDiscussPost(String title, String content, int typeId, @RequestParam("tags[]") List<Integer> tags) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJSONString(403, "你还没有登录哦!");
        }

        // 文章对象数据填充
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setTypeId(typeId);
        post.setCreateTime(new Date());
        // 添加文章
        discussPostService.addDiscussPost(post);
        // 增加对应类型的引用
        discussPostService.addTypeRefCount(typeId);
        // 添加标签相关数据
        discussPostService.addPostTags(post.getId(), tags);
        // 增加对应标签的引用 (不一定会有标签）
        if (tags != null) {
            discussPostService.addTagsRefCount(tags);
        }

        // 触发发帖事件(用于搜索功能)
        Event event = new Event()
                .setTopic(TOPIC_PUBLISH)
                .setUserId(user.getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(post.getId());
        eventProducer.fireEvent(event);

        // 计算帖子分数
        String redisKey = RedisKeyUtil.getPostScoreKey();
        // 用 set 无序 去重，因为只需要记录需要计算的帖子，不需要关注顺序和数量
        redisTemplate.opsForSet().add(redisKey, post.getId());

        // 报错的情况,将来统一处理.
        return CommunityUtil.getJSONString(0, "发布成功!");
    }

    // 分类页面
    @RequestMapping(path = "/{typeId}", method = RequestMethod.GET)
    public String getDiscussPostByTypeId(Model model, Page page,
                                         @PathVariable("typeId") int typeId) {
        ArticleType articleType = discussPostService.findArticleTyepById(typeId);
        // 要分页的总数
        page.setRows(articleType.getRefCount());

        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit(), 0, typeId);
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
                // 置入文章的类型
//                ArticleType articleType = discussPostService.findArticleTyepById(post.getTypeId());
                map.put("type", articleType.getName());

                // 置入帖子收藏的数量
                long collectCount = collectService.findArticleCollectCount(post.getId());
                map.put("collectCount", collectCount);

                // 置入文章的标签
                List<Tag> articleTags = discussPostService.findArticleTagIdsById(post.getId());
                map.put("tags", articleTags);

                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
//        model.addAttribute("orderMode", orderMode);

        // 文章类别数据
        List<ArticleType> articleTypes = discussPostService.findArticleType();
        model.addAttribute("articleTypes", articleTypes);
        model.addAttribute("typeName", articleType.getName());

        // /index 标识从web应用根目录开始的绝对路径 是不加 / 是相对路径
        return "/site/discuss-type";
    }

    // 根据标签查询文章
    @RequestMapping(path = "/tagId/{tagId}", method = RequestMethod.GET)
    public String getDiscussPostByTagId(Model model, Page page,
                                        @PathVariable("tagId") int tagId) {
        int rows = discussPostService.findArticleRowByTagId(tagId);
        // 要分页的总数
        page.setRows(rows);

        List<DiscussPost> list = discussPostService.findArticleByTagId(tagId, page.getOffset(), page.getLimit());

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
                // 置入文章的类型
                ArticleType articleType = discussPostService.findArticleTyepById(post.getTypeId());
                map.put("type", articleType.getName());

                // 置入帖子收藏的数量
                long collectCount = collectService.findArticleCollectCount(post.getId());
                map.put("collectCount", collectCount);

                // 置入文章的标签
                List<Tag> articleTags = discussPostService.findArticleTagIdsById(post.getId());
                map.put("tags", articleTags);

                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);

        // 当前标签的数据
        Tag tag = discussPostService.findTagById(tagId);
        model.addAttribute("tag", tag);
        // 相关标签的数据
        List<Tag> articleTags = discussPostService.findTagsByGroupName(tag.getGroupName());
        model.addAttribute("relatedTags", articleTags);

        return "/site/discuss-tag";
    }

    // 根据帖子id 显示帖子页面
    @RequestMapping(path = "/detail/{discussPostId}", method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page) {
        // 帖子
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post", post);
        // 作者
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);

        // 点赞数量
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeCount", likeCount);
        // 点赞状态
        // 判断用户是否有登入
        int likeStatus = hostHolder.getUser() == null ? 0 :
                likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeStatus", likeStatus);

        // 被收藏数量
        long collectCount = collectService.findArticleCollectCount(post.getId());
        model.addAttribute("collectCount", collectCount);
        // 收藏状态
        int collectStatus = hostHolder.getUser() == null ? 0 :
                collectService.findArticleCollectStatus(hostHolder.getUser().getId(), discussPostId);
        model.addAttribute("collectStatus", collectStatus);

        // 置入文章的类型
        ArticleType articleType = discussPostService.findArticleTyepByPostId(post.getTypeId());
        model.addAttribute("type", articleType.getName());

        // 置入文章的标签
        List<Tag> articleTags = discussPostService.findArticleTagIdsById(post.getId());
        model.addAttribute("tags", articleTags);


        // 评论分页信息
        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);
        page.setRows(post.getCommentCount()); //帖子评论总数

        // 评论: 给帖子的评论
        // 回复: 给评论的评论
        // 评论列表
        List<Comment> commentList = commentService.findCommentsByEntity(
                ENTITY_TYPE_POST, post.getId(), page.getOffset(), page.getLimit());
        // 评论VO列表
        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if (commentList != null) {
            for (Comment comment : commentList) {
                // 评论VO
                Map<String, Object> commentVo = new HashMap<>();
                // 评论
                commentVo.put("comment", comment);
                // 作者  id -> user
                commentVo.put("user", userService.findUserById(comment.getUserId()));
                // 点赞数量
                likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("likeCount", likeCount);
                // 点赞状态
                likeStatus = hostHolder.getUser() == null ? 0 :
                        likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("likeStatus", likeStatus);

                // 回复列表
                List<Comment> replyList = commentService.findCommentsByEntity(
                        ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                // 回复VO列表
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if (replyList != null) {
                    for (Comment reply : replyList) {
                        Map<String, Object> replyVo = new HashMap<>();
                        // 回复
                        replyVo.put("reply", reply);
                        // 作者
                        replyVo.put("user", userService.findUserById(reply.getUserId()));
                        // 回复目标
                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        replyVo.put("target", target);
                        // 点赞数量
                        likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, reply.getId());
                        replyVo.put("likeCount", likeCount);
                        // 点赞状态
                        likeStatus = hostHolder.getUser() == null ? 0 :
                                likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, reply.getId());
                        replyVo.put("likeStatus", likeStatus);

                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys", replyVoList);

                // 回复数量
                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount", replyCount);

                commentVoList.add(commentVo);
            }
        }

        // 将数据传给模版
        model.addAttribute("comments", commentVoList);

        return "/site/discuss-detail";
    }

    // 置顶 异步请求
    @RequestMapping(path = "/top", method = RequestMethod.POST)
    @ResponseBody
//    public String setTop(int id) {
//        discussPostService.updateType(id, 1);
    // 修改置顶状态
    public String updateTop(int id, int type) {
        discussPostService.updateType(id, type);

        // 触发发帖事件
        Event event = new Event()
                .setTopic(TOPIC_PUBLISH)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(id);
        eventProducer.fireEvent(event);

        return CommunityUtil.getJSONString(0);
    }

    // 加精
    @RequestMapping(path = "/wonderful", method = RequestMethod.POST)
    @ResponseBody
//    public String setWonderful(int id) {
//        discussPostService.updateStatus(id, 1);
    public String updateWonderful(int id, int status) {
        discussPostService.updateStatus(id, status);

        // 触发发帖事件
        Event event = new Event()
                .setTopic(TOPIC_PUBLISH)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(id);
        eventProducer.fireEvent(event);

        // 记录需要计算帖子分数
        String redisKey = RedisKeyUtil.getPostScoreKey();
        redisTemplate.opsForSet().add(redisKey, id);

        return CommunityUtil.getJSONString(0);
    }

    // 删除
    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String setDelete(int userId, int postId, int typeId, @RequestParam("tags[]") List<Integer> tags) {
        User user = hostHolder.getUser();
        // 用户只能删除自己的文章 管理员都可以
        if (user.getId() == userId || user.getType() != 0) {
            // 删除文章
            discussPostService.updateStatus(postId, 2);
            // 删除对应分类的引用
            discussPostService.reduceTypeRefCount(typeId);
            if (tags.size() > 1) {
                // 删除标签
//                discussPostService.deletePostTags(postId);
                // 减少对应标签的引用
                discussPostService.reduceTagsRefCount(tags);
            }

            // 触发删帖事件
            Event event = new Event()
                    .setTopic(TOPIC_DELETE)
                    .setUserId(hostHolder.getUser().getId())
                    .setEntityType(ENTITY_TYPE_POST)
                    .setEntityId(postId);
            eventProducer.fireEvent(event);

            return CommunityUtil.getJSONString(0);
        } else {
            return "/error/404";
        }
    }


    // 进入markdown帖子发布页
    @GetMapping("/publish")
    public String getPublishPage(Model model) {

        // 文章类别数据
        List<ArticleType> articleTypes = discussPostService.findArticleType();
        model.addAttribute("articleTypes", articleTypes);

        // 文章标签数据
        // 分组名
        List<Tag> tagGroupNames = discussPostService.findTagGroupNames();
        // 遍历封装对应的标签 key 是分组名 value是对应的标签名
        Map<String, List<Tag>> articleTags = new HashMap<>();
        for (Tag tag : tagGroupNames) {
            List<Tag> tags = discussPostService.findTagsByGroupName(tag.getGroupName());
            articleTags.put(tag.getGroupName(), tags);
        }
        model.addAttribute("articleTags", articleTags);

        return "/site/discuss-publish";
    }

    // markdown 图片上传
    @PostMapping("/uploadMdPic")
    @ResponseBody
    public String uploadMdPic(@RequestParam(value = "editormd-image-file", required = false) MultipartFile pic) {

        String url = null; // 图片访问地址
        try {
            // 获取上传文件的名称
            String trueFileName = pic.getOriginalFilename();
            // 后缀
            String suffix = trueFileName.substring(trueFileName.lastIndexOf("."));
            String fileName = CommunityUtil.generateUUID() + suffix;

            // 图片存储路径 (本地)
            File dest = new File(editormdUploadPath + "/" + fileName);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            // 保存图片到存储路径
            // 保存到本地
            pic.transferTo(dest);

//            // 上传到七牛云
//            //构造一个带指定Zone对象的配置类
//            //指定上传文件服务器地址：
//            Configuration cfg = new Configuration(Zone.zone2());
//            //上传管理器
//            UploadManager uploadManager = new UploadManager(cfg);
//            //身份认证
//            Auth auth = Auth.create(accessKey, secretKey);
//            //指定覆盖上传
//            String upToken = auth.uploadToken(bucketName, fileName);
//            try {
//                //上传
//                Response response = uploadManager.put(pic.getBytes(), fileName, upToken);
//            } catch (QiniuException ex) {
//                System.err.println(ex.getMessage());
//                Response r = ex.response;
//                System.err.println(r.toString());
//                try {
//                    System.err.println(r.bodyString());
//                } catch (QiniuException ex2) {
//                }
//            }
            // 图片访问地址 (本地的)
            url = domain + contextPath + "/editor-md-upload/" + fileName;

            // 七牛云图片访问地址
//            url = bucketUrl + "/" + fileName;


        } catch (Exception e) {
            e.printStackTrace();
            return CommunityUtil.getEditorMdJSONString(0, "上传失败", url);
        }

        return CommunityUtil.getEditorMdJSONString(1, "上传成功", url);
    }
}
