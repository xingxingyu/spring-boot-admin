package com.test.mysql.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by dylan on 2017/9/17.
 */
@Entity
@Table(name = "authority")
@Data
public class Authority implements java.io.Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "authority_name")
    private String authorityName;
    @Column(name = "authority_desc")
    private String authorityDesc;
    @ManyToMany(cascade = {}, fetch = FetchType.EAGER)
    @JoinTable(name = "authority_resource",
            joinColumns = {@JoinColumn(name = "authority_id")},
            inverseJoinColumns = {@JoinColumn(name = "resource_id")})
    private List<Resource> resources;
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

    public Authority(String authorityName, List<Resource> resources) {
        this.authorityName = authorityName;
        this.resources = resources;
    }

}
