package com.fh.shop.api.member.biz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.api.common.ResponseEnum;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.common.SystemConst;
import com.fh.shop.api.member.mapper.IMemberMapper;
import com.fh.shop.api.member.po.Member;
import com.fh.shop.api.member.vo.MemberVo;
import com.fh.shop.api.util.KeyUtil;
import com.fh.shop.api.util.Md5Util;
import com.fh.shop.api.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.UUID;

@Service("memberService")
@Transactional(rollbackFor = Exception.class)
public class IMemberServiceImpl implements IMemberService {
    @Autowired
    private IMemberMapper memberMapper;
    @Override
    public ServerResponse addMember(Member member) {
        //非空判断
        //判断用户名
        String userName = member.getUserName();
        if (StringUtils.isEmpty(userName)){
            return ServerResponse.error(ResponseEnum.USERNAME_IS_NULL);
        }
        //判断真实姓名
        String realName = member.getRealName();
        if (StringUtils.isEmpty(realName)){
            return ServerResponse.error(ResponseEnum.REALNAME_IS_NULL);
        }
        //判断密码
        String password = member.getPassword();
        if (StringUtils.isEmpty(password)){
            return ServerResponse.error(ResponseEnum.PASSWORD_IS_NULL);
        }
        //判断手机号
        String phone = member.getPhone();
        if (StringUtils.isEmpty(phone)){
            return ServerResponse.error(ResponseEnum.PHONE_IS_NULL);
        }
        //判断验证码
        String authCode = member.getAuthCode();
        if (StringUtils.isEmpty(authCode)){
            return ServerResponse.error(ResponseEnum.CODE_IS_NULL);
        }

        //判断email
        String email = member.getEmail();
        if (StringUtils.isEmpty(email)){
            return ServerResponse.error(ResponseEnum.EMAIL_IS_NULL);
        }
        //判断验证码正确性
        String code = RedisUtil.get(KeyUtil.buildSmsKey(phone));
        if (StringUtils.isEmpty(code)){
            return ServerResponse.error(ResponseEnum.CODE_IS_ERROR);
        }
        if (!code.equals(authCode)){
            return ServerResponse.error(ResponseEnum.CODE_IS_EXCEPTION);
        }
        //唯一性判断
        //判断会员名是否存在
        QueryWrapper<Member> memberQueryWrapperUserName = new QueryWrapper<>();
        memberQueryWrapperUserName.eq("userName",userName);
        Member memberUserName = memberMapper.selectOne(memberQueryWrapperUserName);
        if (memberUserName != null){
            return ServerResponse.error(ResponseEnum.USERNAME_IS_EXIST);
        }
        //判断手机号是否存在
        QueryWrapper<Member> memberQueryWrapperPhone = new QueryWrapper<>();
        memberQueryWrapperPhone.eq("phone",phone);
        Member memberPhone = memberMapper.selectOne(memberQueryWrapperPhone);
        if (memberPhone != null){
            return ServerResponse.error(ResponseEnum.PHONE_IS_EXIST);
        }
        //判断email是否存在
        QueryWrapper<Member> memberQueryWrapperEmail = new QueryWrapper<>();
        memberQueryWrapperEmail.eq("email",email);
        Member memberEmail = memberMapper.selectOne(memberQueryWrapperEmail);
        if (memberEmail != null){
            return ServerResponse.error(ResponseEnum.EMAIL_IS_EXIST);
        }
        //注册成功
        memberMapper.insert(member);
        return ServerResponse.success();
    }

    @Override
    @Transactional(readOnly = true)
    public ServerResponse findUserName(String userName) {
        if(StringUtils.isEmpty(userName)){
            return ServerResponse.error(ResponseEnum.USERNAME_IS_NULL);
        }
        QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();
        memberQueryWrapper.eq("userName",userName);
        Member member = memberMapper.selectOne(memberQueryWrapper);
        if (member != null){
            return ServerResponse.error(ResponseEnum.USERNAME_IS_EXIST);
        }
        return ServerResponse.success();
    }

    @Override
    @Transactional(readOnly = true)
    public ServerResponse findEmail(String email) {
        if (StringUtils.isEmpty(email)){
            return ServerResponse.error(ResponseEnum.EMAIL_IS_NULL);
        }
        QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();
        memberQueryWrapper.eq("email",email);
        Member member = memberMapper.selectOne(memberQueryWrapper);
        if (member != null){
            return ServerResponse.error(ResponseEnum.EMAIL_IS_EXIST);
        }
        return ServerResponse.success();
    }
    public ServerResponse findPhone(String phone){
        if (StringUtils.isEmpty(phone)){
            return ServerResponse.error(ResponseEnum.PHONE_IS_NULL);
        }
        QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();
        memberQueryWrapper.eq("phone",phone);
        Member member = memberMapper.selectOne(memberQueryWrapper);
        if (member != null){
            return ServerResponse.error(ResponseEnum.PHONE_IS_EXIST);
        }

        return ServerResponse.success();
    }

