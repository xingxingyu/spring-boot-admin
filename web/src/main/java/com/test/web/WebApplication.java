package com.test.web;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import thread.CheckStatThread;
import dao.DeviceDao;
import dao.DeviceDaoImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import thread.SaleRecordThread;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SpringBootApplication
@ComponentScan(basePackages = "com.test")
public class WebApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
        Thread thread = new Thread(new CheckStatThread());
        thread.start();
    }
}
