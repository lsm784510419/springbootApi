package com.fh.shop.api.smsResult.biz;

import com.fh.shop.api.common.ServerResponse;

public interface ISmsService {
    ServerResponse findPhone(String phone);
}
