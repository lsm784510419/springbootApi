package com.fh.shop.api.member.controller;

import com.fh.shop.api.common.Check;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.member.biz.IMemberService;
import com.fh.shop.api.member.po.Member;
import com.fh.shop.api.member.vo.MemberVo;
import com.fh.shop.api.util.KeyUtil;
import com.fh.shop.api.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/members")
@CrossOrigin("http://localhost:8087")
public class MemberController {

    @Resource(name = "memberService")
    private IMemberService memberService;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(method = RequestMethod.POST)
    public ServerResponse addMember(Member member){
        return memberService.addMember(member);
    }
    @RequestMapping(value = "/userName",method = RequestMethod.GET)
    public ServerResponse findUserName(String userName){

        return memberService.findUserName(userName);
    }
    @GetMapping(value = "/email")
    public ServerResponse findEmail(String email){

        return memberService.findEmail(email);
    }
    @GetMapping(value = "/phone")
    public ServerResponse findPhone(String phone){

        return memberService.findPhone(phone);
    }

    @PostMapping(value = "/login")
    public ServerResponse login(Member member){

        return memberService.login(member);
    }
    @GetMapping("/memberInfo")
    @Check
    public ServerResponse memberInfo(){
        MemberVo memberVo = (MemberVo) request.getAttribute("memberVo");

        String realName = memberVo.getRealName();
        return ServerResponse.success(realName);
    }
    @GetMapping("/loginOut")
    @Check
    public ServerResponse loginOut(){
        MemberVo memberVo = (MemberVo) request.getAttribute("memberVo");
        String userName = memberVo.getUserName();
        String uuid = memberVo.getUuid();
        RedisUtil.del(KeyUtil.buildMenberKey(userName,uuid));
        return ServerResponse.success();
    }

    @PostMapping("/phoneLogin")
    public ServerResponse phoneLogin(Member member){

        return memberService.phoneLogin(member);
    }
    @PostMapping("/phoneYZ")
    public ServerResponse phoneYZ(String phone){

        return memberService.phoneYZ(phone);
    }

































/*    @RequestMapping(method = RequestMethod.GET)
    public ServerResponse findCode(String phone){
        try {
            String authCode = NoteUtil.getAuthCode(phone);
            RedisUtil.setEx(phone,60,authCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ServerResponse.success();
    }*/


/*      String codeJson = RedisUtil.get(member.getPhone());
        if (StringUtils.isEmpty(member.getAuthCode())){
            return ServerResponse.error(ResponseEnum.CODE_IS_NULL);
        }
        if (!(member.getAuthCode().equals(codeJson))){
            return ServerResponse.error(ResponseEnum.CODE_IS_ERROR);
        }
        String md5 = Md5Util.md5(member.getPassword());
        member.setPassword(md5);
        memberService.addMember(member);
        return ServerResponse.success();*/
}
