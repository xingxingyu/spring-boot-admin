package com.test.mysql.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by dongmingjun on 2017/5/15.
 */
public class ElectronicDataForReportQo  extends PageQo{
    private Long id;
    private String categoryId;  // 物料号
    private String categoryName; // 物料名称

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(Integer isCheck) {
        this.isCheck = isCheck;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    private String ip;  //  电子秤ip
    private String department;  //  科室
    private String operator;   //  操作员
    private Integer isCheck;   //  是否复核1：是，0：否
    private String start;  // 开始时间
    private String end;  // 结束时间

    public ElectronicDataForReportQo() {
    }



}
