package com.coder.community;

import com.coder.community.entity.DiscussPost;
import com.coder.community.service.DiscussPostService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

// 设置运行器
@RunWith(SpringRunner.class)
// @SpringBootTest 定义测试类
@SpringBootTest
// 如果测试类在引导类(或叫配置类, @SpringBootApplication 里面包含了 @SpringBootConfiguration)
// 同一个包下 就不需要加这个注解指定引导类类名
@ContextConfiguration(classes = CommunityApplication.class)
// 两个可以合并为：
// @SpringBootTest(classes = CommunityApplication.class) 也可以
public class CaffeineTests {

    // 注入要测试的对象
    @Autowired
    private DiscussPostService postService;

    // @Test 测试方法
    @Test
    public void initDataForTest() {
        // 压力测试，填入10w条数据
        for (int i = 0; i < 100; i++) {
            DiscussPost post = new DiscussPost();
            post.setUserId(111);
            post.setTitle("压力测试");
            post.setContent("你应该知道的缓存进化史 和 如何优雅的设计和使用缓存？ 。这两篇文章主要从一些实战上面去介绍如何去使用缓存。在这两篇文章中我都比较推荐Caffeine这款本地缓存去代替你的Guava Cache。");
            post.setCreateTime(new Date());
            post.setScore(Math.random() * 2000);
            postService.addDiscussPost(post);
        }
    }

    @Test
    public void testCache() {
        // 测试缓存
        // 第一次走db 后两次缓存 第四次是没有缓存的
        System.out.println(postService.findDiscussPosts(0, 0, 10, 1, 0));
        System.out.println(postService.findDiscussPosts(0, 0, 10, 1, 0));
        System.out.println(postService.findDiscussPosts(0, 0, 10, 1, 0 ));
        System.out.println(postService.findDiscussPosts(0, 0, 10, 0, 0));
    }

}
