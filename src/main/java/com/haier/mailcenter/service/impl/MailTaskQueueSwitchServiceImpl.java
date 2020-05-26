package com.haier.mailcenter.service.impl;

import com.haier.mailcenter.model.MailSendTask;
import com.haier.mailcenter.service.MailTaskQueueSwitchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import static com.haier.mailcenter.common.RedisKey.*;

@Slf4j
@Service
public class MailTaskQueueSwitchServiceImpl implements MailTaskQueueSwitchService {

    private final RedisTemplate<String, MailSendTask> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    public MailTaskQueueSwitchServiceImpl(RedisTemplate<String, MailSendTask> redisTemplate, StringRedisTemplate stringRedisTemplate) {
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
    }


    @Override
    public boolean isQueueSwitchOpen() {
        String queueSwitch = stringRedisTemplate.opsForValue().get(QUEUE_SWITCH_KEY);
        return "on".equalsIgnoreCase(queueSwitch);
    }

    @Override
    public boolean openQueueSwitch() {
        log.info("开启任务队列开关，完成线程休眠后，将激活线程");
        stringRedisTemplate.opsForValue().set(QUEUE_SWITCH_KEY, "on");
        return true;
    }

    @Override
    public boolean closeQueueSwitch() {
        log.info("关闭任务队列开关，完成当前任务后，将线程进行休眠");
        stringRedisTemplate.opsForValue().set(QUEUE_SWITCH_KEY, "off");
        return true;
    }
}
