package com.test.mysql.model;


import lombok.Data;

@Data
public class UserlogQo extends PageQo{


    private Long id;
    private String start;
    private String end;

}
