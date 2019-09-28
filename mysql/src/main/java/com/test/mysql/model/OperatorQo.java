package com.test.mysql.model;


import lombok.Data;

@Data
public class OperatorQo extends PageQo{
    private String operator;
    private String department;
    private Integer operatorId;
    private Integer departmentId;
}
