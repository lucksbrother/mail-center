package com.haier.mailcenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haier.mailcenter.dao.ClientInfoMapper;
import com.haier.mailcenter.model.ClientInfo;
import com.haier.mailcenter.service.ClientInfoService;
import org.springframework.stereotype.Service;

@Service
public class ClientInfoServiceImpl extends ServiceImpl<ClientInfoMapper, ClientInfo> implements ClientInfoService {
}
