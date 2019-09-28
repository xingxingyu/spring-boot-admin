package com.test.mysql.model;


import lombok.Data;

/**
 * Created by dongmingjun on 2017/5/15.
 */
@Data
public class GabageDetailQo extends PageQo{
    private Long id;
    private String categoryId;  // 物料号
    private String categoryName; // 物料名称
    private String ip;  //  电子秤ip
    private String department;  //  科室
    private String operator;   //  操作员
    private Integer isCheck;   //  是否复核1：是，0：否
    private String start;  // 开始时间
    private String end;  // 结束时间

}
