package com.fh.shop.api.area.biz;

import com.alibaba.fastjson.JSONObject;
import com.fh.shop.api.area.mapper.IAreaMapper;
import com.fh.shop.api.area.po.Area;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("areaService")
@Transactional(rollbackFor = Exception.class)
public class  IAreaServiceImpl implements IAreaService{

    @Autowired
    private IAreaMapper areaMapper;
    @Override
    @Transactional(readOnly = true)
    public ServerResponse findAreaSelect(Long id) {
        String areaListJson = RedisUtil.get("areaList");
        if (StringUtils.isNotEmpty(areaListJson)){
            List<Area> areaList = JSONObject.parseArray(areaListJson, Area.class);
            List<Area> idByFatherId = getIdByFatherId(id, areaList);
            return ServerResponse.success(idByFatherId);
        }
       /* QueryWrapper<Area> areaQueryWrapper = new QueryWrapper<>();
        areaQueryWrapper.eq("fatherId",id);*/
        List<Area> areaList = areaMapper.selectList(null);
        areaListJson = JSONObject.toJSONString(areaList);
        RedisUtil.set("areaList",areaListJson);
        List<Area> idByFatherId = getIdByFatherId(id, areaList);
        return ServerResponse.success(idByFatherId);
    }
    private List<Area> getIdByFatherId(Long id,List<Area> areaList){
        List<Area> areas = new ArrayList<>();
        for (Area area : areaList) {
            if (area.getFatherId() == id){
                areas.add(area);
            }
        }
        return areas;
    }
}
