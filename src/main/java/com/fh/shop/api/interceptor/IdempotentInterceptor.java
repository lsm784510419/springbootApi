package com.fh.shop.api.interceptor;

import com.fh.shop.api.common.Idempotent;
import com.fh.shop.api.common.ResponseEnum;
import com.fh.shop.api.exception.GlobalException;
import com.fh.shop.api.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class IdempotentInterceptor extends HandlerInterceptorAdapter {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断是否有自定义注解
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (!method.isAnnotationPresent(Idempotent.class)){
            return true;
        }
        //获取头信息
        String header = request.getHeader("x_token");
        if (StringUtils.isEmpty(header)){
            throw new GlobalException(ResponseEnum.HEADER_IS_NULL);
        }
        //判断头信息是否存在
        boolean exists = RedisUtil.exists(header);
        if (!exists){
            throw new GlobalException(ResponseEnum.TOKEN_HEAND_IS_MISS);
        }
        Long token = RedisUtil.del(header);
        if (token <= 0){
            throw new GlobalException(ResponseEnum.TOKEN_TIME_IS_LONG);
        }
        return true;
    }
}
