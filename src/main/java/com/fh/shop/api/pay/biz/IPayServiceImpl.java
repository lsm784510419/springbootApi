package com.fh.shop.api.pay.biz;

import com.alibaba.fastjson.JSONObject;
import com.fh.shop.api.common.ResponseEnum;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.common.SystemConst;
import com.fh.shop.api.order.mapper.IOrderesMapper;
import com.fh.shop.api.order.po.Orderes;
import com.fh.shop.api.payLog.mapper.IPayLogMapper;
import com.fh.shop.api.payLog.po.PayLog;
import com.fh.shop.api.util.*;
import com.github.wxpay.sdk.WXPay;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("payService")
@Transactional(rollbackFor = Exception.class)
public class IPayServiceImpl implements IPayService {

    @Autowired
    private IPayLogMapper payLogMapper;

    @Autowired
    private IOrderesMapper orderesMapper;

    @Override
    public ServerResponse payOrder(Long memberId) {
        String payLogJson = RedisUtil.get(KeyUtil.buildPagLogKey(memberId));
        if (StringUtils.isEmpty(payLogJson)){
            return ServerResponse.error(ResponseEnum.PAYLOG_IS_NULL);
        }
        PayLog payLog = JSONObject.parseObject(payLogJson, PayLog.class);
        String outTradeNo = payLog.getOutTradeNo();
        String orderId1 = payLog.getOrderId();
        String payMoney = BigDecimalUtil.getsubTotalPrice1(payLog.getPayMoney().toString(), "100").intValue() + "";


        MyConfig config = new MyConfig();
        WXPay wxpay = new WXPay(config);

        Map<String, String> data = new HashMap<String, String>();
        data.put("body", "飞狐大卖场");
        data.put("out_trade_no", outTradeNo);
        data.put("fee_type", "CNY");
        data.put("total_fee", payMoney);
        data.put("spbill_create_ip", "123.12.12.123");
        Date date = DateUtils.addMinutes(new Date(), 2);
        String time_expire = DateUtil.data2Str(date, DateUtil.FOR_TIME);
        data.put("time_expire",time_expire);
        data.put("notify_url", "http://www.example.com/wxpay/notify");
        data.put("trade_type", "NATIVE");  // 此处指定为扫码支付
        data.put("product_id", "12");
        try {
            Map<String, String> resp = wxpay.unifiedOrder(data);
            System.out.println(resp);
            String return_code = resp.get("return_code");
            String return_msg = resp.get("return_msg");
            if (!return_code.equalsIgnoreCase("success")){
                return ServerResponse.error(9000,return_msg);
            }
            String result_code = resp.get("result_code");
            String err_code_des = resp.get("err_code_des");
            String transaction_id = resp.get("transaction_id");
            if (!result_code.equalsIgnoreCase("success")){
                //更新订单表
                Orderes orderes = new Orderes();
                orderes.setId(orderId1);
                orderes.setOrderPayTime(new Date());
                orderes.setOrderStatus(SystemConst.ORDER_STATUS_IS_SUCCESS);
                orderesMapper.updateById(orderes);
                //更新支付日志表
                PayLog payLog1 = new PayLog();
                payLog1.setOutTradeNo(outTradeNo);
                payLog1.setPayStatus(SystemConst.PAY_STATUS_IS_SUCCESS);
                BigDecimal orderTotalPrice = orderes.getOrderTotalPrice();
                payLog1.setPayMoney(orderTotalPrice);
                payLog1.setCreateTime(new Date());
                payLog1.setTransactionId(transaction_id);
                payLogMapper.updateById(payLog1);
                //清空支付日志的redis缓存
                RedisUtil.del(KeyUtil.buildPagLogKey(memberId));
                return ServerResponse.error(9000,err_code_des);
            }
            String url = resp.get("code_url");
            String orderId = payLog.getOrderId();
            BigDecimal payMoney1 = payLog.getPayMoney();
            Map<String,String> map = new HashMap<>();
            map.put("url",url);
            map.put("orderId",orderId);
            map.put("payMoney1",payMoney1.toString());
            return ServerResponse.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.error(9002,"微信内部支付错误");
        }

    }

    @Override
    public ServerResponse queryPay(Long memberId) {
        String payLogJson = RedisUtil.get(KeyUtil.buildPagLogKey(memberId));
        if (StringUtils.isEmpty(payLogJson)){
            return ServerResponse.error(ResponseEnum.PAYLOG_IS_NULL);
        }
        PayLog payLog = JSONObject.parseObject(payLogJson, PayLog.class);
        String outTradeNo = payLog.getOutTradeNo();
        MyConfig config = new MyConfig();
        WXPay wxpay = new WXPay(config);

        Map<String, String> data = new HashMap<String, String>();
        data.put("out_trade_no",outTradeNo);
        try {
            int i =0;
            while (true){
            Map<String, String> resp = wxpay.orderQuery(data);
            String return_code = resp.get("return_code");
            String return_msg = resp.get("return_msg");
            if (!return_code.equalsIgnoreCase("success")){
                return ServerResponse.error(9000,return_msg);

            }
            String result_code = resp.get("result_code");
            String err_code_des = resp.get("err_code_des");
            if (!result_code.equalsIgnoreCase("success")){
                return ServerResponse.error(9000,err_code_des);
            }
            String trade_state = resp.get("trade_state");
            String trade_state_desc = resp.get("trade_state_desc");
            if (trade_state.equalsIgnoreCase("success")){
                return ServerResponse.success(trade_state_desc);
            }
            if(i++ > 20){
                return ServerResponse.error(9003,"二维码已过期，请刷新页面重新获取");
            }
            Thread.sleep(3000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.error(9002,"微信内部支付错误");
        }
    }
}
