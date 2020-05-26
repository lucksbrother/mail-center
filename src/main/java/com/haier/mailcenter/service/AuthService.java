package com.haier.mailcenter.service;

/**
 * 简单的权限认证
 */
public interface AuthService {

    boolean hasAuthority(String clientSecret);
}
