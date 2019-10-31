package com.fh.shop.api.memberArea.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_member_area")
public class MemberArea implements Serializable {

    private Long id;

    private Long area1;//省

    private Long area2;//市

    private Long area3;//县

    private String areaDetails;//详细地址

    private String addresseePhone;//收件人电话

    private String addresseeName;//收件人名字
    @TableField(exist = false)
    private String allAreaInfo;//全部信息

    private Long memberId;


}
