package com.coder.community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;


//SpringBoot的 引导类 是Boot工程的执行入口，运行main方法就可以启动项目
//SpringBoot工程运行后初始化Spring容器，扫描引导类所在包加载bean
//内置的tomcat服务器：<artifactId>tomcat-embed-core</artifactId>
//内置服务器
//tomcat(默认) apache出品，粉丝多，应用面广，负载了若干较重的组件
//jetty 更轻量级，负载性能远不及tomcat
//undertow 负载性能勉强跑赢tomcat

@SpringBootApplication

@ComponentScan({"com.coder.management","com.coder.community"})

public class CommunityApplication {

    // 初始化方法
    @PostConstruct
    public void init() {
        // 解决netty启动冲突问题
        // 看 Netty4Utils.setAvailableProcessors()
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }

    //    开发的入口
    public static void main(String[] args) {
        // Spring 加载bean 的入口(默认扫描同一包下的所有类)
        SpringApplication.run(CommunityApplication.class, args);
    }

}
