package com.xuejiaao.foodapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuejiaao.foodapp.entity.Orders;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
    @Results(id="orderlistMap",value = {
            @Result(property = "orderid",column = "orderid"),
            @Result(property = "sid",column = "sid"),
            @Result(property = "cid",column = "cid"),
            @Result(property = "orderstatus",column = "orderstatus"),
            @Result(property = "status",column = "orderstatus"), // 映射前端使用的status字段
            @Result(property = "buyTotalPrice",column = "buyTotalPrice"),
            @Result(property = "totalPrice",column = "buyTotalPrice"), // 映射前端使用的totalPrice字段
            @Result(property = "postprice",column = "postprice"),
            @Result(property = "freightPrice",column = "postprice"), // 映射前端使用的freightPrice字段
            @Result(property = "paytime",column = "paytime"),
            // 一对多关系
            @Result(property = "ordersDetail",column = "orderid",many = @Many(select = "com.xuejiaao.foodapp.mapper.OrdersDetailMapper.findByOrderid")),
            // 一对一关系 - 订单和店铺
            @Result(property = "shop",column = "sid",one = @One(select = "com.xuejiaao.foodapp.mapper.ShopsMapper.selectById")),
            // 一对一关系 - 订单和客户
            @Result(property = "customer",column = "cid",one = @One(select = "com.xuejiaao.foodapp.mapper.UserMapper.selectById"))
    })
    @Select("SELECT * FROM orders WHERE cid = #{cid} ORDER BY ordertime DESC LIMIT #{pageIndex},10")
    public List<Orders> getOrdersList(@Param("cid")Long cid,@Param("pageIndex")Integer pageIndex);
    
    /**
     * 根据店铺ID查询订单列表
     * @param sid 店铺ID
     * @param pageIndex 页码
     * @return 订单列表
     */
    @ResultMap("orderlistMap")
    @Select("SELECT * FROM orders WHERE sid = #{sid} ORDER BY ordertime DESC LIMIT #{pageIndex},10")
    public List<Orders> getOrdersListBySid(@Param("sid")Integer sid,@Param("pageIndex")Integer pageIndex);
    
    /**
     * 根据店铺ID和订单ID查询订单详情
     * @param sid 店铺ID
     * @param orderid 订单ID
     * @return 订单详情
     */
    @ResultMap("orderlistMap")
    @Select("SELECT * FROM orders WHERE sid = #{sid} AND orderid = #{orderid}")
    public Orders getOrderBySidAndOrderid(@Param("sid")Integer sid,@Param("orderid")Long orderid);
    
    /**
     * 根据店铺ID和关键字搜索订单
     * @param sid 店铺ID
     * @param keyword 搜索关键字（订单ID、商品名称）
     * @param pageIndex 页码
     * @return 订单列表
     */
    @ResultMap("orderlistMap")
    @Select("SELECT o.* FROM orders o JOIN orders_detail od ON o.orderid = od.orderid WHERE o.sid = #{sid} AND (o.orderid LIKE CONCAT('%',#{keyword},'%') OR od.gname LIKE CONCAT('%',#{keyword},'%')) GROUP BY o.orderid ORDER BY o.ordertime DESC LIMIT #{pageIndex},10")
    public List<Orders> searchOrdersBySid(@Param("sid")Integer sid,@Param("keyword")String keyword,@Param("pageIndex")Integer pageIndex);
    
    /**
     * 根据店铺ID查询今日订单数量
     * @param sid 店铺ID
     * @return 今日订单数量
     */
    @Select("SELECT COUNT(*) FROM orders WHERE sid = #{sid} AND DATE(ordertime) = CURDATE()")
    public Long getTodayOrdersCount(@Param("sid")Integer sid);
}
