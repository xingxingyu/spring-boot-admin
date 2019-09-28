package com.test.mysql.model;


import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Data
public class UserQo extends PageQo{
    private Long id;
    private String name;
    private String email;
    private Integer sex;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdateStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdateEnd;

}
