package com.fh.shop.api.brand.biz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.api.brand.mapper.IBrandMapper;
import com.fh.shop.api.brand.param.SearchParamBrand;
import com.fh.shop.api.brand.po.brand.Brand;
import com.fh.shop.api.common.Datetables;
import com.fh.shop.api.common.ResponseEnum;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("brandService")
@Transactional(rollbackFor = Exception.class)
public class IBrandServiceImpl implements IBrandService {
    @Autowired
    private IBrandMapper brandMapper;
    @Override
    @Transactional(readOnly = true)
    public ServerResponse findList() {
        //判断缓存中是否存在这个数据
        String hotBrandListJson = RedisUtil.get("hotBrandList");
        if (StringUtils.isNotEmpty(hotBrandListJson)){
            List<Brand> brands = JSONObject.parseArray(hotBrandListJson, Brand.class);
            return ServerResponse.success(brands);
        }
        //缓存中不存在，则到数据库中查询
        QueryWrapper<Brand> brandQueryWrapper = new QueryWrapper<>();
        brandQueryWrapper.orderByAsc("isSort");
        brandQueryWrapper.eq("isHot",1);
        List<Brand> brandListDB = brandMapper.selectList(brandQueryWrapper);
        //将查询出来的java对象转换为json格式的字符串
        String brandListJson = JSONObject.toJSONString(brandListDB);
        //给缓存中存储一份
        RedisUtil.set("hotBrandList",brandListJson);
        return ServerResponse.success(brandListDB);
    }

    @Override
    public ServerResponse addBrand(Brand brand) {
        brandMapper.insert(brand);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse findList1() {
        List<Brand> brands = brandMapper.selectList(null);
        return ServerResponse.success(brands);
    }

    @Override
    public ServerResponse findUpdate(Long id) {
        if (id == null){
            return ServerResponse.error(ResponseEnum.BRAND_IS_NULL);
        }
        Brand brand = brandMapper.selectById(id);
        return ServerResponse.success(brand);
    }

    @Override
    public ServerResponse deleteBrand(Long id) {
        brandMapper.deleteById(id);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse updateBrand(Brand brand) {
        brandMapper.updateById(brand);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse deleteBatch(String ids) {
        if (StringUtils.isNotEmpty(ids)){
            return ServerResponse.error(ResponseEnum.IDS_IS_NULL);
        }
        String[] split = ids.split(",");
        List<Long> idList = new ArrayList<>();
        for (String s : split) {
           idList.add(Long.valueOf(s));
        }
        brandMapper.deleteBatchIds(idList);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse findPageList(SearchParamBrand searchParamBrand) {
         Long count = brandMapper.findCount(searchParamBrand);
         List<Brand> brandList = brandMapper.findPageList(searchParamBrand);
        Datetables datetables = new Datetables(searchParamBrand.getDraw(),count,count,brandList);
        return ServerResponse.success(datetables);
    }
}
