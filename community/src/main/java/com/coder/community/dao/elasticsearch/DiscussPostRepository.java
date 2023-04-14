package com.coder.community.dao.elasticsearch;

import com.coder.community.entity.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

// ElasticsearchRepository里面已经声明好了各种方法，实体类 DiscussPost 实体类主键的类型 Integer
// @Repository 和 @Component 一样是添加到spring容器受其管理，Repository 是用在持久层上的
@Repository
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost, Integer> {

}
