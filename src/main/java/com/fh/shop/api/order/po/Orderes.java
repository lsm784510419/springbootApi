package com.fh.shop.api.order.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("t_order")
public class Orderes implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id; //订单编号
    private Long memberId;//当前下单的会员id
    private Integer payType;//支付方式  1 微信支付  2  货到付款
    private BigDecimal orderTotalPrice; //订单的总金额
    private Long orderTotalCount; //订单中商品的总个数
    private Date orderCreateTime;//订单的创建时间
    private Date orderPayTime;//订单的支付时间
    private Integer orderStatus; //状态 1 支付  2未支付  3  已发货 4 确认订单  5    已完成评价
    private String orderStatusInfo;//订单的状态描述
    private Date orderCloseTime;//订单交易关闭的时间
    private Date orderDeliverTime;//订单发货时间
    private Date orderWinTime;//订单交易成功时间
    private Date orderAppraiseTime;//订单完成评价时间
    private String addressee; //收货人地址
    private String addresseePhone; //收货人电话
    private String addresseeName; //收货人姓名
    private String zipCode;//邮编
    private BigDecimal postage;//邮费
    private Integer isBill;//发票是否打印
    
}
