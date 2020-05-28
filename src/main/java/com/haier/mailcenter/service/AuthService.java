package com.haier.mailcenter.service;

import com.haier.mailcenter.model.ClientInfo;

/**
 * 简单的权限认证
 */
public interface AuthService {

    boolean hasAuthority(String clientSecret);

    ClientInfo getClientInfo(String clientSecret);
}
