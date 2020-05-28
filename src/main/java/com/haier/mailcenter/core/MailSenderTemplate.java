package com.haier.mailcenter.core;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.mailcenter.dto.SendMailDto;
import com.haier.mailcenter.exception.CheckMailAddressException;
import com.haier.mailcenter.model.BlackList;
import com.haier.mailcenter.model.ClientInfo;
import com.haier.mailcenter.model.MailSendLog;
import com.haier.mailcenter.model.MailTemplate;
import com.haier.mailcenter.service.*;
import com.haier.mailcenter.model.MailSendTask;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.*;

@Slf4j
public abstract class MailSenderTemplate {

    @Autowired
    protected JavaMailSender javaMailSender;
    @Autowired
    protected RedisTemplate<String, MailSendTask> redisTemplate;
    @Autowired
    protected Configuration configuration;
    @Autowired
    protected ClientInfoService clientInfoService;
    @Autowired
    protected BlackListService blackListService;
    @Autowired
    protected MailTemplateService mailTemplateService;
    @Autowired
    protected MailSendLogService mailSendLogService;
    @Autowired
    protected MailTaskQueueService mailTaskQueueService;
    @Autowired
    protected AuthService authService;

    private final List<String> formatErrorList = new ArrayList<>();
    private final List<String> blackAddressList = new ArrayList<>();

    /**
     * 发送邮件核心模版方法
     * sendMailDto 发送邮件数据传输类
     *
     * @return 发送结果
     * @throws Exception 抛出异常
     */
    public final Map<String, Object> sendMail(SendMailDto sendMailDto) throws Exception {
        //1、检查邮件地址-不为空-不超量-不非法格式-排除黑名单
        String[] targetList = checkMailAddress(sendMailDto.getMailAddresses(),sendMailDto.getClientSecret());
        //2、组装模版+变量为邮件内容
        String content = assembleMessageContent(sendMailDto.getTemplateId(), sendMailDto.getVariableMap());
        //3、指定发件人邮箱
        String from = assembleMessageHeader(sendMailDto.getClientSecret());
        //4、将邮件组成任务送入队列
        String taskId = UUID.randomUUID().toString().replaceAll("-", "");
        MailSendTask mailSendTask = new MailSendTask(taskId, sendMailDto.getSubject(), targetList, from, sendMailDto.getSubject(), content, new Date(), from, "", null);
        mailTaskQueueService.addMailTaskQueue(mailSendTask);
        //返回发送邮件信息
        return assembleResultMessage(0, "成功", "");
    }

    /**
     * 检查邮件地址-不为空-不超量-不非法格式-排除黑名单
     *
     * @param mailAddresses 邮件地址
     * @throws CheckMailAddressException 自定义异常
     */
    private String[] checkMailAddress(List<String> mailAddresses,String clientSecret) throws CheckMailAddressException {
        if (CollectionUtils.isEmpty(mailAddresses)) {
            throw new CheckMailAddressException("目标地址不能为空");
        }
        if (mailAddresses.size() > 200) {
            throw new CheckMailAddressException("超出最大收件人数量");
        }
        formatErrorList.clear();
        blackAddressList.clear();
        ClientInfo clientInfo = authService.getClientInfo(clientSecret);
        QueryWrapper<BlackList> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("client_id", clientInfo.getId()).or().eq("client_id", 0);
        List<BlackList> blackLists = blackListService.list(queryWrapper);
        for (String mailAddress : mailAddresses) {
            if (!mailAddress.matches("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?")) {
                formatErrorList.add(mailAddress);
                log.info("格式不正确：{}", mailAddress);
                continue;
            }
            for (BlackList blackList : blackLists) {
                if (mailAddress.equalsIgnoreCase(blackList.getMailAddress())) {
                    blackAddressList.add(mailAddress);
                    log.info("监测到黑名单地址：{}", mailAddress);
                }
            }
        }
        mailAddresses.removeAll(formatErrorList);
        mailAddresses.removeAll(blackAddressList);
        log.info("邮件地址检查完毕,待发送邮件地址为:{}", mailAddresses.toString());
        return mailAddresses.toArray(new String[0]);
    }

    private String assembleMessageContent(Integer templateId,
                                          Map<String, String> variableMap) throws Exception {
        MailTemplate mailTemplate = mailTemplateService.getById(templateId);
        if (mailTemplate == null) {
            throw new Exception("选择的模版不存在");
        }
        Template template = new Template(mailTemplate.getTemplateName(), mailTemplate.getTemplateContent(), configuration);
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, variableMap);
        log.info("组装消息内容:{}", content);
        return content;
    }

    private String assembleMessageHeader(String clientSecret) throws Exception {
        QueryWrapper<ClientInfo> clientInfoQueryWrapper = new QueryWrapper<>();
        clientInfoQueryWrapper.eq("client_secret", clientSecret);
        ClientInfo clientInfo = clientInfoService.getOne(clientInfoQueryWrapper);
        if (clientInfo == null) {
            throw new Exception("客户端密钥不正确");
        }
        if (StringUtils.isEmpty(clientInfo.getFromAddress())) {
            clientInfo.setFromAddress("morendizhi@haier.com");
        }
        log.info("组装消息头,客户端名称{}", clientInfo.getClientName());
        return clientInfo.getFromAddress();
    }

    public Map<String, Object> send(MailSendTask mailSendTask) {
        Map<String, Object> result = assembleResultMessage(200, "邮件发送成功", "");
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            //邮件地址
            helper.setTo(mailSendTask.getTargetList());
            //发送人地址
            helper.setFrom(mailSendTask.getFrom());
            //邮件标题
            helper.setSubject(mailSendTask.getSubject());
            //邮件内容
            helper.setText(mailSendTask.getContent(), true);
            javaMailSender.send(message);
        } catch (MailSendException me) {
            log.error("邮件发送异常:{}", me.getMessageExceptions()[0].getMessage());
            result = assembleResultMessage(500, "邮件发送失败", me.getMessageExceptions()[0].getMessage());
        } catch (MessagingException mge) {
            log.error("邮件发送异常:{}", mge.getLocalizedMessage());
            result = assembleResultMessage(500, "邮件发送失败", mge.getLocalizedMessage());
        } finally {
            recordSendMailLog(mailSendTask, result);
        }
        log.info("发送邮件完毕");
        return result;
    }

    private void recordSendMailLog(MailSendTask mailSendTask, Map<String, Object> result) {
        MailSendLog mailSendLog = new MailSendLog();
        mailSendLog.setTaskId(mailSendTask.getTaskId());
        mailSendLog.setTaskBy(mailSendTask.getCreateBy());
        mailSendLog.setSendAddress(Arrays.toString(mailSendTask.getTargetList()));
        mailSendLog.setSendFrom(mailSendTask.getFrom());
        mailSendLog.setSendContent(mailSendTask.getContent());
        mailSendLog.setSendTime(new Date());
        if (result.get("error").equals(200)) {
            mailSendLog.setSendStatus(1);
        } else {
            mailSendLog.setSendStatus(2);
            mailSendLog.setSendDesc(result.get("ex").toString());
        }
        mailSendLogService.save(mailSendLog);
        log.info("记录发送结果");
    }

    private Map<String, Object> assembleResultMessage(int error, String msg, String ex) {
        Map<String, Object> result = new HashMap<>();
        result.put("error", error);
        result.put("message", msg);
        result.put("ex", ex);
        return result;
    }

}
