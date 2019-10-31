package com.fh.shop.api.token.controller;

import com.fh.shop.api.common.Check;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.token.biz.ITokenService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/tokens")
public class TokenController {

    @Resource
    private ITokenService tokenService;
    @GetMapping
    @Check
    public ServerResponse findToken(){

        return tokenService.findToken();
    }
}
