package com.fh.shop.api.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.api.product.mapper.IProductMapper;
import com.fh.shop.api.product.po.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TimingSendEmail {
    /**
     * CRON表达式	含义
     * “0 0 12 * * ?”	每天中午十二点触发
     * “0 15 10 ? * *”	每天早上10：15触发
     * “0 15 10 * * ?”	每天早上10：15触发
     * “0 15 10 * * ? *”	每天早上10：15触发
     * “0 15 10 * * ? 2005”	2005年的每天早上10：15触发
     * “0 * 14 * * ?”	每天从下午2点开始到2点59分每分钟一次触发
     * “0 0/5 14 * * ?”	每天从下午2点开始到2：55分结束每5分钟一次触发
     * “0 0/5 14,18 * * ?”	每天的下午2点至2：55和6点至6点55分两个时间段内每5分钟一次触发
     * “0 0-5 14 * * ?”	每天14:00至14:05每分钟一次触发
     * “0 10,44 14 ? 3 WED”	三月的每周三的14：10和14：44触发
     * “0 15 10 ? * MON-FRI”	每个周一、周二、周三、周四、周五的10：15触发
     */

   @Autowired
    private IProductMapper productMapper;
   // @Scheduled(cron="0 */1 * * * ?")
    //public void test(){
      //  System.out.println("定时器1：时间=" + new Date()); // 1次
   // }
    @Scheduled(cron="0 */1 * * * ?")
    public void SendEmail(){
        QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();
        productQueryWrapper.lt("stock",5);
        List<Product> productList = productMapper.selectList(productQueryWrapper);
        if ( null == productList){
            return ;
        }
        StringBuffer str = new StringBuffer();
        for (Product product : productList) {
            str.append("商品名：").append(product.getProName()).append("商品价格：").append(product.getPrice()).append("库存量不足了,仅剩余：").append(product.getStock()).append("          刘帅萌");
        }
        System.out.println(str);
        EmailUtil.buildEmail("784510419@qq.com","库存不足提示",str.toString());
    }
}
