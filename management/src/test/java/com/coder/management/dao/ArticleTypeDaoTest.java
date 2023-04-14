package com.coder.management.dao;

import com.coder.management.ManagementApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ManagementApplication.class)
public class ArticleTypeDaoTest {

    @Autowired
    private ArticleTypeDao articleTypeDao;

    @Test
    public void baseMapperTest() {
        articleTypeDao.selectList(null);

//        IPage page = new Page(2, 5);
//        articleTypeDao.selectPage(page, null);
//        System.out.println(page.getCurrent());
//        System.out.println(page.getSize());
//        System.out.println(page.getRecords());

//        QueryWrapper<ArticleType> queryWrapper = new QueryWrapper<>();
//        queryWrapper.like("description", "类别");
//        articleTypeDao.selectList(queryWrapper);

//        LambdaQueryWrapper<ArticleType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.like(ArticleType::getDescription, "类别");
//        articleTypeDao.selectList(lambdaQueryWrapper);

//        String description = "类别";
//        LambdaQueryWrapper<ArticleType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.like(Strings.isNotEmpty(description), ArticleType::getDescription, description);
//        articleTypeDao.selectList(lambdaQueryWrapper);


    }
}
