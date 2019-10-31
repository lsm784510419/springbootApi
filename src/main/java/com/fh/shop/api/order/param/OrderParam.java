package com.fh.shop.api.order.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderParam implements Serializable {

    private Integer payType;//支付类型

    private String addresseePhone;//收货人电话

    private String addressee;//收货人地址

    private String addresseeName;//收货人姓名

    private Integer isBill;//是否打印发票
}
