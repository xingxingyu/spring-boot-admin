package com.test.web.service;

import org.springframework.stereotype.Service;

@Service
public class ReoportServiceImpl implements ReoportService {


    @Override
    public byte[] buildReport1(String start, String end) {
        return new byte[0];
    }

    @Override
    public byte[] buildReport2(String month) {
        return new byte[0];
    }

    @Override
    public byte[] listReport2(String month) {
        return new byte[0];
    }
}