    @Override
    @Transactional(readOnly = true)
    public ServerResponse login(Member member) {
        //非空判断
        String userName = member.getUserName();
        String password = member.getPassword();
        if (StringUtils.isEmpty(userName)){
            return ServerResponse.error(ResponseEnum.USERNAME_IS_NULL);
        }
        if (StringUtils.isEmpty(password)){
            return ServerResponse.error(ResponseEnum.PASSWORD_IS_NULL);
        }
        //验证会员名
        QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();
        memberQueryWrapper.eq("userName",userName);
        Member memberInfo = memberMapper.selectOne(memberQueryWrapper);
        if (memberInfo == null){
            return ServerResponse.error(ResponseEnum.MEMBER_IS_NULL);
        }
        //验证密码
        String md5Pass = Md5Util.md5(password);
        if (!memberInfo.getPassword().equals(password)){
            return ServerResponse.error(ResponseEnum.MEMBER_PASSWORD_IS_NULL);
        }
        //构建存到客户端的信息
        MemberVo memberVo = new MemberVo();
        memberVo.setUserName(userName);
        memberVo.setId(memberInfo.getId());
        String uuid = UUID.randomUUID().toString();
        memberVo.setUuid(uuid);
        memberVo.setRealName(memberInfo.getRealName());
        //将其转换为json格式的字符串
        String memberVoJson = JSONObject.toJSONString(memberVo);
        //进行base64位编码  使用jdk1.8自带的
        String base64 = null;
        try {
            base64 = Base64.getEncoder().encodeToString(memberVoJson.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //生成签名
        String sing = Md5Util.sing(base64, SystemConst.SECRET);
        //将签名转为base64位编码  为了好看
        String base64Sing = Base64.getEncoder().encodeToString(sing.getBytes());
        //放入redis中
        RedisUtil.setEx(KeyUtil.buildMenberKey(userName,uuid),SystemConst.MENBER_EXPIRE,"yyy");
        //将信息响应给前台
        return ServerResponse.success(base64+"."+base64Sing);
    }

    @Override
    @Transactional(readOnly = true)
    public ServerResponse phoneLogin(Member member) {
        //非空验证
        String realNale = member.getRealName();
        //判断手机号
        String phone = member.getPhone();
        if (StringUtils.isEmpty(phone)){
            return ServerResponse.error(ResponseEnum.PHONE_IS_NULL);
        }
        //判断验证码
        String authCode = member.getAuthCode();
        if (StringUtils.isEmpty(authCode)){
            return ServerResponse.error(ResponseEnum.CODE_IS_NULL);
        }
        QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();
        memberQueryWrapper.eq("phone",phone);
        Member memberInfo = memberMapper.selectOne(memberQueryWrapper);
        if (memberInfo == null ){
            return ServerResponse.error(ResponseEnum.PHONELOGIN_IS_NULL);
        }
        String code = RedisUtil.get(KeyUtil.buildSmsKey(phone));
        if (!authCode.equals(code)){
            return ServerResponse.error(ResponseEnum.CODE_IS_EXCEPTION);
        }
        //构建存到客户端的信息
        MemberVo memberVo = new MemberVo();
        memberVo.setUserName(memberInfo.getUserName());
        memberVo.setRealName(memberInfo.getRealName());
        String uuid = UUID.randomUUID().toString();
        memberVo.setUuid(uuid);
        memberVo.setId(memberInfo.getId());
        //将其转换为JSON格式的字符串
        String memberVoJSON = JSONObject.toJSONString(memberVo);
        //转换base64位的编码
        String base64MemberInfo = Base64.getEncoder().encodeToString(memberVoJSON.getBytes());
        //生成签名
        String sing = Md5Util.sing(base64MemberInfo, SystemConst.SECRET);
        //转换为base64编码的签名
        String base64Sing = Base64.getEncoder().encodeToString(sing.getBytes());
        //存入redis
        RedisUtil.setEx(KeyUtil.buildMenberKey(memberVo.getUserName(),memberVo.getUuid()),SystemConst.MENBER_EXPIRE,"11");
        return ServerResponse.success(base64MemberInfo+"."+base64Sing);
    }

    @Override
    @Transactional(readOnly = true)
    public ServerResponse phoneYZ(String phone) {
        QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Member> phoneYZ = memberQueryWrapper.eq("phone", phone);
        Member member = memberMapper.selectOne(phoneYZ);
        if (null == member){
            return ServerResponse.error(ResponseEnum.PHONELOGIN_IS_NULL);
        }
        return ServerResponse.success();
    }

/*    @Override
    public ServerResponse findRealNameById(Long id) {
        QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();
        memberQueryWrapper.eq("id",id);
        Member member = memberMapper.selectOne(memberQueryWrapper);
        return ServerResponse.success(member.getRealName());
    }*/
}
