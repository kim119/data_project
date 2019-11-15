package com.dlb.userlogin.interceptor;

import com.dlb.userlogin.service.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginHandlerInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenManager tokenManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //拦截逻辑
        String token = request.getHeader("token");
        boolean b = tokenManager.checkToken(token);
        if (!b) {
            System.out.println("没有权限请先登陆");
            return false;
        } else {
            //已登陆，放行请求
            return true;
        }
    }


}
