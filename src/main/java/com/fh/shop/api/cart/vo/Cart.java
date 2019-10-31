package com.fh.shop.api.cart.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Cart implements Serializable {

    private Long totalCount;

    private String totalPrice;

    private List<CartItem> itemCart = new ArrayList<>();

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<CartItem> getItemCart() {
        return itemCart;
    }

    public void setItemCart(List<CartItem> itemCart) {
        this.itemCart = itemCart;
    }
}
