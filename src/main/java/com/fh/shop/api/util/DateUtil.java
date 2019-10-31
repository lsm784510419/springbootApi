package com.fh.shop.api.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static final String Y_M_D="yyyy-MM-dd";
    public static final String FOR_YEAR = "yyyy-MM-dd HH:mm:ss";

    public static final String FOR_TIME = "yyyyMMddHHmmss";

    public static String data2Str(Date date,String patten){
        if (date==null){
            return "";
        }
        SimpleDateFormat sdf=new SimpleDateFormat(patten);
        String strDate = sdf.format(date);
        return strDate;
    }
}
