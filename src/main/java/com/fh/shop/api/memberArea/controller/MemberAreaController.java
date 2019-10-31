package com.fh.shop.api.memberArea.controller;

import com.fh.shop.api.common.Check;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.member.vo.MemberVo;
import com.fh.shop.api.memberArea.biz.IMemberAreaService;
import com.fh.shop.api.memberArea.po.MemberArea;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/memberAreas")
public class MemberAreaController {

    @Resource(name = "memberAreaService")
    private IMemberAreaService memberAreaService;

    @PostMapping("/addMemberArea")
    @Check
    public ServerResponse addMemberArea(MemberArea memberArea, MemberVo memberVo){
        Long memberId = memberVo.getId();
        return memberAreaService.addMemberArea(memberArea,memberId);
    }

    @GetMapping("/findMemberArea")
    @Check
    public ServerResponse findMemberArea(MemberVo memberVo){
        Long memberId = memberVo.getId();
        return memberAreaService.findMemberArea(memberId);
    }

    @DeleteMapping("/{id}")
    public ServerResponse deleteArea(@PathVariable Long id){
        return memberAreaService.deleteArea(id);
    }

    @GetMapping("/findMemberAreById")
    public ServerResponse findMemberAreById(Long id){

        return memberAreaService.findMemberAreById(id);
    }

    @PutMapping("/updateMemberArea")
    public ServerResponse updateMemberArea(@RequestBody MemberArea memberArea){
        return memberAreaService.updateMemberArea(memberArea);
    }
}
