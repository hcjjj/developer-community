package com.coder.management.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

@Data
public class DiscussPost {
    private Long id;
    private int userId;

    private String title;

    // 不查询文章内容
    @TableField(exist = false)
    private String content;

    @TableField(exist = false)
    private int sortByScore;

    @TableField(exist = false)
    private int sortByTime;

    private int type;
    private int status;
    private Date createTime;
    private int commentCount;
    private double score;

}
