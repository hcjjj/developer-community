package com.coder.community.entity;

import com.coder.community.enums.ArticleTypeScopeEn;
import com.coder.community.enums.AuditStateEn;
import lombok.Data;

import java.util.Date;

@Data
public class ArticleType {

    private Long id;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;

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
//    private Long refCount;
    private int refCount;

    /**
     * 作用域
     */
    private ArticleTypeScopeEn scope;

    /**
     * 创建人
     */
    private Long creatorId;

    /**
     * 审核状态
     */
    private AuditStateEn auditState;
}
