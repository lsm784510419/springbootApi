package com.fh.shop.api.area.controller;

import com.fh.shop.api.area.biz.IAreaService;
import com.fh.shop.api.common.Check;
import com.fh.shop.api.common.ServerResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/areas")
public class AreaController {

    @Resource(name = "areaService")
    private IAreaService areaService;
    //三级联动所需要查询
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public ServerResponse findAreaSelect( @PathVariable Long id){
        return areaService.findAreaSelect(id);
    }

    /*添加订单时的三级联动*/
    @Check
    @GetMapping("/findCodeArea")
    public ServerResponse findCodeArea(Long id){

        return areaService.findAreaSelect(id);
    }
}
