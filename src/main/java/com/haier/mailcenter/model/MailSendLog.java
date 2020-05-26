package com.haier.mailcenter.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class MailSendLog {
    /**
     * 主键ID.
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 任务ID
     * UUID去-，32位
     * 对应Redis队列中的TaskId
     */
    private String taskId;

    /**
     * 任务创建人（客户端）
     */
    private String taskBy;

    /**
     * 发件人
     */
    private String sendFrom;

    /**
     * 收件人
     */
    private String sendAddress;

    /**
     * 邮件内容
     */
    private String sendContent;

    /**
     * 发送状态：1-成功，2-失败
     */
    private Integer sendStatus;

    /**
     * 描述：异常信息或其他
     */
    private String sendDesc;

    /**
     * 发送时间
     */
    private Date sendTime;

}
