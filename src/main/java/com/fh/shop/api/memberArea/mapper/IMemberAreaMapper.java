package com.fh.shop.api.memberArea.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.api.memberArea.po.MemberArea;

import java.util.List;

public interface IMemberAreaMapper extends BaseMapper<MemberArea> {
    List<MemberArea> findMemberArea(Long memberId);

    MemberArea findMemberAreById(Long id);
}
