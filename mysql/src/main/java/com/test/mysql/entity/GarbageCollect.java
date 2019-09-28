package com.test.mysql.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

/**
 * 电子称数据明细表，报表展示 Created by dongmingjun on 2017/5/15.
 */
@Entity
@Data
public class GarbageCollect implements java.io.Serializable {
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        GarbageCollect that = (GarbageCollect)o;

        if (Double.compare(that.netWeight, netWeight) != 0)
            return false;
        if (id != null ? !id.equals(that.id) : that.id != null)
            return false;
        if (categoryId != null ? !categoryId.equals(that.categoryId) : that.categoryId != null)
            return false;
        if (categoryName != null ? !categoryName.equals(that.categoryName) : that.categoryName != null)
            return false;
        return department != null ? department.equals(that.department) : that.department == null;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String categoryId;  // 物料号
    private String categoryName; // 物料名称
    private Double netWeight;  //  净重
    private String department;  //  科室
    private String transitp; //装车人
    private Date transitpt;  //装车时间
    private Double mweight;

    public GarbageCollect(String categoryId, String categoryName, String department, Double netWeight, Double mweight,
                          String transitp, Date transitpt) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.netWeight = netWeight;
        this.department = department;
        this.transitp = transitp;
        this.transitpt = transitpt;
        this.mweight = mweight;
    }

    public GarbageCollect() {
    }
}
