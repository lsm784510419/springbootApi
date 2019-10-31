package com.fh.shop.api.util;


import com.baomidou.mybatisplus.core.toolkit.IdWorker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class IdUtil {
    public static String idXueHua(){
        DateTimeFormatter yyyyMMddHHmm = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        String format = LocalDateTime.now().format(yyyyMMddHHmm);

        return format+IdWorker.getId();
    }
}
