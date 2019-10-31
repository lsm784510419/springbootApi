package com.fh.shop.api.order.biz;

import com.alibaba.fastjson.JSONObject;
import com.fh.shop.api.cart.vo.Cart;
import com.fh.shop.api.cart.vo.CartItem;
import com.fh.shop.api.common.ResponseEnum;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.common.SystemConst;
import com.fh.shop.api.order.mapper.IOrderItemMapper;
import com.fh.shop.api.order.mapper.IOrderesMapper;
import com.fh.shop.api.order.param.OrderParam;
import com.fh.shop.api.order.po.OrderItem;
import com.fh.shop.api.order.po.Orderes;
import com.fh.shop.api.payLog.mapper.IPayLogMapper;
import com.fh.shop.api.payLog.po.PayLog;
import com.fh.shop.api.product.mapper.IProductMapper;
import com.fh.shop.api.product.po.Product;
import com.fh.shop.api.util.BigDecimalUtil;
import com.fh.shop.api.util.IdUtil;
import com.fh.shop.api.util.KeyUtil;
import com.fh.shop.api.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service("orderService")
@Transactional(rollbackFor = Exception.class)
public class IOrderesServiceImpl implements IOrderesService {
    @Autowired
    private IOrderesMapper orderesMapper;

    @Autowired
    private IOrderItemMapper orderItemMapper;

    @Autowired
    private IProductMapper productMapper;

    @Autowired
    private IPayLogMapper payLogMapper;
    //增加订单
    @Override
    public ServerResponse addOrder(OrderParam orderParam, Long memberId) {
        //查询出购物车缓存的商品信息
        String cartInfo = RedisUtil.hget(SystemConst.CARTKEY, KeyUtil.buildCartFiled(memberId));
        if (StringUtils.isEmpty(cartInfo)){
            return ServerResponse.error(ResponseEnum.CART_IS_NULL);
        }
        //将redis中取出的购物车json格式的数据转换为java对象
        Cart cart = JSONObject.parseObject(cartInfo, Cart.class);
        //时间戳+雪花算法的Id   订单表的Id
        String timeId = IdUtil.idXueHua();
        //插入订单详细信息
        //获取信息商品项的详情
        List<CartItem> itemCart = cart.getItemCart();
        //创建一个空集合 用来放库存量不够的商品信息
        List<CartItem> cartItemList = new ArrayList<>();
        Iterator<CartItem> iterator = itemCart.iterator();
        for (itemCart.iterator();iterator.hasNext();) {
            CartItem cartItem = iterator.next();
            OrderItem orderItem = new OrderItem();//构建订单详细信息数据载体
            orderItem.setOrderId(timeId);//订单id
            orderItem.setProductId(cartItem.getProductId());//商品id
            orderItem.setProductName(cartItem.getProductName());//商品名
            orderItem.setProductSubTotalPrice(new BigDecimal(cartItem.getSubTotalPrice()));//商品小计
            orderItem.setProductCount(cartItem.getCount());//商品数量
            orderItem.setProductImg(cartItem.getImage());
            orderItem.setMemberId(memberId);
            Long productId = orderItem.getProductId();//获取商品iD
            Product product = productMapper.selectById(productId);//根据商品ID去商品表查询出对应的商品
            Long stock = product.getStock();//获取商品表中当前商品的库存量
            Long productCount = orderItem.getProductCount();//获取购物车中商品的数量，就是要买的数量
            if (stock >= productCount){//如果商品表中的库存量大于购买数量
                product.setStock(stock - productCount);//就将商品表中的库存量减去购买的数量
                Long updateRow = productMapper.updateStock(productId, productCount);//更改商品表中库存量
                if (updateRow <= 0 ){
                    //将不需要插入的放入这个list集合中  就是销量不够的
                    cartItemList.add(cartItem);
                    //删除数量不够卖的商品
                    iterator.remove();
                }else {
                    orderItemMapper.insert(orderItem);//将信息插入详情表
                }
            }else {
                //将不需要插入的放入这个list集合中  就是销量不够的
                cartItemList.add(cartItem);
                //删除数量不够卖的商品
                iterator.remove();
            }
        }
        if (itemCart.size() == 0){
            return ServerResponse.error(ResponseEnum.STOCK_IS_ERROR);
        }
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
        cart.setTotalCount(totalCount);
        cart.setTotalPrice(totalPrice);

        //插入订单

        Orderes orderes = new Orderes();
        orderes.setId(timeId);
        orderes.setMemberId(memberId);
        orderes.setPayType(orderParam.getPayType());
        orderes.setOrderTotalPrice(new BigDecimal(cart.getTotalPrice()));
        orderes.setOrderTotalCount(cart.getTotalCount());
        orderes.setOrderCreateTime(new Date());
        orderes.setOrderStatus(SystemConst.ORDER_NON_PAYMENT);//支付状态
        orderes.setAddressee(orderParam.getAddressee());//收货人地址
        orderes.setAddresseeName(orderParam.getAddresseeName());//收货人姓名
        orderes.setAddresseePhone(orderParam.getAddresseePhone());//收货人电话
        orderes.setIsBill(orderParam.getIsBill());
        orderesMapper.insert(orderes);
        //插入日志支付表
        PayLog payLog = new PayLog();
        //雪花算法  日志表的Id
        String timeId1 = IdUtil.idXueHua();
        payLog.setOutTradeNo(timeId1);//日志表的主键
        payLog.setMemberId(memberId);//会员id
        payLog.setOrderId(timeId);//
        payLog.setCreateTime(new Date());
        payLog.setPayMoney(new BigDecimal(totalPrice));
        payLog.setPayType(orderParam.getPayType());
        payLog.setPayStatus(SystemConst.PAY_STATUS_IS_NOT);
        payLogMapper.insert(payLog);
        String payLogJson = JSONObject.toJSONString(payLog);
        RedisUtil.set(KeyUtil.buildPagLogKey(memberId),payLogJson);
        RedisUtil.hdel(SystemConst.CARTKEY,KeyUtil.buildCartFiled(memberId));
        return ServerResponse.success(cartItemList);
    }
}
