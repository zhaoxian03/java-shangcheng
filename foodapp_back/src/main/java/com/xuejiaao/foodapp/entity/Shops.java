package com.xuejiaao.foodapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "shops")
public class Shops implements Serializable {
    @TableId(value = "sid",type = IdType.AUTO)
    private Integer sid;//'id'
    private String sname;//'门店名称'
    private String slogo;//'店铺logo'
    private String province;//'所在省市区（县）'
    private String saddress;//'详细地址'
    private String phone;//'联系电话'
    private String xgade;//'信誉等级(每月16号更新)：青铜(Lv1---Lv5)、白银(Lv1---Lv5)、黄金(Lv1---Lv5)、抖钻(Lv1---Lv5)',
    private String businesslicense;//'商家资质图片--营业执照'
    private String foodBusinessLicense;//'商家资质图片--食品经营许可证'
    private String otherBusinessLicense;//'其他商家资质图片,多个用;分号分隔'
    private Double overallExperience;//商品体验
    private Double goodsExperience;//'综合体验'
    private Double serviceExperience;//'服务体验'
    private Double logisticsExperience;//'物流体验'
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date ontime;//'上线时间'
    private Integer sstatus;//'状态：0.禁用 1正常 2.休息 '
}
