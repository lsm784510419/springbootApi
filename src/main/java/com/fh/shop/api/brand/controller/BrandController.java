package com.fh.shop.api.brand.controller;

import com.fh.shop.api.brand.biz.IBrandService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/brand")
public class BrandController {
    @Resource(name = "brandService")
    private IBrandService brandService;

  /*  @RequestMapping("/findList")
    @Check
    public Object findList(String callback){
        ServerResponse list = brandService.findList();
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(list);
        mappingJacksonValue.setJsonpFunction(callback);
        return mappingJacksonValue;
    }*/

}
