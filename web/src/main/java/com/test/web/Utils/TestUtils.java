package com.test.web.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by asus on 2016/11/14.
 */
public class TestUtils {

    Logger logger = LoggerFactory.getLogger(TestUtils.class);
    // ����ָ����ʽ������ʱ��
    public static Date stringToDateYmdHms(String dateString) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = sdf.parse(dateString);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return date;
    }
    // ����ָ������ʱ��һ�����Ժ��ʱ��
    public static Date dateToAfterDateYmdHms(int n) {
        Date resDate = null;
        try {
            //  java�����ȡ��ǰʱ�����N���µ���ĩʱ��..
            Calendar cal = Calendar.getInstance();
            //  ��ʾ��ȡ��ǰʱ�����һ���µĵ�һ��
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.add(Calendar.MONTH, 1);
            //��ʽ�����������
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            resDate = sdf.parse(sdf.format(cal.getTime()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return resDate;
    }

    public void testDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        try {
            Date date = simpleDateFormat.parse("15:39:12");
            System.out.println(date.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
