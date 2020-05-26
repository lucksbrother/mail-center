package com.haier.mailcenter.service;

import com.haier.mailcenter.dto.SendMailDto;


public interface SendMailService {

    void sendMailNow(SendMailDto sendMailDto) throws Exception;

    void sendMailCron();

}
