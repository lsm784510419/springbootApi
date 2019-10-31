package com.fh.shop.api.brand.param;

import com.fh.shop.api.common.Page;

import java.io.Serializable;

public class SearchParamBrand extends Page implements Serializable {

    private String brandName;

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
