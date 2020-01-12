package com.test.mysql.entity;

import lombok.Data;


import javax.persistence.*;

@Data
@Entity
public class MonthStatisc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sdate;
    private String categoryName; // 物料名称
    private Double netWeight;
    private Long peitaiNum;

    public MonthStatisc() {
        this.id = id;
        this.sdate = sdate;
        this.categoryName = categoryName;
        this.netWeight = netWeight;
    }
}
