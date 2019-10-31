package com.fh.shop.api.member.biz;

import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.member.po.Member;

public interface IMemberService {
    ServerResponse addMember(Member member);

    ServerResponse findUserName(String userName);

    ServerResponse findEmail(String email);

    ServerResponse findPhone(String phone);

    ServerResponse login(Member member);

    ServerResponse phoneLogin(Member member);

    ServerResponse phoneYZ(String phone);

    /*  ServerResponse findRealNameById(Long id);*/
}
