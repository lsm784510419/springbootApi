package com.fh.shop.api.brand.controller;

import com.fh.shop.api.brand.biz.IBrandService;
import com.fh.shop.api.brand.param.SearchParamBrand;
import com.fh.shop.api.brand.po.brand.Brand;
import com.fh.shop.api.common.Check;
import com.fh.shop.api.common.ServerResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/brands")
public class BrandApi {
    @Resource(name = "brandService")
    private IBrandService brandService;

    @RequestMapping(method = RequestMethod.POST)
    public ServerResponse add1(Brand brand){

        return brandService.addBrand(brand);
    }
    @RequestMapping(value = "/findAll",method = RequestMethod.GET)
    public ServerResponse findListAll(){

        return brandService.findList1();
    }
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public ServerResponse deleteBrand(@PathVariable Long id){
        return brandService.deleteBrand(id);
    }
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public ServerResponse findUpdate(@PathVariable Long id){
        return brandService.findUpdate(id);
    }
    @RequestMapping(method = RequestMethod.PUT)
    public ServerResponse updateBrand(@RequestBody Brand brand){
        return  brandService.updateBrand(brand);
    }
    @RequestMapping(method = RequestMethod.DELETE)
    public ServerResponse deleteBatch(String ids){

        return brandService.deleteBatch(ids);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ServerResponse findPageList(SearchParamBrand searchParamBrand){

        return brandService.findPageList(searchParamBrand);
    }

    @GetMapping(value = "/findList")
    @Check
    public ServerResponse findList(){
       return  brandService.findList();
    }

}
