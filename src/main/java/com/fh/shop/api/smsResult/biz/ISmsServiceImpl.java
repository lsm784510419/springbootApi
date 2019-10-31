package com.fh.shop.api.smsResult.biz;

import com.alibaba.fastjson.JSONObject;
import com.fh.shop.api.common.ResponseEnum;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.common.SystemConst;
import com.fh.shop.api.smsResult.po.SmsResult;
import com.fh.shop.api.util.KeyUtil;
import com.fh.shop.api.util.RedisUtil;
import com.fh.shop.api.util.SMSUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("smsService")
@Transactional(rollbackFor = Exception.class)
public class ISmsServiceImpl implements ISmsService {

    @Override
    @Transactional(readOnly = true)
    public ServerResponse findPhone(String phone) {
        //判断是否输入手机号
        if (StringUtils.isEmpty(phone)){
            return ServerResponse.error(ResponseEnum.PHONE_IS_NULL);
        }
        //获取返回的验证码
        String sendSMSJson = SMSUtil.sendSMS(phone);
        //类型转换
        SmsResult smsResult = JSONObject.parseObject(sendSMSJson, SmsResult.class);
        int code = smsResult.getCode();//获取状态码
        //返回的状态码不是200   就是发送失败
        if(code != 200){
            return ServerResponse.error(ResponseEnum.PHONE_IS_ERROR);
        }
        //上边都不成功  则证明可以获取验证码
        String smsCode = smsResult.getObj();
        //放入redis缓存
        RedisUtil.setEx(KeyUtil.buildSmsKey(phone),SystemConst.CODE_EXPIRE,smsCode);
        return ServerResponse.success();
    }
}
