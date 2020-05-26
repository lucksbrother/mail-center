package com.haier.mailcenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haier.mailcenter.dao.BlackListMapper;
import com.haier.mailcenter.model.BlackList;
import com.haier.mailcenter.service.BlackListService;
import org.springframework.stereotype.Service;

@Service
public class BlackListServiceImpl extends ServiceImpl<BlackListMapper, BlackList> implements BlackListService {
}
