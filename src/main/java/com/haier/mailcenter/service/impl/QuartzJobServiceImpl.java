package com.haier.mailcenter.service.impl;

import com.haier.mailcenter.common.JobType;
import com.haier.mailcenter.dto.SendMailDto;
import com.haier.mailcenter.job.MailJob;
import com.haier.mailcenter.service.QuartzJobService;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuartzJobServiceImpl implements QuartzJobService {

    /**
     * 注入任务调度器
     */
    private final Scheduler scheduler;

    public QuartzJobServiceImpl(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void addJobWithCron(SendMailDto sendMailDto) throws SchedulerException {
        //任务名称
        String name = UUID.randomUUID().toString().replaceAll("-", "");
        //任务所属分组
        String group = MailJob.class.getName() + "_corn";
        //创建任务
        JobDetail jobDetail = JobBuilder.newJob(MailJob.class)
                .withIdentity(name, group)
                .withDescription("周期发送类定时任务，指定发送周期，任务循环执行").build();
        //设置任务传递邮件内容
        jobDetail.getJobDataMap().put("sendMailDto", sendMailDto);
        //创建任务触发器
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(sendMailDto.getCron());
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group).withSchedule(scheduleBuilder).build();
        //将触发器与任务绑定到调度器内
        scheduler.scheduleJob(jobDetail, trigger);
    }

    @Override
    public void addJobWithDelay(SendMailDto sendMailDto) throws SchedulerException {
        //任务名称
        String name = UUID.randomUUID().toString().replaceAll("-", "");
        //任务所属分组
        String group = MailJob.class.getName() + "_delay";
        //创建任务
        JobDetail jobDetail = JobBuilder.newJob(MailJob.class)
                .withIdentity(name, group)
                .withDescription("延迟发送类定时任务，指定发送时间，任务只执行一次").build();
        //设置任务传递邮件内容
        jobDetail.getJobDataMap().put("sendMailDto", sendMailDto);
        //创建任务触发器
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group).startAt(sendMailDto.getDelay()).build();
        //将触发器与任务绑定到调度器内
        scheduler.scheduleJob(jobDetail, trigger);
    }

    @Override
    public boolean removeJob(String jobName, String jobGroup) throws SchedulerException {
        return scheduler.deleteJob(JobKey.jobKey(jobName, jobGroup));
    }

    @Override
    public void updateJob(String triggerName, String triggerGroup, JobType jobType, String cron, Date startAtTime) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroup);
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        switch (jobType) {
            case CRON:
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
                trigger = trigger.getTriggerBuilder().withSchedule(scheduleBuilder).build();
                break;
            case DELAY:
                trigger = trigger.getTriggerBuilder().startAt(startAtTime).build();
                break;
            default:
        }
        scheduler.rescheduleJob(triggerKey, trigger);
    }

    @Override
    public List<Map<String, Object>> getJobs() throws SchedulerException {
        List<Map<String, Object>> jobDetails = new ArrayList<>();
        Set<JobKey> jobKeySet = scheduler.getJobKeys(GroupMatcher.anyGroup());
        for (JobKey jobKey : jobKeySet) {
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            Map<String, Object> map = new HashMap<>();
            map.put("JobKey", jobDetail.getKey());
            map.put("JobDesc", jobDetail.getDescription());
            map.put("JobParams", jobDetail.getJobDataMap());
            map.put("JobStatus", scheduler.getTriggerState(TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup())));
            map.put("JobNextFireTime", scheduler.getTrigger(TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup())).getNextFireTime());
            map.put("JobExpression", ((CronTrigger) scheduler.getTrigger(TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup()))).getCronExpression());
            map.put("JobStartTime", scheduler.getTrigger(TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup())).getStartTime());
            jobDetails.add(map);
        }
        return jobDetails;
    }

    @Override
    public JobDetail getJob(String jobName, String jobGroup) throws SchedulerException {
        return scheduler.getJobDetail(JobKey.jobKey(jobName, jobGroup));
    }

    @Override
    public void pauseJob(String jobName, String jobGroup) throws SchedulerException {
        scheduler.pauseJob(JobKey.jobKey(jobName, jobGroup));
    }

    @Override
    public void pauseAllJob() throws SchedulerException {
        scheduler.pauseAll();
    }

    @Override
    public void pauseJobs(String endsWith) throws SchedulerException {
        scheduler.pauseJobs(GroupMatcher.groupEndsWith(endsWith));
    }

    @Override
    public void resumeJob(String jobName, String jobGroup) throws SchedulerException {
        scheduler.resumeJob(JobKey.jobKey(jobName, jobGroup));
    }

    @Override
    public void resumeAllJob() throws SchedulerException {
        scheduler.resumeAll();
    }

    @Override
    public void resumeJobs(String endsWith) throws SchedulerException {
        scheduler.resumeJobs(GroupMatcher.groupEndsWith(endsWith));
    }

}
