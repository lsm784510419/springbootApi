package com.fh.shop.api.pay.controller;

import com.fh.shop.api.common.Check;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.member.vo.MemberVo;
import com.fh.shop.api.pay.biz.IPayService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/pay")
public class PayController {
    @Resource(name = "payService")
    private IPayService payService;

    @PostMapping
    @RequestMapping("/payOrder")
    @Check
    public ServerResponse payOrder(MemberVo memberVo){
        Long memberId = memberVo.getId();
        return payService.payOrder(memberId);
    }
    @GetMapping
    @RequestMapping("/queryPay")
    @Check
    public ServerResponse queryPay(MemberVo memberVo){
        Long memberId = memberVo.getId();
        return payService.queryPay(memberId);
    }
}
