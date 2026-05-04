package com.xuejiaao.foodapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("discounts")
public class Discounts {
    @TableId(value = "disid",type = IdType.AUTO)
    private Long disid;//'优惠劵id'
    private Long gid;//'商品id'
    private Integer dtype;//'优惠类型：1.劵后价  2.限时价 3.立减'
    private Double couponSatisfyPrice;//'满足价格
    private Double subPrice;//'减少价格'
    private Date endTime;//'限时结束时间
    private Double timeLimitPrice;//'限时价格
}
