package com.coder.community.dao;

import com.coder.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

@Mapper
// 声明不推荐使用了
@Deprecated
public interface LoginTicketMapper {

    // 之前是在src/main/resources/mapper/xxx.xml这种方式来写SQL
    // 这里用第二种方法，用注解的方式
    @Insert({
            "insert into login_ticket(user_id,ticket,status,expired) ",
            "values(#{userId},#{ticket},#{status},#{expired})"
    })

    // Option 声明自动生成主键并注入到id属性
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);

    @Select({
            "select id,user_id,ticket,status,expired ",
            "from login_ticket where ticket=#{ticket}"
    })
    LoginTicket selectByTicket(String ticket);

    // if动态sql，这里不需要，只是演示如何在这里写动态sql
    @Update({
            "<script>",
            "update login_ticket set status=#{status} where ticket=#{ticket} ",
//            "<if test=\"ticket!=null\"> ",
//            "and 1=1 ",
//            "</if>",
            "</script>"
    })
    int updateStatus(String ticket, int status);

}