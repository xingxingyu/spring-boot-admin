package com.test.mysql.entity;


import lombok.Data;

import javax.persistence.*;


/**
 * Created by jiyang on 2017/4/23.
 */
@Entity(name = "device")
@Table(name = "device")
@Data
public class Device implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="num")
    private String num;
    @Column(name="departments")
    private String departments;
    @Column(name="ip")
    private String ip;
    @Column(name="port")
    private String port;
    @Column(name="examine")
    private String examine;
    @Column(name="stat")
    private String  stat;

    
    
    
}
