package com.xuejiaao.foodapp.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Address {
    private Long id;              // 地址ID
    private Long userId;          // 用户ID
    private String name;          // 收货人姓名
    private String phone;         // 收货人电话
    private String province;      // 省
    private String city;          // 市
    private String district;      // 区/县
    private String detail;        // 详细地址
    private Integer isDefault;    // 是否默认地址 0-否 1-是
    private Date createTime;      // 创建时间
    private Date updateTime;      // 更新时间
}