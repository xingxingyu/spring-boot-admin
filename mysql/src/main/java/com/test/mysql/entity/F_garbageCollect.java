package com.test.mysql.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 电子称数据明细表，报表展示
 * Created by dongmingjun on 2017/5/15.
 */
@Entity
public class F_garbageCollect implements java.io.Serializable {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        F_garbageCollect that = (F_garbageCollect) o;

        if (Double.compare(that.netWeight, netWeight) != 0) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (categoryId != null ? !categoryId.equals(that.categoryId) : that.categoryId != null) return false;
        if (categoryName != null ? !categoryName.equals(that.categoryName) : that.categoryName != null) return false;
        return department != null ? department.equals(that.department) : that.department == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        result = 31 * result + (categoryId != null ? categoryId.hashCode() : 0);
        result = 31 * result + (categoryName != null ? categoryName.hashCode() : 0);
        temp = Double.doubleToLongBits(netWeight);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (department != null ? department.hashCode() : 0);
        return result;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String categoryId;  // 物料号
    private String categoryName; // 物料名称
    private Double netWeight;  //  净重
    private String department;  //  科室

    @Override
    public String toString() {
        return "F_garbageCollect{" +
                "categoryId='" + categoryId + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", netWeight=" + netWeight +
                ", department='" + department + '\'' +
                '}';
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Double getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(Double netWeight) {
        this.netWeight = netWeight;
    }


    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public F_garbageCollect(String categoryId, String categoryName, String department, double netWeight) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.netWeight = netWeight;
        this.department = department;
    }

    public F_garbageCollect() {
    }
}
