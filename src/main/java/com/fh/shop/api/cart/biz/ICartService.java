package com.fh.shop.api.cart.biz;

import com.fh.shop.api.common.ServerResponse;

public interface ICartService {
    ServerResponse addCart(Long productId, Long count, Long memberId);

    ServerResponse findCart(Long memberId);

    ServerResponse deleteCartItem(Long productId, Long memberId);
}
