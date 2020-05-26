package com.haier.mailcenter.service;

import com.haier.mailcenter.model.MailSendTask;

import java.util.List;

/**
 * 邮件任务队列服务
 */
public interface MailTaskQueueService {

    /**
     * 增加邮件任务(即时队列型）
     *
     * @param mailSendTask 任务实体
     * @return 任务实体
     */
    MailSendTask addMailTaskQueue(MailSendTask mailSendTask);

    /**
     * 增加邮件任务附带Corn表达式（固定周期型）
     *
     * @param mailSendTask 任务实体
     * @return 任务实体
     */
    MailSendTask addMailTaskQueueWithCorn(MailSendTask mailSendTask);

    /**
     * 增加邮件任务附带delay表达式（延迟发送型）
     *
     * @param mailSendTask 任务实体
     * @return 任务实体
     */
    MailSendTask addMailTaskQueueWithDelay(MailSendTask mailSendTask);

    /**
     * 删除邮件任务（固定周期型）
     *
     * @param taskId 任务Id
     * @return 操作是否成功
     */
    Boolean delMailTaskQueueWithCorn(String taskId);

    /**
     * 删除邮件任务（延迟发送型）
     *
     * @param taskId 任务Id
     * @return 操作是否成功
     */
    Boolean delMailTaskQueueWithDelay(String taskId);

    /**
     * 改邮件任务（固定周期型）
     *
     * @param mailSendTask 任务实体
     * @return 任务实体
     */
    MailSendTask updateMailTaskWithCorn(MailSendTask mailSendTask);

    /**
     * 改邮件任务（延迟发送型）
     *
     * @param mailSendTask 任务实体
     * @return 任务实体
     */
    MailSendTask updateMailTaskWithDelay(MailSendTask mailSendTask);

    /**
     * 查询所有任务队列
     *
     * @return 任务实体
     */
    List<MailSendTask> getMailTaskQueue();

    /**
     * 查询所有任务队列(备份队列：异常导致失败的）
     *
     * @return 任务实体
     */
    List<MailSendTask> getMailTaskQueueBak();

    /**
     * 查询一个任务详情
     *
     * @param taskId 任务Id
     * @return 任务实体
     */
    MailSendTask getMailTask(String taskId);

    /**
     * 从队列中右取任务，并且插入备份队列
     *
     * @return 任务实体
     */
    MailSendTask rightPopAndLeftPush();

    /**
     * 移除备份队列任务
     *
     * @param tempTask 任务实体
     */
    void removeBakQueueTask(MailSendTask tempTask);



}
