package com.haier.mailcenter.service.impl;

import com.haier.mailcenter.common.RedisKey;
import com.haier.mailcenter.model.MailSendTask;
import com.haier.mailcenter.service.MailTaskQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;


import static com.haier.mailcenter.common.RedisKey.*;

@Slf4j
@Service
public class MailTaskQueueServiceImpl implements MailTaskQueueService {

    private final RedisTemplate<String, MailSendTask> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    public MailTaskQueueServiceImpl(RedisTemplate<String, MailSendTask> redisTemplate, StringRedisTemplate stringRedisTemplate) {
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
    }


    @Override
    public MailSendTask addMailTaskQueue(MailSendTask mailSendTask) {
        Long num = redisTemplate.opsForList().leftPush("mailSendTaskQueue", mailSendTask);
        log.info("邮件任务放入队列成功，当前队列：{}", num);
        return mailSendTask;
    }

    @Override
    public MailSendTask addMailTaskQueueWithCorn(MailSendTask mailSendTask) {
        return null;
    }

    @Override
    public MailSendTask addMailTaskQueueWithDelay(MailSendTask mailSendTask) {
        return null;
    }

    @Override
    public Boolean delMailTaskQueueWithCorn(String taskId) {
        return null;
    }

    @Override
    public Boolean delMailTaskQueueWithDelay(String taskId) {
        return null;
    }

    @Override
    public MailSendTask updateMailTaskWithCorn(MailSendTask mailSendTask) {
        return null;
    }

    @Override
    public MailSendTask updateMailTaskWithDelay(MailSendTask mailSendTask) {
        return null;
    }

    @Override
    public List<MailSendTask> getMailTaskQueue() {
        Long llen = redisTemplate.opsForList().size(QUEUE_KEY);
        return redisTemplate.opsForList().range(QUEUE_KEY, 0, llen != null ? llen : 1000);
    }

    @Override
    public List<MailSendTask> getMailTaskQueueBak() {
        Long llen = redisTemplate.opsForList().size(QUEUE_KEY_BAK);
        return redisTemplate.opsForList().range(QUEUE_KEY_BAK, 0, llen != null ? llen : 1000);
    }

    @Override
    public MailSendTask getMailTask(String taskId) {
        Long llen = redisTemplate.opsForList().size(QUEUE_KEY);
        List<MailSendTask> mailSendTaskList = redisTemplate.opsForList().range(QUEUE_KEY, 0, llen != null ? llen : 1000);
        if (mailSendTaskList != null) {
            for (MailSendTask mailSendTask : mailSendTaskList) {
                if (mailSendTask.getTaskId().equalsIgnoreCase(taskId)) {
                    return mailSendTask;
                }
            }
        }
        return null;
    }

    @Override
    public MailSendTask rightPopAndLeftPush() {
        return redisTemplate.opsForList().rightPopAndLeftPush(QUEUE_KEY, QUEUE_KEY_BAK, QUEUE_WAIT_TIME_SECONDS, TimeUnit.SECONDS);
    }

    @Override
    public void removeBakQueueTask(MailSendTask tempTask) {
        redisTemplate.opsForList().remove(QUEUE_KEY_BAK, 0, tempTask);
    }


}
