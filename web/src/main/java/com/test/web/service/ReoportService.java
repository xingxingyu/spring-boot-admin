package com.test.web.service;

import com.test.mysql.entity.MonthStatisc;

import java.util.List;

public interface ReoportService {
    /**
     * 医疗废物院内交接登记表
     *
     * @return
     */
    byte[] downloadReport1(String start, String end);


    /**
     * 某一月份，按天的汇总报表
     *
     * @return
     */
    byte[] downloadReport2(String month);


    /**
     * 某一月份，按天的汇总报表
     *
     * @return
     */
    List<MonthStatisc> listReport2(String month);

}
