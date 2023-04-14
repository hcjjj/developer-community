package com.coder.community;

import com.coder.community.util.SensitiveFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveTests {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testSensitiveFilter() {
        String text = "我今天遇到了一个傻逼，啥也不会又自以为是";
        text = sensitiveFilter.filter(text);
        System.out.println(text);

        text = "我今天遇到了一个傻❤逼，啥也不会又自以为是，真实傻x";
        text = sensitiveFilter.filter(text);
        System.out.println(text);
    }

}
