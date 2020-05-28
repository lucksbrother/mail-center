package com.haier.mailcenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.mailcenter.dao.ClientInfoMapper;
import com.haier.mailcenter.model.ClientInfo;
import com.haier.mailcenter.service.AuthService;
import com.haier.mailcenter.service.ClientInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private ClientInfoService clientInfoService;

    @Override
    public boolean hasAuthority(String clientSecret) {
        QueryWrapper<ClientInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("client_secret", clientSecret);
        ClientInfo clientInfo = clientInfoService.getOne(queryWrapper);
        return clientInfo != null;
    }

    @Override
    public ClientInfo getClientInfo(String clientSecret) {
        QueryWrapper<ClientInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("client_secret", clientSecret);
        return clientInfoService.getOne(queryWrapper);
    }
}
