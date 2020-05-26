package com.haier.mailcenter.task;

import com.haier.mailcenter.core.MailSenderTemplate;
import com.haier.mailcenter.model.MailSendTask;
import com.haier.mailcenter.service.MailTaskQueueService;
import com.haier.mailcenter.service.MailTaskQueueSwitchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * 任务执行监听器
 */
@Slf4j
@Component
public class TaskExecutionMonitor extends MailSenderTemplate implements ApplicationRunner {

    private final AtomicInteger sleepTimeCount = new AtomicInteger();

    private final MailTaskQueueService mailTaskQueueService;
    private final MailTaskQueueSwitchService mailTaskQueueSwitchService;

    public TaskExecutionMonitor(MailTaskQueueService mailTaskQueueService, MailTaskQueueSwitchService mailTaskQueueSwitchService) {
        this.mailTaskQueueService = mailTaskQueueService;
        this.mailTaskQueueSwitchService = mailTaskQueueSwitchService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        while (true) {
            if (mailTaskQueueSwitchService.isQueueSwitchOpen()) {
                sleepTimeCount.set(0);
                MailSendTask tempTask = mailTaskQueueService.rightPopAndLeftPush();
                if (tempTask != null) {
                    log.info("接收到邮件任务,ID:{},Name:{}", tempTask.getTaskId(), tempTask.getTaskName());
                    if (tempTask.getCreateBy().equalsIgnoreCase("123")) {
                        //如果队列客户端开关关闭，即：createBy 包含于 客户端名称列表 ，则将任务放回队列尾端
                        mailTaskQueueService.addMailTaskQueue(tempTask);
                    }
                    log.info("开始执行发送邮件");
                    send(tempTask.getTargetList(), tempTask.getFrom(), tempTask.getSubject(), tempTask.getContent());
                    mailTaskQueueService.removeBakQueueTask(tempTask);
                    log.info("结束执行发送邮件");
                }
                Thread.sleep(500);
            } else {
                //TODO 增加逻辑，如果开关为关闭状态，将当前队列中的MailTask，根据模版规则条件，
                // 非抛弃型任务持久化到Mysql中，抛弃型任务记录Log日志后扔掉

                for (int i = 6; i > 0; i--) {
                    log.info("当前总开关为关闭状态，休眠线" + i + "0秒");
                    Thread.sleep(10000);
                }
                sleepTimeCount.incrementAndGet();
                log.info("已累计休眠{}分钟", sleepTimeCount);
            }
        }
    }
}
