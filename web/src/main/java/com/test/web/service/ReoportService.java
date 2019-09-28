package com.test.web.service;

public interface ReoportService {
    /**
     * 医疗废物院内交接登记表
     *
     * @return
     */
    byte[] buildReport1(String start, String end);


    /**
     * 某一月份，按天的汇总报表
     *
     * @return
     */
    byte[] buildReport2(String month);


    /**
     * 某一月份，按天的汇总报表
     *
     * @return
     */
    byte[] listReport2(String month);

}
