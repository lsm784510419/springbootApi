package com.fh.shop.api.product.biz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.product.mapper.IProductMapper;
import com.fh.shop.api.product.po.Product;
import com.fh.shop.api.product.vo.ProductVo;
import com.fh.shop.api.util.DateUtil;
import com.fh.shop.api.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("productService")
@Transactional(rollbackFor = Exception.class)
public class IProductServiceImpl implements IProductService {
    @Autowired
    private IProductMapper productMapper;
    @Override
    @Transactional(readOnly = true)
    public ServerResponse findList() {
        //获取redis中key对应的值
        String cendProductJson = RedisUtil.get("cendProduct");
        //判断redis中是否存在数据
        if (StringUtils.isNotEmpty(cendProductJson)){
            List<ProductVo> productVoList = JSONObject.parseArray(cendProductJson, ProductVo.class);
            return ServerResponse.success(productVoList);
        }
        //不存在就去数据库查
        QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();
        productQueryWrapper.orderByDesc("id");
        productQueryWrapper.eq("status",1);
        List<Product> productList = productMapper.selectList(productQueryWrapper);
        List<ProductVo> productVoList = getProductVos(productList);
        //转换成json格式的数据
        String cendProduct = JSONObject.toJSONString(productVoList);
        //查出来之后存储一份到redis缓存服务器中
        RedisUtil.setEx("cendProduct",30,cendProduct);
        return ServerResponse.success(productVoList);
    }

    private List<ProductVo> getProductVos(List<Product> productList) {
        List<ProductVo> productVoList = new ArrayList<>();
        for (Product product : productList) {
            ProductVo productVo = getProductVo(product);
            productVoList.add(productVo);
        }
        return productVoList;
    }

    private ProductVo getProductVo(Product product) {
        ProductVo productVo = new ProductVo();
        productVo.setId(product.getId());
        productVo.setPrice(product.getPrice().toString());
        productVo.setProName(product.getProName());
        productVo.setCreateDate(DateUtil.data2Str(product.getCreateDate(),DateUtil.Y_M_D));
        productVo.setProImg(product.getProImg());
        return productVo;
    }
}
