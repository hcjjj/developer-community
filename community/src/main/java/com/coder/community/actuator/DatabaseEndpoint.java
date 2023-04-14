package com.coder.community.actuator;

import com.coder.community.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Deprecated
//@Component
//@Endpoint(id = "database")
public class DatabaseEndpoint {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseEndpoint.class);

    @Autowired
    private DataSource dataSource;

    // 通过get请求来访问
//    @ReadOperation
    public String checkConnection() {
        // 放括号里的资源会自动关闭
        try (Connection connection = dataSource.getConnection()
        ) {
            return CommunityUtil.getJSONString(0, "获取连接成功!");
        } catch (SQLException e) {
            logger.error("获取连接失败");
            return CommunityUtil.getJSONString(1, "获取连接失败！");
        }
    }


}
