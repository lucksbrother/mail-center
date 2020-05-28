package com.haier.mailcenter.job;

import com.haier.mailcenter.dto.SendMailDto;
import com.haier.mailcenter.service.SendMailService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Slf4j
//@Component
public class MailJob extends QuartzJobBean {

    @Autowired
    private SendMailService sendMailService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        SendMailDto sendMailDto = (SendMailDto) jobDataMap.get("sendMailDto");
        if (sendMailDto == null) {
            log.error("Job接收sendMailDto参数为空");
            throw new JobExecutionException("Job接收sendMailDto参数为空");
        }
        try {
            sendMailService.sendMailNow(sendMailDto);
        } catch (Exception ce) {
            ce.printStackTrace();
            log.error("JOB调用异常：{}", ce.getMessage());
            throw new JobExecutionException("JOB调用异常", ce);
        }
    }

    private SendMailDto mapToDto(JobDataMap jobDataMap) throws JobExecutionException {
        if (CollectionUtils.isEmpty(jobDataMap)) {
            log.warn("Job参数不能为空");
            throw new JobExecutionException("Job参数不能为空");
        }
        SendMailDto sendMailDto = new SendMailDto();
        if (jobDataMap.get("mailAddress") != null && jobDataMap.get("mailAddress") instanceof List) {
            List<String> mailAddress = (List<String>) jobDataMap.get("mailAddress");
            sendMailDto.setMailAddresses(mailAddress);
        } else {
            log.warn("Job参数{mailAddress}无效");
            throw new JobExecutionException("Job参数{mailAddress}无效");
        }
        if (jobDataMap.getIntegerFromString("templateId") != null && jobDataMap.get("templateId") instanceof Integer) {
            Integer templateId = jobDataMap.getIntegerFromString("templateId");
            sendMailDto.setTemplateId(templateId);
        } else {
            log.warn("Job参数{templateId}无效");
            throw new JobExecutionException("Job参数{templateId}无效");
        }
        if (jobDataMap.get("variableMap") != null && jobDataMap.get("variableMap") instanceof Map) {
            Map<String, String> variableMap = (Map<String, String>) jobDataMap.get("variableMap");
            sendMailDto.setVariableMap(variableMap);
        } else {
            log.warn("Job参数{variableMap}无效");
            throw new JobExecutionException("Job参数{variableMap}无效");
        }
        if (!StringUtils.isEmpty(jobDataMap.getString("subject"))) {
            String subject = jobDataMap.getString("subject");
            sendMailDto.setSubject(subject);
        } else {
            log.warn("Job参数{subject}无效");
            throw new JobExecutionException("Job参数{subject}无效");
        }
        return sendMailDto;
    }
}
