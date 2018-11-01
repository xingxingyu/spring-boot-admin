package com.test.mysql.entity;


import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.util.Date;

/**
 * Created by jiyang on 2017/4/23.
 */
@Entity(name = "device")
@Table(name = "device")
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDepartments() {
		return departments;
	}

	public void setDepartments(String departments) {
		this.departments = departments;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getExamine() {
		return examine;
	}

	public void setExamine(String examine) {
		this.examine = examine;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}
    
    
    
}
