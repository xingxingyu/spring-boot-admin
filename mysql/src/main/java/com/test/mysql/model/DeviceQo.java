package com.test.mysql.model;


import lombok.Data;

@Data
public class DeviceQo extends PageQo {

    private long id;

    private String num;

    private String departments;

    private String ip;

    private String port;

    private String examine;


}
