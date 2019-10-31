package com.fh.shop.api.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.fh.shop.api.common.Check;
import com.fh.shop.api.common.ResponseEnum;
import com.fh.shop.api.common.SystemConst;
import com.fh.shop.api.exception.GlobalException;
import com.fh.shop.api.member.vo.MemberVo;
import com.fh.shop.api.util.KeyUtil;
import com.fh.shop.api.util.Md5Util;
import com.fh.shop.api.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Base64;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //处理拦截器的跨域问题
         response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,"GET,POST,DELETE,PUT,OPTIONS");
         response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,"*");
         response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,"x_auth,content-type,x_token");

        //因为我们自定义了头信息所以在发送请求之前会先发送一个options请求，所以需要解决这个问题
        String method1 = request.getMethod();//请求类型
        if (method1.equalsIgnoreCase("options")){
            return false;
        }

        //判断该方法上是否有注解
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (!method.isAnnotationPresent(Check.class)){
            return true;
        }
        //获取header
        String header = request.getHeader("x_auth");
        //判断header是否存在   如果不存在则抛出头信息丢失
        if (StringUtils.isEmpty(header)){
            throw new GlobalException(ResponseEnum.HEADER_IS_NULL);
        }
        //拆分  数组长度规定为2  如果不是2则证明头信息不完整
        String[] split = header.split("\\.");
        if (split.length != 2){
            throw new GlobalException(ResponseEnum.HEADER_IS_NOTQUAN);
        }
        //验签  根据.分割获取到第一次的base64编码的数据和第二次经过md5加密后的base64代码
        String base64Json = split[0];
        String base64JsonMd5 = split[1];
        //经过md5加密获取新的秘钥
        String newSing = Md5Util.sing(base64Json, SystemConst.SECRET);
        //转换为新的base64位
        String base64JsonNewSing = Base64.getEncoder().encodeToString(newSing.getBytes());
        //判断两次的是否一样 不一样就证明被头信息被篡改
        if (!base64JsonMd5.equals(base64JsonNewSing)){
            throw new GlobalException(ResponseEnum.HEADER_IS_UPDATE);
        }
        //获取信息/解码
        String memberJson= new String (Base64.getDecoder().decode(base64Json),"UTF-8");
        MemberVo memberVo = JSONObject.parseObject(memberJson, MemberVo.class);
        String userName = memberVo.getUserName();
        String uuid = memberVo.getUuid();
        //是否超时  根据redis中的方法来判断key是否存在  不存在就证明超时
        boolean exists = RedisUtil.exists(KeyUtil.buildMenberKey(userName, uuid));
        if (!exists){
            throw new GlobalException(ResponseEnum.LOGIN_IS_TIME);
        }
        request.setAttribute(SystemConst.MEMBER_IS_VO_KEY,memberVo);
        RedisUtil.expire(KeyUtil.buildMenberKey(userName,uuid),SystemConst.MENBER_EXPIRE);
        return true;
    }
}
