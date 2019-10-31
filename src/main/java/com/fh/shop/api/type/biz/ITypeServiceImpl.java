package com.fh.shop.api.type.biz;

import com.alibaba.fastjson.JSONObject;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.type.mapper.ITypeMapper;
import com.fh.shop.api.type.po.Type;
import com.fh.shop.api.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("typeService")
@Transactional(rollbackFor = Exception.class)
public class ITypeServiceImpl implements ITypeService {
    @Autowired
    private ITypeMapper typeMapper;
    @Override
    @Transactional(readOnly = true)
    public ServerResponse findTypeList() {
        //从缓存服务器中取数据
        String typeListAllJson = RedisUtil.get("typeListAll");
        //判断缓存中是否有数据 有的话经过转换 直接返回
        if (StringUtils.isNotEmpty(typeListAllJson)){
            List<Type> typeList = JSONObject.parseArray(typeListAllJson, Type.class);
            return ServerResponse.success(typeList);
        }
        //走到这里 证明缓存中没有数据  需要去数据库查
        List<Type> typedList = typeMapper.selectList(null);
        //转换数据
        typeListAllJson = JSONObject.toJSONString(typedList);
        //存入缓存服务器
        RedisUtil.set("typeListAll",typeListAllJson);
        return ServerResponse.success(typedList);
    }
}
