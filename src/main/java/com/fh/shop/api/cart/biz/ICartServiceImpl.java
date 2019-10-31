package com.fh.shop.api.cart.biz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.api.cart.vo.Cart;
import com.fh.shop.api.cart.vo.CartItem;
import com.fh.shop.api.common.ResponseEnum;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.common.SystemConst;
import com.fh.shop.api.product.mapper.IProductMapper;
import com.fh.shop.api.product.po.Product;
import com.fh.shop.api.util.BigDecimalUtil;
import com.fh.shop.api.util.KeyUtil;
import com.fh.shop.api.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("cartService")
@Transactional(rollbackFor = Exception.class)
public class ICartServiceImpl implements ICartService {
    @Autowired
    private IProductMapper productMapper;
    @Override
    public ServerResponse addCart(Long productId, Long count, Long memberId) {
        //根据商品id查询这条信息是否存在
        QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();
        productQueryWrapper.eq("id",productId);
        Product product = productMapper.selectOne(productQueryWrapper);
        //判断信息是否存在
        if (null == product){
            return ServerResponse.error(ResponseEnum.PRODUCTINFO_IS_NO);
        }
        //判断状态
        int status = product.getStatus();
        if (status == SystemConst.PRODUCT_IS_DOWN){
            return ServerResponse.error(ResponseEnum.PRODUCTSTATUS_IS_DOWN);
        }
        //获取购物车
        String cartInfo = RedisUtil.hget(SystemConst.CARTKEY, KeyUtil.buildCartFiled(memberId));
        //如果购物车为空，
        if (StringUtils.isEmpty(cartInfo)){
            //则创建购物车
            Cart cart = new Cart();
            //为购物车添加商品
            CartItem cartItem = buildCartItem(productId, count, product);
            cart.getItemCart().add(cartItem);
            //更新购物车
            updateCart(memberId, cart);
            return ServerResponse.success();
        }
        //走到这里证明有购物车
        //将购物车的转换java对象
        Cart cart = JSONObject.parseObject(cartInfo, Cart.class);
        //获取到购物车中数据的list集合
        List<CartItem> itemCart = cart.getItemCart();
        //循环判断购物车中的数据信息是否有我们这条数据
        CartItem cartItem = null;
        //获取到我们这条信息数据
        cartItem = getCartItem(productId, itemCart, cartItem);
        //进这个if判断的话就证明购物车中没有这条信息
        if(cartItem == null){
            //没有的话就创建这件商品的信息
            cartItem =buildCartItem(productId, count, product);
            itemCart.add(cartItem);
            //更新购物车
            updateCart(memberId, cart);
            return ServerResponse.success();
        }
        //没有进上边的if判断的话就证明购物车中有这条信息，这时候我们只是又多增加了一条信息
        //所以只需要修改个别信息，，比如这件商品的小计以及条数
        Long count1 = cartItem.getCount();
        cartItem.setCount(count1+count);
        cartItem.setSubTotalPrice(BigDecimalUtil.getsubTotalPrice(cartItem.getPrice(),cartItem.getCount().toString()));

        if(cartItem.getCount() < 1){
            itemCart.remove(cartItem);
        }
        updateCart(memberId, cart);
        return ServerResponse.success();
    }

    private CartItem getCartItem(Long productId, List<CartItem> itemCart, CartItem cartItem) {
        for (CartItem cartItemVo : itemCart) {
            //如果购物车中这条商品信息的id等于我们前台传过来的id就证明有这条信息
            //找到之后就break中断跳出
            if (cartItemVo.getProductId().equals(productId)){
                cartItem = cartItemVo;
                break;
            }
        }
        return cartItem;
    }

    //查询
    @Override
    public ServerResponse findCart(Long memberId) {
        String cartJson = RedisUtil.hget(SystemConst.CARTKEY, KeyUtil.buildCartFiled(memberId));
        if (StringUtils.isEmpty(cartJson)){
            return ServerResponse.error(ResponseEnum.CART_IS_NULL);
        }
        Cart cart = JSONObject.parseObject(cartJson, Cart.class);
        return ServerResponse.success(cart);
    }

    @Override
    public ServerResponse deleteCartItem(Long productId, Long memberId) {
        //获取购物车
        String cartInfo = RedisUtil.hget(SystemConst.CARTKEY, KeyUtil.buildCartFiled(memberId));
        //判断购物车是否为空
        if (StringUtils.isEmpty(cartInfo)){
            return ServerResponse.error(ResponseEnum.CART_IS_NULL);
        }
        //购物车不为空转换为java对象
        Cart cart = JSONObject.parseObject(cartInfo, Cart.class);
        //获取到list集合
        List<CartItem> itemCart = cart.getItemCart();
        //获取是否删除成功
        boolean isSuccess = delCartItem(productId, itemCart);
        if(!isSuccess){
            return ServerResponse.error(ResponseEnum.CARTITEM_IS_NULL);
        }
        updateCart(memberId,cart);
        return ServerResponse.success();
    }

    private boolean delCartItem(Long productId, List<CartItem> itemCart) {
        for (CartItem cartItem : itemCart) {
            if (cartItem.getProductId() == productId){
                itemCart.remove(cartItem);
                return true;
            }
        }
        return false;
    }

    private void updateCart(Long memberId, Cart cart) {
        //获取放数据的list集合
        List<CartItem> itemCart = cart.getItemCart();

        if (itemCart.size() == 0){
            RedisUtil.hdel(SystemConst.CARTKEY,KeyUtil.buildCartFiled(memberId));
        }else{
            //总条数先设置一个全局变量为0
            Long totalCount = 0L;
            //价格设置为0
            String totalPrice = "0";
            for (CartItem item : itemCart) {
                //循环放每条数据信息的集合   给总价钱和总条数赋值
                totalPrice = BigDecimalUtil.gettotalPrice(item.getSubTotalPrice(), totalPrice);
                totalCount = item.getCount()+totalCount;
            }
            //分别赋值
            cart.setItemCart(itemCart);
            cart.setTotalCount(totalCount);
            cart.setTotalPrice(totalPrice);
            //将购物车的数据转换为JSON格式的字符串
            String cartJson = JSONObject.toJSONString(cart);
            RedisUtil.hset(SystemConst.CARTKEY,KeyUtil.buildCartFiled(memberId),cartJson);
        }

    }

    private CartItem buildCartItem(Long productId, Long count, Product product) {
        //创建给购物车中添加的商品
        CartItem cartItem = new CartItem();
        cartItem.setProductId(productId);
        cartItem.setProductName(product.getProName());
        cartItem.setImage(product.getProImg());
        cartItem.setCount(count);
        String price = product.getPrice().toString();//转化为String类型的
        cartItem.setPrice(price);
        //计算小计的价格
        cartItem.setSubTotalPrice(BigDecimalUtil.getsubTotalPrice(price,String.valueOf(count)));
        return cartItem;
    }
}
