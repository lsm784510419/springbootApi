package com.fh.shop.api.cart.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CartItem implements Serializable {
    private Long productId;

    private String productName;

    private String image;

    private String price;

    private Long count;

    private String subTotalPrice;


}
