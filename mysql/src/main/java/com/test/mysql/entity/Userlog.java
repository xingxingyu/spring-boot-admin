package com.test.mysql.entity;


import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by dylan on 2017/4/23.
 */
@Entity
@Table(name = "userlog")
@Data
public class Userlog implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdate;
    private String action;
    private String result;
    private Long targetId;


    public Userlog(String name, Date createdate, String action, String result) {
        this.name = name;
        this.createdate = createdate;
        this.action = action;
        this.result = result;
    }

    public Userlog() {

    }

}
