package com.example.serverimpl.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

public class SessionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest req,
                             HttpServletResponse res,
                             Object handler) throws Exception {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            return true;
        }
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }
}
