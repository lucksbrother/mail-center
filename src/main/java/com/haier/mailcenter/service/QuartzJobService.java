package com.haier.mailcenter.service;

import com.haier.mailcenter.common.JobType;
import com.haier.mailcenter.dto.SendMailDto;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface QuartzJobService {

    void addJobWithCron(SendMailDto sendMailDto) throws SchedulerException;

    void addJobWithDelay(SendMailDto sendMailDto) throws SchedulerException;

    boolean removeJob(String jobName, String jobGroup) throws SchedulerException;

    void updateJob(String triggerName, String triggerGroup, JobType jobType, String cron, Date startAtTime) throws SchedulerException;

    List<Map<String, Object>> getJobs() throws SchedulerException;

    JobDetail getJob(String jobName, String jobGroup) throws SchedulerException;

    void pauseJob(String jobName, String jobGroup) throws SchedulerException;

    void pauseAllJob() throws SchedulerException;

    void pauseJobs(String endsWith) throws SchedulerException;

    void resumeJob(String jobName, String jobGroup) throws SchedulerException;

    void resumeAllJob() throws SchedulerException;

    void resumeJobs(String endsWith) throws SchedulerException;
}
