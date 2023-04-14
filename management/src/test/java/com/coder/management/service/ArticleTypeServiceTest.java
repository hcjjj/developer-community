package com.coder.management.service;

import com.coder.management.ManagementApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ManagementApplication.class)
public class ArticleTypeServiceTest {

    @Autowired
    private IArticleTypeService articleTypeService;

    @Test
    public void test() {
        System.out.println(articleTypeService.getById(1));

//        articleTypeService.getPage(2, 5);

    }
}
