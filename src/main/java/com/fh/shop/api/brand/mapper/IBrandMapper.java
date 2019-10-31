package com.fh.shop.api.brand.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.api.brand.param.SearchParamBrand;
import com.fh.shop.api.brand.po.brand.Brand;

import java.util.List;

public interface IBrandMapper extends BaseMapper<Brand> {


    Long findCount(SearchParamBrand searchParamBrand);

    List<Brand> findPageList(SearchParamBrand searchParamBrand);

}
