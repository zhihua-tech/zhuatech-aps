/* Copyright 2026 Shanghai Rujing Zhihua Information Technology Co., Ltd. · https://www.zhuatech.cn/ */
package cn.zhuatech.aps.model;
import jakarta.persistence.*;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Entity @Table(name="aps_work_center") public class WorkCenter extends BaseEntity {
    @Column(nullable=false,unique=true,length=32) private String code; @Column(nullable=false,length=80) private String name;
    @Column(nullable=false,length=60) private String workshop; @Column(nullable=false) private int plannedCapacity; @Column(nullable=false) private boolean active=true;
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    protected WorkCenter(){} /**
                              * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                              */
public WorkCenter(String code,String name,String workshop,int plannedCapacity){this.code=code;this.name=name;this.workshop=workshop;this.plannedCapacity=plannedCapacity;}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public String getCode(){return code;} /**
                                           * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                           */
public String getName(){return name;} /**
                                                                                 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                                 */
public String getWorkshop(){return workshop;} /**
                                                                                                                               * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                                                                               */
public int getPlannedCapacity(){return plannedCapacity;} /**
                                                                                                                                                                                        * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                                                                                                                                        */
public boolean isActive(){return active;}
}
