package com.fh.shop.api.memberArea.biz;

import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.memberArea.po.MemberArea;

public interface IMemberAreaService {
    ServerResponse addMemberArea(MemberArea memberArea, Long memberId);

    ServerResponse findMemberArea(Long memberId);

    ServerResponse deleteArea(Long id);

    ServerResponse findMemberAreById(Long id);

    ServerResponse updateMemberArea(MemberArea memberArea);

}
