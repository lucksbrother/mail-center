package com.haier.mailcenter;

import com.haier.mailcenter.dto.SendMailDto;
import com.haier.mailcenter.service.MailSendLogService;
import com.haier.mailcenter.service.SendMailService;
import com.haier.mailcenter.model.MailSendTask;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class MailCenterApplicationTests {

    @Autowired
    private SendMailService sendMailService;
    @Autowired
    private MailSendLogService mailSendLogService;

    @SneakyThrows
    @Test
    public void sendMail() {
        List<String> targetList = new ArrayList<>();
        targetList.add("161789415@qq.com");
        targetList.add("161789415@haier.com");
        targetList.add("456@haier.com");
        Map<String, String> variableMap = new HashMap<>();
        variableMap.put("name", "wang");
        variableMap.put("course", "asd");
        variableMap.put("time", "2020-01-01");
        variableMap.put("teacher", "bin");

        SendMailDto sendMailDto = new SendMailDto();
        sendMailDto.setMailAddresses(targetList);
        sendMailDto.setTemplateId(1);
        sendMailDto.setClientSecret("123");
        sendMailDto.setSubject("测试");
        sendMailService.sendMailNow(sendMailDto);

        System.out.println(Arrays.toString(mailSendLogService.list().toArray()));
    }

    @Autowired
    private RedisTemplate<String, MailSendTask> redisTemplate;

    @Test
    public void addTaskQueue() {

    }

}
