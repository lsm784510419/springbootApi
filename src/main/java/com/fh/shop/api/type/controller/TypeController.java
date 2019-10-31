package com.fh.shop.api.type.controller;

import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.type.biz.ITypeService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/type")
@CrossOrigin
public class TypeController {

    @Resource(name = "typeService")
    private ITypeService typeService;

    @RequestMapping("/findTypeList")
    public ServerResponse findTypeList(){
        return typeService.findTypeList();
    }
}
