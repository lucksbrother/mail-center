package com.haier.mailcenter.model;

import com.haier.mailcenter.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class MailTemplate extends BaseEntity {

    /**
     * 客户端id
     */
    private Long clientId;
    /**
     * 模版名称
     */
    private String templateName;
    /**
     * 模版内容
     */
    private String templateContent;
    /**
     * 模版变量
     * 多个变量
     */
    private String templateVariable;
}
