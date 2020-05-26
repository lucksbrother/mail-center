package com.haier.mailcenter.service.impl;

import com.haier.mailcenter.core.MailSenderTemplate;
import com.haier.mailcenter.dto.SendMailDto;
import com.haier.mailcenter.service.SendMailService;
import org.springframework.stereotype.Service;


@Service
public class SendMailServiceImpl extends MailSenderTemplate implements SendMailService {

    @Override
    public void sendMailNow(SendMailDto sendMailDto) throws Exception {
        sendMail(sendMailDto);
    }

    @Override
    public void sendMailCron() {

    }

}
