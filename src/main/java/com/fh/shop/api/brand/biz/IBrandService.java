package com.fh.shop.api.brand.biz;

import com.fh.shop.api.brand.param.SearchParamBrand;
import com.fh.shop.api.brand.po.brand.Brand;
import com.fh.shop.api.common.ServerResponse;

public interface IBrandService {
    ServerResponse findList();

    ServerResponse addBrand(Brand brand);

    ServerResponse findList1();

    ServerResponse findUpdate(Long id);

    ServerResponse deleteBrand(Long id);

    ServerResponse updateBrand(Brand brand);

    ServerResponse deleteBatch(String ids);

    ServerResponse findPageList(SearchParamBrand searchParamBrand);

}
