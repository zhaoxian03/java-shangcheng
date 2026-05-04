package com.xuejiaao.foodapp.controller;

import com.xuejiaao.foodapp.entity.Cart;
import com.xuejiaao.foodapp.entity.vo.MyResult;
import com.xuejiaao.foodapp.service.CartService;
import com.xuejiaao.foodapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    
    @Autowired
    private UserService userService;

    /**
     * 获取当前登录用户的购物车列表
     * 该方法根据token获取用户ID，然后查询购物车信息，并计算邮费
     * @param token JWT token
     * @return MyResult 包含购物车列表数据的响应结果
     */
    @RequestMapping("/getcartlist")
    public MyResult getCartList(@RequestHeader("Authorization") String token){
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
            
            // 使用从token中获取的用户ID查询购物车
            List<Cart> cartList = cartService.getCartList(Long.valueOf(userId));
            if(cartList!=null && cartList.size()>0){
                myResult.setCode(200);
                myResult.setMsg("已成功加载购物车列表数据");
                myResult.setData(cartList);
            }
            else{
                myResult.setCode(404);
                myResult.setMsg("购物车空空如也");
                myResult.setData(null);
            }

        }catch (Exception ex){
            ex.printStackTrace();
            myResult.setCode(500);
            myResult.setMsg("加载购物车列表数据出错了");
            myResult.setData(null);
        }
       return myResult;
    }

    @RequestMapping("/updatecartlist")
    public MyResult updateCartList(@RequestHeader("Authorization") String token, @RequestBody List<Cart> cartlist){
        System.out.println(cartlist);
        MyResult myResult = new MyResult();
        try {
            // 验证token并获取用户ID
            Integer userId = userService.validateToken(token);
            if(userId == null){
                myResult.setCode(401);
                myResult.setMsg("无效的token");
                myResult.setData(null);
                return myResult;
            }
            
            // 确保购物车数据属于当前用户
            for(Cart cart : cartlist){
                cart.setCid(Long.valueOf(userId));
            }
            
            List<Cart> curcartVoList = cartService.updateCartList(cartlist);
            if(curcartVoList!=null){
                myResult.setCode(200);
                myResult.setMsg("更新购物车");
                myResult.setData(curcartVoList);
            }
            else{
                myResult.setCode(203);
                myResult.setMsg("更新购物车失败");
                myResult.setData(null);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            myResult.setCode(500);
            myResult.setMsg("更新购物车出错了");
            myResult.setData(null);
        }
        return  myResult;
    }
    @RequestMapping("/emptycartlist")
    public MyResult emptyCartList(@RequestHeader("Authorization") String token){
        MyResult myResult = new MyResult();
        try {
            // 验证token并获取用户ID
            Integer userId = userService.validateToken(token);
            if(userId == null){
                myResult.setCode(401);
                myResult.setMsg("无效的token");
                myResult.setData(null);
                return myResult;
            }
            
            // 使用从token中获取的用户ID清空购物车
            int rows = cartService.delCartlist(Long.valueOf(userId));
            if(rows>0){
                myResult.setCode(200);
                myResult.setMsg("已清空购物车");
                myResult.setData(rows);
            }
            else{
                myResult.setCode(203);
                myResult.setMsg("清空购物车失败");
                myResult.setData(null);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            myResult.setCode(500);
            myResult.setMsg("清空购物车出错了");
            myResult.setData(null);
        }
        return  myResult;
    }
}
