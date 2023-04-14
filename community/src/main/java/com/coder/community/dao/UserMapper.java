package com.coder.community.dao;

import com.coder.community.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    // 查询
    User selectById(int id);

    User selectByName(String username);

    User selectByEmail(String email);

    // 增加
    int insertUser(User user);

    // 修改
    int updateStatus(int id, int status);

    int updateHeader(int id, String headerUrl);

    int updatePassword(int id, String password);

}
