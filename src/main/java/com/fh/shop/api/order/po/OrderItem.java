package com.fh.shop.api.order.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("t_order_item")
public class OrderItem implements Serializable {

    private String orderId;//订单id 外键
    private Long productId;//商品id  外键
    private String productName;//商品名
    private BigDecimal productPrice;//单个商品价格
    private Long productCount;//用户已选中商品的数量
    private BigDecimal productSubTotalPrice;//商品的小计   价格
    private String productImg;//商品图片
    private Long memberId;//会员id    冗余参数/属性


}
