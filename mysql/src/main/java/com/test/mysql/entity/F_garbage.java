package com.test.mysql.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * 电子称数据明细表，报表展示 Created by dongmingjun on 2017/5/15.
 */
@Entity(name = "f_garbage")
@Table(name = "f_garbage")
@Data
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
    @Column(name = "nurse_name")
    private String nurseName;
    @Column(name = "trans_name")
    private String transName;
    @Column(name = "m_time2")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date mtime2;
    private String scheckp;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date schecpt;
    private String transitp;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date transitpt;
    private Double mweight;

    public F_garbage() {
    }

    public F_garbage(String department, String operator) {
        this.department = department;
        this.operator = operator;
    }

}
