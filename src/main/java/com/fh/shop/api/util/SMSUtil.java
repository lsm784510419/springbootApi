package com.fh.shop.api.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SMSUtil {
    //发送验证码的请求路径URL
    private static final String
            SERVER_URL="https://api.netease.im/sms/sendcode.action";
    //网易云信分配的账号，请替换你在管理后台应用下申请的Appkey
    private static final String
            APP_KEY="2e3b47d6b4ff7418abeddfa81b52ba73";
    //网易云信分配的密钥，请替换你在管理后台应用下申请的appSecret
    private static final String APP_SECRET="459594150489";
    //随机数
    private static final String NONCE="123456";
    //短信模板ID
    //private static final String TEMPLATEID="3057527";
    //手机号  15001359482
    //private static final String MOBILE="18537950615";
    //验证码长度，范围4～10，默认为4
    private static final String CODELEN="6";

    public static String sendSMS (String phone){
        //获取随机数
        String uuid = UUID.randomUUID().toString();
        //获取时间戳
        String time = System.currentTimeMillis()+"";
        /*
         * 参考计算CheckSum的java代码，在上述文档的参数列表中，有CheckSum的计算文档示例
         */
        String checkSum = CheckSumBuilder.getCheckSum(APP_SECRET, NONCE, time);
        Map<String,String> headers = new HashMap<>();
        headers.put("AppKey",APP_KEY);
        headers.put("Nonce",NONCE);
        headers.put("CurTime",time);
        headers.put("CheckSum",checkSum);

        Map<String,String> params = new HashMap<>();
        params.put("mobile",phone);
        params.put("codeLen",CODELEN);
        String code = HttpClientUtil.sendHttpClient(SERVER_URL, headers, params);
        return code;
    }
}
