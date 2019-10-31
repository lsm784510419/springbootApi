package com.fh.shop.api.token.biz;

import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.util.RedisUtil;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("tokenService")
public class ITokenServiceImpl implements ITokenService {
    @Override
    public ServerResponse findToken() {
        String token = UUID.randomUUID().toString();
        RedisUtil.set(token,"2");
        return ServerResponse.success(token);
    }
}
