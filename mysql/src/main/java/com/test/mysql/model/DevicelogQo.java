package com.test.mysql.model;


import lombok.Data;

@Data
public class DevicelogQo extends PageQo {
    private long id;
    private String device_id;

    private String start;

    private String end;
}
