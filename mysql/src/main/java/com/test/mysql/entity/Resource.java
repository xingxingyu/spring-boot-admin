package com.test.mysql.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.naming.NamingEnumeration;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by dylan on 2017/9/17.
 */

@Entity
@Table(name = "resource")
@Data
public class Resource implements java.io.Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "resource_name")
    private String resourceName;
    @Column(name = "resource_desc")
    private String resourceDesc;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_by")
    private String updatedBy;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time")
    private Date updateTime;

    public Resource(String resourceName) {
        this.resourceName = resourceName;
    }
    public Resource() { }

}
