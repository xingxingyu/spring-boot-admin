package com.test.web.Utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by dylan on 2017/4/23.
 */
public class DateUtil {

    public static Date getTime(int d_interval, int h_interval, int m_interval, int s_interval) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, d_interval);
        cal.add(Calendar.HOUR_OF_DAY, h_interval);
        cal.add(Calendar.MINUTE, m_interval);
        cal.add(Calendar.SECOND, s_interval);
        return cal.getTime();
    }



}