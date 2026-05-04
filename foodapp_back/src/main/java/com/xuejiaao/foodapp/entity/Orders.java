package com.xuejiaao.foodapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@TableName("orders")
public class Orders implements Serializable {
    @TableId(value = "orderid",type = IdType.INPUT)
    private Long orderid; //'订单id（cid+时间毫秒数）',
    private Long cid; //'客户id'
    private Integer sid; //'门店id'
    private Date ordertime; //'订单时间'
    private Date paytime; //'支付时间'
    private Integer orderstatus; //'订单状态： 0.待支付 1.已支付  2.已完成 3.已取消 4.退单'
    private String orderstatusStr; //'订单状态： 0.待支付 1.已支付  2.已完成 3.已取消 4.退单'
    private Integer buyTotalNums; //'购物数量'
    private Double buyTotalPrice; //'购买总价格'
    private Double postprice; //'邮费'
    private String beizhu; //'备注'
    @TableField(exist = false)
    private Long cartid; // 购物车id
    // 一对多关系 ，一个订单有多个订单详细
    @TableField(exist = false)
    private List<OrdersDetail> ordersDetail; // 订单详细列表
    // 一对一关系
    @TableField(exist = false)
    private Shops shop; // 当前店铺信息
    // 一对一关系 - 订单和客户
    @TableField(exist = false)
    private User customer; // 当前订单的客户信息
    // 订单状态（前端使用，映射自orderstatus）
    @TableField(exist = false)
    private Integer status;
    // 订单金额（前端使用）
    @TableField(exist = false)
    private Double totalPrice;
    // 运费（前端使用）
    @TableField(exist = false)
    private Double freightPrice;
    // 发货时间（前端使用）
    @TableField(exist = false)
    private Date deliverytime;
    // 完成时间（前端使用）
    @TableField(exist = false)
    private Date finishTime;
    // 支付方式（前端使用）
    @TableField(exist = false)
    private Integer paymentMethod;
    // 支付状态（前端使用）
    @TableField(exist = false)
    private Integer payStatus;
    // 物流信息（前端使用）
    @TableField(exist = false)
    private List<Map<String, Object>> logisticsInfo;
}
