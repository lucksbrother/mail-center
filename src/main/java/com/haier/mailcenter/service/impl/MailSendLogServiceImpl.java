package com.haier.mailcenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haier.mailcenter.dao.MailSendLogMapper;
import com.haier.mailcenter.model.MailSendLog;
import com.haier.mailcenter.service.MailSendLogService;
import org.springframework.stereotype.Service;

@Service
public class MailSendLogServiceImpl extends ServiceImpl<MailSendLogMapper, MailSendLog> implements MailSendLogService {
}
