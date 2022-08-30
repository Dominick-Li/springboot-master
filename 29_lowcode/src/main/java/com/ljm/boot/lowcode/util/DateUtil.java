package com.ljm.boot.lowcode.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtil {

    private DateUtil(){}

    /**
     * 获取当前日期加一天的时间字符串
     */
    public static String getDateStrIncrement(String dateStr) {
        String result;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(dateStr));//设置起时间
            //cal.add(Calendar.YEAR, 1);//增加一年
            cal.add(Calendar.DATE, 1);//增加一天
            //cal.add(Calendar.DATE, -10);//减10天
            //cal.add(Calendar.MONTH, n);//增加一个月
            result = sdf.format(cal.getTime());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取当前日期增加指定时间的格式化字符
     *
     * @param date     原始时间
     * @param time     增加的数值
     * @param timeUnit 单位
     * @return
     */
    public static Date getDateStrIncrement(Date date, Integer time, TimeUnit timeUnit) {
        Calendar cal = Calendar.getInstance();
        //设置开始时间
        cal.setTime(date);
        switch (timeUnit) {
            case SECONDS:
                cal.add(Calendar.SECOND, time);
                break;
            case MINUTES:
                //增加分钟
                cal.add(Calendar.MINUTE, time);
                break;
            case HOURS:
                //增加小时
                cal.add(Calendar.HOUR, time);
                break;
            case DAYS:
                //增加天
                cal.add(Calendar.DATE, time);
                break;
        }
        return cal.getTime();
    }

}
