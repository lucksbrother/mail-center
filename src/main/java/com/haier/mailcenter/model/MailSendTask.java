package com.haier.mailcenter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 邮件发送任务
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailSendTask implements Serializable {

    /**
     * 任务id
     * UUID 去-，32位
     */
    private String taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 收件人地址
     */
    private String[] targetList;

    /**
     * 发件人地址
     */
    private String from;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 邮件内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 固定周期定时任务表达式
     */
    private String corn;

    /**
     * 延迟周期定时任务
     */
    private Date delay;
}
