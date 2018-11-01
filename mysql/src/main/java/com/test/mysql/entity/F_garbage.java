package com.test.mysql.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * 电子称数据明细表，报表展示
 * Created by dongmingjun on 2017/5/15.
 */
@Entity(name = "f_garbage")
@Table(name = "f_garbage")
public class F_garbage implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "category_id")
    private String categoryId;  // 物料号
    @Column(name = "category_name")
    private String categoryName; // 物料名称
    @Column(name = "bach_id")
    private String bachId;  //
    @Column(name = "net_weight")
    private Double netWeight;  //  净重
    @Column(nullable = true)
    private Double tare;  //  皮重
    @Column(name = "pre_tare")
    private Double preTare;   //  预置皮重
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "up_date")
    private Date up_Date;  // 上传时间
    @Column(unique = true)
    private Long sn;   //  打印序列号
    private String ip;  //  电子秤ip
    private String department;  //  科室
    private String operator;   //  操作员
    @Column(name = "is_check")
    private Integer isCheck;   //  是否复核1：是，0：否
    @Column(name = "check_dif", nullable = true)
    private Double checkDif;  //  复核差异
    @Column(name = "check_rs")
    private Integer checkRs;   //  复核结果1：表示正常  0：表示异常

    public F_garbage() {
    }

    public F_garbage(String department, String operator) {
        this.department = department;
        this.operator = operator;
    }

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

    public String getBachId() {
        return bachId;
    }

    public void setBachId(String bachId) {
        this.bachId = bachId;
    }

    public Double getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(Double netWeight) {
        this.netWeight = netWeight;
    }

    public Double getTare() {
        return tare;
    }

    public void setTare(Double tare) {
        this.tare = tare;
    }

    public Double getPreTare() {
        return preTare;
    }

    public void setPreTare(Double preTare) {
        this.preTare = preTare;
    }

    public Date getUp_Date() {
        return up_Date;
    }

    public void setUp_Date(Date up_Date) {
        this.up_Date = up_Date;
    }

    public Long getSn() {
        return sn;
    }

    public void setSn(Long sn) {
        this.sn = sn;
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

    public Double getCheckDif() {
        return checkDif;
    }

    public void setCheckDif(Double checkDif) {
        this.checkDif = checkDif;
    }

    public Integer getCheckRs() {
        return checkRs;
    }

    public void setCheckRs(Integer checkRs) {
        this.checkRs = checkRs;
    }
}
