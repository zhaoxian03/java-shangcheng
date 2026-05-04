package com.xuejiaao.foodapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@TableName("cart")
public class Cart implements Serializable {
    @TableId(value = "cartid", type = IdType.AUTO)
    private Long cartid;//'购物车id'
    private Long cid;//'客户id'
    private Integer sid;//'店铺id'
    private Integer buyTotalNums;//'购买总数量'
    private Double buyTotalPrice;//'购买总价格'
    private String beizhu;//'备注'
    @TableField(exist = false)
    private Boolean checked=false; // 是否选中
    @TableField(exist = false)
    private Double postprice; // 邮费
    // 一对多关系
    @TableField(exist = false)
    private List<CartDetail> goodsDetail; // 购物车详细列表
    // 一对一关系
    @TableField(exist = false)
    private Shops shop; // 当前店铺信息
}
