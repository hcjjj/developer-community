package com.coder.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Deprecated
// 这是spring所提供的线程池的配置类
@Configuration
@EnableScheduling
@EnableAsync
// 让 @Async @Scheduled 注解生效
public class ThreadPoolConfig {
}
