package com.fh.shop.api.cart.controller;

import com.fh.shop.api.cart.biz.ICartService;
import com.fh.shop.api.common.Check;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.member.vo.MemberVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/carts")
public class CartApi {
    @Autowired
    private HttpServletRequest request;

    @Resource(name = "cartService")
    private ICartService cartService;
    @PostMapping
    @Check
    public ServerResponse addCart(Long productId, Long count,MemberVo memberVo){
        Long memberId = memberVo.getId();
        return cartService.addCart(productId,count,memberId);
    }

    @GetMapping
    @Check
    public ServerResponse findCart(MemberVo memberVo){
        Long memberId = memberVo.getId();
        return cartService.findCart(memberId);
    }

    @DeleteMapping("/{id}")
    @Check
    public ServerResponse deleteCartItem(@PathVariable("id") Long productId,MemberVo memberVo){
        Long memberId = memberVo.getId();
        return cartService.deleteCartItem(productId,memberId);
    }
}
