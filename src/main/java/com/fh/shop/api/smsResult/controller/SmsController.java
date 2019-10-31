package com.fh.shop.api.smsResult.controller;

import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.smsResult.biz.ISmsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/sms")
@CrossOrigin("http://localhost:8087")
public class SmsController {
    @Resource(name = "smsService")
    private ISmsService smsService;

    @RequestMapping(method = RequestMethod.GET)
    public ServerResponse findCode(String phone){

        return smsService.findPhone(phone);
    }
}
