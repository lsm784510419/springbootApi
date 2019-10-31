package com.fh.shop.api.common;

public enum ResponseEnum {
    IDS_IS_NULL(5000,"批量删除的ids为空"),
    BRAND_IS_NULL(5001,"查询的品牌为空"),
    CODE_IS_NULL(5002,"验证码为空"),
    CODE_IS_ERROR(5003,"未发送验证码或验证码过时"),
    PHONE_IS_NULL(5004,"请输入手机号"),
    PHONE_IS_ERROR(5005,"发送失败"),
    USERNAME_IS_NULL(5006,"会员名为空"),
    REALNAME_IS_NULL(5007,"真实姓名为空"),
    PASSWORD_IS_NULL(5008,"密码为空"),
    USERNAME_IS_EXIST(5009,"会员名已存在"),
    PHONE_IS_EXIST(5010,"手机号已存在"),
    CODE_IS_EXCEPTION(5011,"您输入的验证码不正确"),
    EMAIL_IS_NULL(5012,"您输入的邮箱为空"),
    EMAIL_IS_EXIST(5013,"邮箱地址已存在"),
    MEMBER_PASSWORD_IS_NULL(5014,"您输入的密码不正确"),
    MEMBER_IS_NULL(5015,"此会员不存在"),
    PHONELOGIN_IS_NULL(5015,"此手机号未注册，请先注册"),
    HEADER_IS_NULL(5100,"头信息丢失"),
    HEADER_IS_NOTQUAN(5101,"头信息不完整"),
    HEADER_IS_UPDATE(5102,"头信息被篡改"),
    LOGIN_IS_TIME(5103,"登陆超时"),
    PRODUCTINFO_IS_NO(5200,"商品信息不存在"),
    PRODUCTSTATUS_IS_DOWN(5201,"商品已下架"),
    CART_IS_NULL(5202,"您的购物车为空"),
    CARTITEM_IS_NULL(5203,"您的购物车中没有该商品"),


    ADDRESSEENAME_IS_ERROR(5300,"您输入的收件人名字不符合规则"),
    ADDRESSEEPHONE_IS_ERROR(5301,"您输入的收件人电话不符合规则"),
    AREADETAILS_IS_ERROR(5302,"您输入的详细地址不符合规则"),

    TOKEN_HEAND_IS_MISS(5400,"token头信息异常"),
    TOKEN_TIME_IS_LONG(5401,"请求超时"),



    STOCK_IS_ERROR(6000,"下订单失败，库存量不足"),

    PAYLOG_IS_NULL(6000,"下订单失败，库存量不足"),
    ;

    private int code;

    private String msg;


   private ResponseEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
