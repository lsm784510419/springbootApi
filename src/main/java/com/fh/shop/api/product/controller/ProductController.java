package com.fh.shop.api.product.controller;

import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.product.biz.IProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Resource(name = "productService")
    private IProductService productService;


    @GetMapping("/findList")
    @ApiOperation(value = "所有上架的商品",httpMethod = "GET",notes = "公共资源")
    @ApiImplicitParam(value = "不需要传参数",name = "需要传的参数：无参数")
    public ServerResponse findList(){
        ServerResponse list = productService.findList();

        return list;
    }
}
