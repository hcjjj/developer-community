package com.coder.management.entity;

import com.coder.management.enums.AuditStateEn;
import lombok.Data;

import java.util.Date;

@Data
public class Tag {

    private Long id;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;
    private String groupName;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 引用统计
     */
    private Long refCount;

    /**
     * 创建人
     */
    private Long creatorId;

    /**
     * 审核状态
     */
    private AuditStateEn auditState;
    private int isDelete;
}
