package entity;


import java.sql.Date;
import java.sql.Timestamp;

/**
 * 电子称数据明细表，报表展示
 * Created by dongmingjun on 2017/5/15.
 */

public class F_garbage implements java.io.Serializable {

    private Long id;
    private String categoryId;  // 物料号
    private String categoryName; // 物料名称
    private String bachId;  // 生产批号
    private double netWeight;  //  净重
    private double tare;  //  皮重
    private double preTare;   //  预置皮重
    private Timestamp up_Date;  // 上传时间
    private Long sn;   //  打印序列号
    private String ip;  //  电子秤ip
    private String department;  //  科室
    private String operator;   //  操作员
    private Integer isCheck;   //  是否复核1：是，0：否
    private double checkDif;  //  复核差异
    private Integer checkRs;   //  复核结果1：表示正常  0：表示异常

    public F_garbage() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getBachId() {
        return bachId;
    }

    public void setBachId(String bachId) {
        this.bachId = bachId;
    }

    public double getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(double netWeight) {
        this.netWeight = netWeight;
    }

    public double getTare() {
        return tare;
    }

    public void setTare(double tare) {
        this.tare = tare;
    }

    public double getPreTare() {
        return preTare;
    }

    public void setPreTare(double preTare) {
        this.preTare = preTare;
    }

    public Timestamp getUp_Date() {
        return up_Date;
    }

    public void setUp_Date(Timestamp up_Date) {
        this.up_Date = up_Date;
    }

    public Long getSn() {
        return sn;
    }

    public void setSn(Long sn) {
        this.sn = sn;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(Integer isCheck) {
        this.isCheck = isCheck;
    }

    public double getCheckDif() {
        return checkDif;
    }

    public void setCheckDif(double checkDif) {
        this.checkDif = checkDif;
    }

    public Integer getCheckRs() {
        return checkRs;
    }

    public void setCheckRs(Integer checkRs) {
        this.checkRs = checkRs;
    }

    @Override
    public String toString() {
        return "F_garbage{" +
                "id=" + id +
                ", categoryId='" + categoryId + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", bachId='" + bachId + '\'' +
                ", netWeight=" + netWeight +
                ", tare=" + tare +
                ", preTare=" + preTare +
                ", up_Date=" + up_Date +
                ", sn=" + sn +
                ", ip='" + ip + '\'' +
                ", department='" + department + '\'' +
                ", operator='" + operator + '\'' +
                ", isCheck=" + isCheck +
                ", checkDif=" + checkDif +
                ", checkRs=" + checkRs +
                '}';
    }

    public F_garbage(String categoryId, String categoryName, double netWeight, Timestamp up_Date, Long sn, String ip, String department,String operator) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.netWeight = netWeight;
        this.up_Date = up_Date;
        this.sn = sn;
        this.ip = ip;
        this.department = department;
        this.operator = operator;
    }
}
