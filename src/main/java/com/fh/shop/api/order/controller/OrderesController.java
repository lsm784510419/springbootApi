package com.fh.shop.api.order.controller;

import com.fh.shop.api.common.Check;
import com.fh.shop.api.common.Idempotent;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.member.vo.MemberVo;
import com.fh.shop.api.order.biz.IOrderesService;
import com.fh.shop.api.order.param.OrderParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/orders")
public class OrderesController {

    @Resource(name = "orderService")
    private IOrderesService orderService;

    @PostMapping("/addOrder")
    @Check
    @Idempotent
    public ServerResponse addOrder(OrderParam orderParam, MemberVo memberVo){
        Long memberId = memberVo.getId();
        return orderService.addOrder(orderParam,memberId);
    }
}
