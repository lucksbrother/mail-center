package com.haier.mailcenter.service.impl;

import com.haier.mailcenter.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public boolean hasAuthority(String clientSecret) {
        return true;
    }
}
