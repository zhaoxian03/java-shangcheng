package com.xuejiaao.foodapp.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xuejiaao.foodapp.entity.Cart;
import com.xuejiaao.foodapp.entity.CartDetail;
import com.xuejiaao.foodapp.entity.Goods;
import com.xuejiaao.foodapp.mapper.CartDetailMapper;
import com.xuejiaao.foodapp.mapper.CartMapper;
import com.xuejiaao.foodapp.mapper.GoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private CartDetailMapper cartDetailMapper;
    @Autowired
    private GoodsMapper goodsMapper;

    /**
     * 根据客户cid，查询此客户的购物车列表数据
     * @param cid
     * @return
     */
    public List<Cart> getCartList(Long cid){
        List<Cart> cartList =  cartMapper.getCartList(cid);
        for (Cart cartitem :cartList) {
            Double postprice = 0.0;
            for (CartDetail cartDetailitem:cartitem.getGoodsDetail()){
                // 根据当前商品id,查询引商品邮费
                Goods goods = goodsMapper.selectById(cartDetailitem.getGid());
                postprice += goods.getPostprice();
                cartitem.setPostprice(postprice);
            }
        }
        return cartList;
    }

    /**
     * 清空购物车
     * @param cid
     * @return
     */
    @Transactional
    public int delCartlist(Long cid){
        // 1.根据主表购物车数据，先删除从表数据
        // 2.再删除主表数据
        // 1.根据主表购物车数据，先删除从表数据
        int rows = cartDetailMapper.deleteCartDetailByCid(cid);
        // 2.再删除主表数据
        rows += cartMapper.deleteCartByCid(cid);
        return rows;
    }
    /**
     * 更新购物车的数据
     * @param cartList
     * @return
     */
    @Transactional
    public List<Cart> updateCartList(List<Cart> cartList){
        /**
         * 实现思路
         * 1.注意在操作时候，要做事务
         * 2.删除此客户原来购物车里的数据（主表、从表）
         * 3.再添加现在购物车内的数据（主表、从表）
         * 4.返回操作完成的购物车数据
         */

       // 1.注意在操作时候，要做事务
       // 2.删除此客户原来购物车里的数据（主表、从表）
        Long cid = 1L;
        if (!cartList.isEmpty()) {
            cid = cartList.get(0).getCid();
            // 2.1 先删除从表的数据
            cartDetailMapper.deleteCartDetailByCid(cid);
            // 2.2 再删除主表的数据
            cartMapper.deleteCartByCid(cid);
        }
        // 3.再添加现在购物车内的数据（主表、从表）
        for (Cart cartitem:cartList) {
            // 3.1 先添加主表的数据
            cartMapper.insert(cartitem);
            // 3.2 再添加从表的数据
            for (CartDetail cartDetailitem:cartitem.getGoodsDetail()) {
                cartDetailitem.setCartid(cartitem.getCartid());
                cartDetailMapper.insert(cartDetailitem);
            }
        }
        // 4.返回操作完成的购物车数据
        return  getCartList(cid);
    }
}
