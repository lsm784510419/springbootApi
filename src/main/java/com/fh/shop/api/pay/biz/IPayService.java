package com.fh.shop.api.pay.biz;

import com.fh.shop.api.common.ServerResponse;

public interface IPayService {

    ServerResponse payOrder(Long memberId);

    ServerResponse queryPay(Long memberId);
}
