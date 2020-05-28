package com.haier.mailcenter.config;

import com.haier.mailcenter.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AuthCheckInterceptor implements HandlerInterceptor {

    @Autowired
    private AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String secret = request.getHeader("clientSecret");
        log.info("检查客户端：{}权限", secret);
        if (!StringUtils.isEmpty(secret) && authService.hasAuthority(secret)) {
            return true;
        }
        returnJson(response);
        return false;
    }


    private void returnJson(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try (PrintWriter writer = response.getWriter()) {
            Map<String, Object> result = new HashMap<>();
            result.put("error", 401);
            result.put("message", "权限不足");
            writer.print(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
