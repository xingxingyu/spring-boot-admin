package com.test.mysql.model;


import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class ResourceQo extends PageQo {
    private String resourceName;

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
}
