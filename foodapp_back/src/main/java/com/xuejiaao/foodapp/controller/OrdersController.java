package com.xuejiaao.foodapp.controller;

import com.xuejiaao.foodapp.entity.Orders;
import com.xuejiaao.foodapp.entity.vo.MyResult;
import com.xuejiaao.foodapp.service.OrdersService;
import com.xuejiaao.foodapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/orders")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;
    
    @Autowired
    private UserService userService;

    @RequestMapping("/addorders")
    public MyResult addOrdersList(@RequestHeader("Authorization") String token, @RequestBody List<Orders> ordersList){
        MyResult myResult = new MyResult();
        try{
            // 验证token并获取用户ID
            Integer userId = userService.validateToken(token);
            if(userId == null){
                myResult.setCode(401);
                myResult.setMsg("无效的token");
                myResult.setData(null);
                return myResult;
            }
            
            // 确保订单数据属于当前用户
            for(Orders order : ordersList){
                order.setCid(Long.valueOf(userId));
            }
            
            List<Orders> reordersList = ordersService.AddOrdersList(ordersList);
            if(reordersList.size()>0){
                myResult.setCode(200);
                myResult.setMsg("success");
                myResult.setData(reordersList);
            }
            else{
                myResult.setCode(302);
                myResult.setMsg("添加订单数据失败");
                myResult.setData(0);
            }
        }catch (Exception ex){
            ex.printStackTrace();
            myResult.setCode(500);
            myResult.setMsg("添加订单数据出错了");
            myResult.setData(null);
        }
        return  myResult;
    }
    @RequestMapping({"/getorderslist/{pageIndex}","/getorderslist"})
    public MyResult getOrdersList(@RequestHeader("Authorization") String token, @PathVariable(value = "pageIndex",required = false) Integer pageIndex){
        MyResult myResult = new MyResult();
        try{
            // 验证token并获取用户ID
            Integer userId = userService.validateToken(token);
            if(userId == null){
                myResult.setCode(401);
                myResult.setMsg("无效的token");
                myResult.setData(null);
                return myResult;
            }
            
            // 使用从token中获取的用户ID查询订单
            List<Orders> reordersList = ordersService.getOrdersList(Long.valueOf(userId), pageIndex);
            if(reordersList.size()>0){
                myResult.setCode(200);
                myResult.setMsg("success");
                myResult.setData(reordersList);
            }
            else{
                myResult.setCode(404);
                myResult.setMsg("没有查询到订单数据");
                myResult.setData(0);
            }
        }catch (Exception ex){
            ex.printStackTrace();
            myResult.setCode(500);
            myResult.setMsg("查询订单数据出错了");
            myResult.setData(null);
        }
        return  myResult;
    }
    @RequestMapping("/updateorders")
    public MyResult updateOrders(@RequestHeader("Authorization") String token, @RequestBody Orders orders){
        MyResult myResult = new MyResult();
        try{
            // 验证token并获取用户ID
            Integer userId = userService.validateToken(token);
            if(userId == null){
                myResult.setCode(401);
                myResult.setMsg("无效的token");
                myResult.setData(null);
                return myResult;
            }
            
            // 验证订单是否属于当前用户
            Orders existingOrder = ordersService.getOrdersById(orders.getOrderid());
            if(existingOrder == null){
                myResult.setCode(404);
                myResult.setMsg("订单不存在");
                myResult.setData(null);
                return myResult;
            }
            
            if(!existingOrder.getCid().equals(Long.valueOf(userId))){
                myResult.setCode(403);
                myResult.setMsg("无权修改该订单");
                myResult.setData(null);
                return myResult;
            }
            
            // 确保订单数据属于当前用户
            orders.setCid(Long.valueOf(userId));
            
            Orders curOrders = ordersService.updateOrders(orders);
            myResult.setCode(200);
            myResult.setMsg("已成功更新订单数据");
            myResult.setData(curOrders);
        }catch (Exception ex){
            ex.printStackTrace();
            myResult.setCode(500);
            myResult.setMsg("更新订单数据出错");
            myResult.setData(0);
        }
        return myResult;
    }
    @RequestMapping("/delorders/{orderid}")
    public MyResult delOrders(@RequestHeader("Authorization") String token, @PathVariable("orderid") Long orderid){
        MyResult myResult = new MyResult();
        try{
            // 验证token并获取用户ID
            Integer userId = userService.validateToken(token);
            if(userId == null){
                myResult.setCode(401);
                myResult.setMsg("无效的token");
                myResult.setData(null);
                return myResult;
            }
            
            // 验证订单是否属于当前用户
            Orders existingOrder = ordersService.getOrdersById(orderid);
            if(existingOrder == null){
                myResult.setCode(404);
                myResult.setMsg("订单不存在");
                myResult.setData(0);
                return myResult;
            }
            
            if(!existingOrder.getCid().equals(Long.valueOf(userId))){
                myResult.setCode(403);
                myResult.setMsg("无权删除该订单");
                myResult.setData(0);
                return myResult;
            }
            
            // 只有订单所有者才能删除订单
            int rows = ordersService.delOrders(orderid);
            if(rows>0){
                myResult.setCode(200);
                myResult.setMsg("已成功删除订单");
                myResult.setData(rows);
            }
            else{
                myResult.setCode(302);
                myResult.setMsg("删除订单失败");
                myResult.setData(0);
            }
        }catch (Exception ex){
            ex.printStackTrace();
            myResult.setCode(500);
            myResult.setMsg("删除订单出错");
            myResult.setData(0);
        }
        return myResult;
    }
}
