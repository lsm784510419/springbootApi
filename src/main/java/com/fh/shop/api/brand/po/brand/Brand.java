package com.fh.shop.api.brand.po.brand;

import java.io.Serializable;

public class Brand implements Serializable {
    private Long id;

    private String brandName;

    private String brandImg;

    private Integer isHot;

    private Integer isSort;

    public Integer getIsSort() {
        return isSort;
    }

    public void setIsSort(Integer isSort) {
        this.isSort = isSort;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandImg() {
        return brandImg;
    }

    public void setBrandImg(String brandImg) {
        this.brandImg = brandImg;
    }

    public Integer getIsHot() {
        return isHot;
    }

    public void setIsHot(Integer isHot) {
        this.isHot = isHot;
    }
}
