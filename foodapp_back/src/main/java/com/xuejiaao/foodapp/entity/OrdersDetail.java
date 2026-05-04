package com.xuejiaao.foodapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("orders_detail")
public class OrdersDetail implements Serializable {
    @TableId(value = "odid", type = IdType.AUTO)
    private Long odid; //'订单详细id'
    private Long orderid; //'订单id'
    private Long gid; // '商品id'
    private Long gdid; //'商品详细id'
    private String gname; //'商品名称'
    private String icon; //'商品图片'
    private String bcontent; //'商品内容'
    private Double buyPrice; //'购买价格'
    private Integer buyNums; //'购买数量'
    private String receiveName; //'收货人姓名'
    private String tel; //'收货人电话'
    private String province; //'省份'
    private String city; //'城市'
    private String district; //'区县'
    private String detailAddress; //'详细地址'
}
