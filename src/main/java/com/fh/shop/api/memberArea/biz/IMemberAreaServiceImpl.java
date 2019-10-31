package com.fh.shop.api.memberArea.biz;

import com.fh.shop.api.common.ResponseEnum;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.memberArea.mapper.IMemberAreaMapper;
import com.fh.shop.api.memberArea.po.MemberArea;
import com.fh.shop.api.memberArea.vo.MemberAreaVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("memberAreaService")
@Transactional(rollbackFor = Exception.class)
public class IMemberAreaServiceImpl implements IMemberAreaService {

    @Autowired
    private IMemberAreaMapper memberAreaMapper;
    @Override
    public ServerResponse addMemberArea(MemberArea memberArea,Long memberId) {
        //非空判断
        //判断收件人姓名
        String addresseeName = memberArea.getAddresseeName();
        if (StringUtils.isEmpty(addresseeName)){
            return ServerResponse.error(ResponseEnum.ADDRESSEENAME_IS_ERROR);
        }
        //判断收件人电话
        String addresseePhone = memberArea.getAddresseePhone();
        if (StringUtils.isEmpty(addresseePhone)){
            return ServerResponse.error(ResponseEnum.ADDRESSEEPHONE_IS_ERROR);
        }
        //判断详细地址
        String areaDetails = memberArea.getAreaDetails();
        if (StringUtils.isEmpty(areaDetails)){
            return ServerResponse.error(ResponseEnum.AREADETAILS_IS_ERROR);
        }
        memberArea.setMemberId(memberId);
        memberAreaMapper.insert(memberArea);
        return ServerResponse.success();
    }

    @Override
    @Transactional(readOnly = true)
    public ServerResponse findMemberArea(Long memberId) {
        List<MemberArea> memberAreaList =  memberAreaMapper.findMemberArea(memberId);
        List<MemberAreaVo> memberAreaVoList = new ArrayList<>();
        for (MemberArea memberArea : memberAreaList) {
            MemberAreaVo memberAreaVo = new MemberAreaVo();
            memberAreaVo.setId(memberArea.getId());
            memberAreaVo.setAddresseeName(memberArea.getAddresseeName());
            memberAreaVo.setAllAreaInfo(memberArea.getAllAreaInfo());
            memberAreaVo.setAddresseePhone(memberArea.getAddresseePhone());
            memberAreaVoList.add(memberAreaVo);
        }
        return ServerResponse.success(memberAreaVoList);
    }

    @Override
    public ServerResponse deleteArea(Long id) {
        memberAreaMapper.deleteById(id);
        return ServerResponse.success();
    }

    @Override
    @Transactional(readOnly = true)
    public ServerResponse findMemberAreById(Long id) {
      /*  QueryWrapper<MemberArea> memberAreaQueryWrapper = new QueryWrapper<>();
        memberAreaQueryWrapper.eq("id",id);
        MemberArea memberArea = memberAreaMapper.selectOne(memberAreaQueryWrapper);*/
        MemberArea memberArea = memberAreaMapper.findMemberAreById(id);
        return ServerResponse.success(memberArea);
    }

    @Override
    public ServerResponse updateMemberArea(MemberArea memberArea) {
        memberAreaMapper.updateById(memberArea);
        return ServerResponse.success();
    }
}
