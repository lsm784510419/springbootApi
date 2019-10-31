package com.fh.shop.api.memberArea.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class MemberAreaVo implements Serializable {

    private Long id;

    private String addresseeName;//收件人姓名

    private String allAreaInfo;//全部信息

    private String addresseePhone;

}
