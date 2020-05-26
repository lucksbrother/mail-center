package com.haier.mailcenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haier.mailcenter.dao.MailTemplateMapper;
import com.haier.mailcenter.model.MailTemplate;
import com.haier.mailcenter.service.MailTemplateService;
import org.springframework.stereotype.Service;

@Service
public class MailTemplateServiceImpl extends ServiceImpl<MailTemplateMapper, MailTemplate> implements MailTemplateService {
}
