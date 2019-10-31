package com.fh.shop.api.util;

public class KeyUtil {

    public static String buildSmsKey(String phone){
        return "SMS"+phone;
    }

    public static String buildMenberKey(String userName,String uuid){

        return "member:"+userName+":"+uuid;
    }
    public static String buildCartFiled(Long memberId){

        return "member:"+memberId;
    }
    public static String buildPagLogKey(Long memberId){

        return "pagLog:"+memberId;
    }

}
