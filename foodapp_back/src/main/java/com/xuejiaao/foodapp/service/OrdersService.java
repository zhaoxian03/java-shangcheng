package com.xuejiaao.foodapp.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xuejiaao.foodapp.entity.Orders;
import com.xuejiaao.foodapp.entity.OrdersDetail;
import com.xuejiaao.foodapp.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrdersService {
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrdersDetailMapper ordersDetailMapper;

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private CartDetailMapper cartDetailMapper;

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsDetailMapper goodsDetailMapper;

    /**
     * 添加订单
     * @param ordersList
     * @return
     */
    @Transactional
    public List<Orders> AddOrdersList(List<Orders> ordersList){
        /**
         * 实现思路
         * 1.添加事务，因为操作多张表
         * 2.先添加订单主表数据，再添加从表的数据
         * 3.删除购物车中对应商品数据
         * 4.当前商品销售量更新
         * 5.返回添加成功的订单数据
         */
        //2.先添加订单主表数据，再添加从表的数据
        for (Orders ordersitem:ordersList){
            // 2.1 先添加主表数据
            ordersMapper.insert(ordersitem);
            // 2.2 再添加从表数据
            Long gid = null;
            for (OrdersDetail ordersDetailItem :ordersitem.getOrdersDetail()){
                gid = ordersDetailItem.getGid();
                // 2.2 再添加从表数据
                ordersDetailMapper.insert(ordersDetailItem);
                // 3.删除购物车中对应商品数据
                // 3.1 先删除购物车从表数据
                cartDetailMapper.deleteByCartidAndGdid(ordersitem.getCartid(),ordersDetailItem.getGdid());

                //4.当前商品销售量更新
                //4.1 先更新商品详细表，销售量和库存量
                goodsDetailMapper.updateByGdid(ordersDetailItem.getGdid(),ordersDetailItem.getBuyNums());
            }
            // 3.2 先删除购物车主表数据
            cartMapper.deleteByCartid(ordersitem.getCartid());
            // 3.3 更新购物车主表数据（总购买数量和总价格）
            cartMapper.updateTotalNumsAndTotalPriceByCartid(ordersitem.getCartid());

            //4.2 先更新商品主表，销售量和库存量
            goodsMapper.updateByGid(gid);
        }

        return ordersList;
    }

    /**
     * 根据客户id,查询此客户的订单
     * @param cid
     * @param pageIndex
     * @return
     */
    public List<Orders> getOrdersList(Long cid,Integer pageIndex){
        if(pageIndex==null || pageIndex <=0) pageIndex = 1;
        return ordersMapper.getOrdersList(cid,(pageIndex-1)*10);
    }

    /**
     * 更新当前订单
     * @param orders
     * @return
     */
    @Transactional
    public Orders updateOrders(Orders orders){
        // 更新订单主表
        ordersMapper.updateById(orders);
        
        // 更新订单详情表中的地址信息
        if(orders.getOrdersDetail() != null && orders.getOrdersDetail().size() > 0){
            for(OrdersDetail ordersDetail : orders.getOrdersDetail()){
                // 只更新地址相关字段，使用odid作为更新条件
                ordersDetailMapper.updateById(ordersDetail);
            }
        }
        
        return orders;
    }

    /**
     * 删除订单
     * @param ordersid
     * @return
     */
    public int delOrders(Long ordersid){
        // 1.先删除从表数据
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("orderid",ordersid);
        ordersDetailMapper.delete(queryWrapper);
        // 2.再删除主表数据
        return ordersMapper.deleteById(ordersid);
    }
    
    /**
     * 根据订单ID查询订单
     * @param orderid 订单ID
     * @return 订单信息
     */
    public Orders getOrdersById(Long orderid){
        return ordersMapper.selectById(orderid);
    }
    
    /**
     * 根据店铺ID查询订单列表
     * @param sid 店铺ID
     * @param pageIndex 页码
     * @return 订单列表
     */
    public List<Orders> getOrdersListBySid(Integer sid, Integer pageIndex){
        if(pageIndex == null || pageIndex <= 0) {
            pageIndex = 1;
        }
        return ordersMapper.getOrdersListBySid(sid, (pageIndex - 1) * 10);
    }
    
    /**
     * 根据店铺ID和订单ID查询订单详情
     * @param sid 店铺ID
     * @param orderid 订单ID
     * @return 订单详情
     */
    public Orders getOrderBySidAndOrderid(Integer sid, Long orderid) {
        return ordersMapper.getOrderBySidAndOrderid(sid, orderid);
    }
    
    /**
     * 根据店铺ID和关键字搜索订单
     * @param sid 店铺ID
     * @param keyword 搜索关键字
     * @param pageIndex 页码
     * @return 订单列表
     */
    public List<Orders> searchOrdersBySid(Integer sid, String keyword, Integer pageIndex) {
        if(pageIndex == null || pageIndex <= 0) {
            pageIndex = 1;
        }
        return ordersMapper.searchOrdersBySid(sid, keyword, (pageIndex - 1) * 10);
    }
    
    /**
     * 根据店铺ID查询今日订单数量
     * @param sid 店铺ID
     * @return 今日订单数量
     */
    public Long getTodayOrdersCount(Integer sid) {
        return ordersMapper.getTodayOrdersCount(sid);
    }

}
